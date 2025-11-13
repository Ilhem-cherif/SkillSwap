package com.skillswap.skill_service.services;

import com.skillswap.skill_service.repository.SkillRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SkillService {
    private final SkillRepository skillRepository;
}
