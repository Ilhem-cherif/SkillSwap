package com.skillswap.exchange_service.dto;

import com.skillswap.exchange_service.entities.ExchangeStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ExchangeResponseDto {
    private Long id;
    private Long skillId;
    private String requester;
    private String provider;
    private ExchangeStatus status;
    private Long creditsAmount;
    private String message;
    private Instant createdAt;
    private Instant updatedAt;
}