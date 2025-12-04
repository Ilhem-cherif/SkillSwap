package com.skillswap.credits_service.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // types: TOPUP, TRANSFER_OUT, TRANSFER_IN, CHARGE, REFUND
    private String type;

    private Long amount; // cents, positive

    private String fromUser; // may be null for TOPUP
    private String toUser;   // may be null for CHARGE

    private Instant createdAt;
    private String description;
}
