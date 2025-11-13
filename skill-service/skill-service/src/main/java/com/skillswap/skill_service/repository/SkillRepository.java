package com.skillswap.skill_service.repository;

import com.skillswap.skill_service.entities.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findByOwnerUsername(String ownerUsername);
}
