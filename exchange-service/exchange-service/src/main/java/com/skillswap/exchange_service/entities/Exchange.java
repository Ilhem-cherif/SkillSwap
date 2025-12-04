package com.skillswap.exchange_service.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "exchanges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long skillId;

    private String requester; // username
    private String provider;  // username

    @Enumerated(EnumType.STRING)
    private ExchangeStatus status;

    private Long creditsAmount; // amount to transfer when accepted (in cents)

    private String message; // optional message from requester

    private Instant createdAt;
    private Instant updatedAt;
}
