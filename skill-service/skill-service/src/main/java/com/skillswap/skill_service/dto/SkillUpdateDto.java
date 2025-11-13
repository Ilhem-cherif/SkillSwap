package com.skillswap.skill_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillUpdateDto {
    private String title;
    private String description;
    private String level;
    private String tags;

}
