package com.skillswap.skill_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "skills")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Owner username (from gateway header X-Authenticated-User)
    private String ownerUsername;

    private String title;
    private String description;
    private String level; // beginner/intermediate/advanced
    private String tags; // comma-separated (simple approach)
}
