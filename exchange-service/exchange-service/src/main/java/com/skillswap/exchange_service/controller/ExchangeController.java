package com.skillswap.exchange_service.controller;

import com.skillswap.exchange_service.dto.*;
import com.skillswap.exchange_service.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exchanges")
@RequiredArgsConstructor
public class ExchangeController {

    private final ExchangeService exchangeService;

    // Create exchange (requester)
    @PostMapping
    public ResponseEntity<ExchangeResponseDto> createExchange(
            @RequestHeader("X-Authenticated-User") String requester,
            @RequestBody CreateExchangeRequestDto req) {
        return ResponseEntity.ok(exchangeService.createExchange(requester, req));
    }

    // Get by id
    @GetMapping("/{id}")
    public ResponseEntity<ExchangeResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(exchangeService.getExchange(id));
    }

    // List for requester
    @GetMapping("/my-requests")
    public ResponseEntity<List<ExchangeResponseDto>> myRequests(@RequestHeader("X-Authenticated-User") String requester) {
        return ResponseEntity.ok(exchangeService.listForRequester(requester));
    }

    // List for provider
    @GetMapping("/incoming")
    public ResponseEntity<List<ExchangeResponseDto>> incoming(@RequestHeader("X-Authenticated-User") String provider) {
        return ResponseEntity.ok(exchangeService.listForProvider(provider));
    }

    // Provider accepts
    @PostMapping("/{id}/accept")
    public ResponseEntity<ExchangeResponseDto> accept(@PathVariable Long id,
                                                      @RequestHeader("X-Authenticated-User") String provider) {
        return ResponseEntity.ok(exchangeService.acceptExchange(id, provider));
    }

    // Provider rejects
    @PostMapping("/{id}/reject")
    public ResponseEntity<ExchangeResponseDto> reject(@PathVariable Long id,
                                                      @RequestHeader("X-Authenticated-User") String provider,
                                                      @RequestBody(required = false) ChangeStatusDto dto) {
        return ResponseEntity.ok(exchangeService.rejectExchange(id, provider, dto == null ? new ChangeStatusDto() : dto));
    }

    // Complete
    @PostMapping("/{id}/complete")
    public ResponseEntity<ExchangeResponseDto> complete(@PathVariable Long id,
                                                        @RequestHeader("X-Authenticated-User") String caller) {
        return ResponseEntity.ok(exchangeService.completeExchange(id, caller));
    }
}
