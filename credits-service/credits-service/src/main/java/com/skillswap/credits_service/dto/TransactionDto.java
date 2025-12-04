package com.skillswap.credits_service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class TransactionDto {
    private Long id;
    private String type;
    private Long amount;
    private String fromUser;
    private String toUser;
    private Instant createdAt;
    private String description;
}