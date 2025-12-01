package com.skillswap.skill_service.controllers;

import com.skillswap.skill_service.dto.*;
import com.skillswap.skill_service.services.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    // Anyone can view all skills
    @GetMapping
    public ResponseEntity<List<SkillDto>> listAll() {
        return ResponseEntity.ok(skillService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(skillService.getSkill(id));
    }

    // Create skill - requires gateway-authenticated user
    @PostMapping
    public ResponseEntity<SkillDto> create(@RequestHeader("X-Authenticated-User") String username,
                                           @RequestBody SkillCreateDto dto) {
        // Gateway secret is validated by a filter (explained below)
        return ResponseEntity.ok(skillService.createSkill(username, dto));
    }

    // Update skill - owner only
    @PutMapping("/{id}")
    public ResponseEntity<SkillDto> update(@PathVariable Long id,
                                           @RequestHeader("X-Authenticated-User") String username,
                                           @RequestBody SkillUpdateDto dto) {
        return ResponseEntity.ok(skillService.updateSkill(id, username, dto));
    }

    // List by owner
    @GetMapping("/mine")
    public ResponseEntity<List<SkillDto>> mySkills(@RequestHeader("X-Authenticated-User") String username) {
        return ResponseEntity.ok(skillService.listByOwner(username));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @RequestHeader("X-Authenticated-User") String username) {
        skillService.deleteSkill(id, username);
        return ResponseEntity.ok().build();
    }
}
