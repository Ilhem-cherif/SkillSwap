package com.skillswap.user_service.controller;

import com.skillswap.user_service.dto.JwtResponseDto;
import com.skillswap.user_service.dto.UserLoginDto;
import com.skillswap.user_service.dto.UserRegisterDto;
import com.skillswap.user_service.entities.User;
import com.skillswap.user_service.security.JwtUtil;
import com.skillswap.user_service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private AuthenticationManager authenticationManager;
    @PostMapping("/api/auth/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDto dto){
        User user = userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("user created with Id : "+ user.getId());
    }
    @PostMapping("/api/auth/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto dto){
        try {
            User user = userService.authenticate(dto.getUsernameOrEmail(), dto.getPassword());
            String token = jwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(new JwtResponseDto(token));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}
