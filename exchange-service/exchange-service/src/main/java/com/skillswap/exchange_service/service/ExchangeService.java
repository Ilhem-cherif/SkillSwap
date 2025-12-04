package com.skillswap.exchange_service.service;

import com.skillswap.exchange_service.dto.*;
import com.skillswap.exchange_service.entities.*;
import com.skillswap.exchange_service.exception.*;
import com.skillswap.exchange_service.repository.ExchangeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExchangeService {

    private final ExchangeRepository repo;

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${credits.service.base-url:http://credits-service}")
    private String creditsBaseUrl;

    // create request
    public ExchangeResponseDto createExchange(String requester, CreateExchangeRequestDto req) {
        if (req.getProvider() == null || req.getSkillId() == null || req.getCreditsAmount() == null) {
            throw new BadRequestException("provider, skillId and creditsAmount are required");
        }
        Exchange e = Exchange.builder()
                .skillId(req.getSkillId())
                .provider(req.getProvider())
                .requester(requester)
                .status(ExchangeStatus.PENDING)
                .creditsAmount(req.getCreditsAmount())
                .message(req.getMessage())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        Exchange saved = repo.save(e);
        return toDto(saved);
    }

    public ExchangeResponseDto getExchange(Long id) {
        Exchange e = repo.findById(id).orElseThrow(() -> new NotFoundException("Exchange not found"));
        return toDto(e);
    }

    public List<ExchangeResponseDto> listForRequester(String requester) {
        return repo.findByRequester(requester).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<ExchangeResponseDto> listForProvider(String provider) {
        return repo.findByProvider(provider).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public ExchangeResponseDto acceptExchange(Long id, String provider) {
        Exchange e = repo.findById(id).orElseThrow(() -> new NotFoundException("Exchange not found"));
        if (!e.getProvider().equals(provider)) throw new BadRequestException("Only the provider can accept this exchange");
        if (e.getStatus() != ExchangeStatus.PENDING) throw new BadRequestException("Only pending exchanges can be accepted");

        // Call credits-service to transfer credits from requester -> provider
        // Build transfer request payload
        String url = creditsBaseUrl + "/api/credits/transfer";

        // prepare transfer body
        var body = new java.util.HashMap<String, Object>();
        body.put("toUsername", e.getProvider());
        body.put("amount", e.getCreditsAmount());
        body.put("description", "Exchange #" + id + " payment");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Note: this internal call is a service-to-service call; we don't need Authorization header here
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> resp = restTemplate.postForEntity(url, entity, String.class);
            if (resp.getStatusCode().is2xxSuccessful()) {
                e.setStatus(ExchangeStatus.ACCEPTED);
                e.setUpdatedAt(Instant.now());
                repo.save(e);
                return toDto(e);
            } else {
                throw new RuntimeException("Failed to transfer credits, status: " + resp.getStatusCode());
            }
        } catch (Exception ex) {
            throw new RuntimeException("Credits transfer failed: " + ex.getMessage());
        }
    }

    @Transactional
    public ExchangeResponseDto rejectExchange(Long id, String provider, ChangeStatusDto dto) {
        Exchange e = repo.findById(id).orElseThrow(() -> new NotFoundException("Exchange not found"));
        if (!e.getProvider().equals(provider)) throw new BadRequestException("Only the provider can reject");
        if (e.getStatus() != ExchangeStatus.PENDING) throw new BadRequestException("Only pending exchanges can be rejected");
        e.setStatus(ExchangeStatus.REJECTED);
        e.setUpdatedAt(Instant.now());
        repo.save(e);
        return toDto(e);
    }

    @Transactional
    public ExchangeResponseDto completeExchange(Long id, String caller) {
        Exchange e = repo.findById(id).orElseThrow(() -> new NotFoundException("Exchange not found"));
        if (e.getStatus() != ExchangeStatus.ACCEPTED) throw new BadRequestException("Only accepted exchanges can be completed");
        // Optionally verify caller is provider or requester; keep simple: allow provider or requester
        if (!caller.equals(e.getProvider()) && !caller.equals(e.getRequester())) {
            throw new BadRequestException("Not authorized to complete");
        }
        e.setStatus(ExchangeStatus.COMPLETED);
        e.setUpdatedAt(Instant.now());
        repo.save(e);
        return toDto(e);
    }

    private ExchangeResponseDto toDto(Exchange e) {
        return ExchangeResponseDto.builder()
                .id(e.getId())
                .skillId(e.getSkillId())
                .requester(e.getRequester())
                .provider(e.getProvider())
                .status(e.getStatus())
                .creditsAmount(e.getCreditsAmount())
                .message(e.getMessage())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}
