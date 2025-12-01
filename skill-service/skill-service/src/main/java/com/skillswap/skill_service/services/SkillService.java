package com.skillswap.skill_service.services;

import com.skillswap.skill_service.dto.*;
import com.skillswap.skill_service.entities.Skill;
import com.skillswap.skill_service.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillDto toDto(Skill s){
        return SkillDto.builder()
                .id(s.getId())
                .ownerUsername(s.getOwnerUsername())
                .title(s.getTitle())
                .description(s.getDescription())
                .level(s.getLevel())
                .tags(s.getTags())
                .build();
    }

    public SkillDto createSkill(String ownerUsername, SkillCreateDto dto){
        Skill s = Skill.builder()
                .ownerUsername(ownerUsername)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .level(dto.getLevel())
                .tags(dto.getTags())
                .build();
        Skill saved = skillRepository.save(s);
        return toDto(saved);
    }

    public SkillDto updateSkill(Long id, String ownerUsername, SkillUpdateDto dto){
        Skill s = skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found"));
        if (!s.getOwnerUsername().equals(ownerUsername)) {
            throw new RuntimeException("Not authorized to update this skill");
        }
        s.setTitle(dto.getTitle());
        s.setDescription(dto.getDescription());
        s.setLevel(dto.getLevel());
        s.setTags(dto.getTags());
        skillRepository.save(s);
        return toDto(s);
    }

    public SkillDto getSkill(Long id){
        Skill s = skillRepository.findById(id).orElseThrow(() -> new RuntimeException("Skill not found"));
        return toDto(s);
    }

    public List<SkillDto> listByOwner(String ownerUsername){
        return skillRepository.findByOwnerUsername(ownerUsername).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<SkillDto> listAll(){
        return skillRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public void deleteSkill(Long id, String ownerUsername){
        Skill s = skillRepository.findById(id).orElseThrow(() -> new RuntimeException("Skill not found"));
        if (!s.getOwnerUsername().equals(ownerUsername)) throw new RuntimeException("Not authorized");
        skillRepository.delete(s);
    }
}
