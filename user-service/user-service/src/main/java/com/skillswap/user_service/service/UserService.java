package com.skillswap.user_service.service;

import com.skillswap.user_service.dto.UserProfileDto;
import com.skillswap.user_service.dto.UserProfileUpdateDto;
import com.skillswap.user_service.dto.UserRegisterDto;
import com.skillswap.user_service.entities.User;
import com.skillswap.user_service.repository.UserRepository;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User register(UserRegisterDto dto){
        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .displayname(dto.getDisplayname())
                .build();
        return userRepository.save(user);
    }

    public User authenticate(String usernameOrEmail, String password) {
        User user = userRepository.findByUsername(usernameOrEmail)
                .orElse(userRepository.findByEmail(usernameOrEmail)
                        .orElseThrow(() -> new RuntimeException("User not found")));
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("Invalid password");
        }
        return user;
    }
    public UserProfileDto getUserProfile(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User not found"));
        return UserProfileDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .displayname(user.getDisplayname())
                .bio(user.getBio())
                .location(user.getLocation())
                .build();
    }
    public UserProfileDto updateUserProfile(String username, UserProfileUpdateDto dto){
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User not found"));
        user.setDisplayname(dto.getDisplayName());
        user.setBio(dto.getBio());
        user.setLocation(dto.getLocation());
        userRepository.save(user);
        return getUserProfile(username);
    }
}
