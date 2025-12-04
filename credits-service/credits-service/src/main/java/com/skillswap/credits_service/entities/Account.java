package com.skillswap.credits_service.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "accounts", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // username from gateway header (unique)
    @Column(nullable = false, unique = true)
    private String username;

    // balance stored in cents
    @Column(nullable = false)
    private Long balance;
}
