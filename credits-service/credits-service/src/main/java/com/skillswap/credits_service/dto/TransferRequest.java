package com.skillswap.credits_service.dto;

import lombok.Data;

@Data
public class TransferRequest {
    private String toUsername;
    private Long amount; // cents
    private String description;
}
