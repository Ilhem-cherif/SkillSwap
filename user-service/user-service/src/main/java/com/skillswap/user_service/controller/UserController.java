package com.skillswap.user_service.controller;

import com.skillswap.user_service.dto.UserProfileDto;
import com.skillswap.user_service.dto.UserProfileUpdateDto;
import com.skillswap.user_service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getUserProfile(@RequestHeader("X-Authenticated-User") String username){
        return ResponseEntity.ok(userService.getUserProfile(username));
    }
    @PutMapping("/me")
    public ResponseEntity<UserProfileDto> updateUserProfile(@RequestHeader("X-Authenticated-User") String username, @RequestBody UserProfileUpdateDto dto){
        return ResponseEntity.ok(userService.updateUserProfile(username, dto));
    }
}
