package com.skillswap.exchange_service.dto;

import lombok.Data;

@Data
public class CreateExchangeRequestDto {
    private Long skillId;
    private String provider; // username of provider
    private Long creditsAmount; // cents
    private String message;
}
