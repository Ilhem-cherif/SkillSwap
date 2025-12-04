package com.skillswap.credits_service.dto;

import lombok.Data;

@Data
public class TopUpRequest {
    private Long amount; // cents
    private String description;
}