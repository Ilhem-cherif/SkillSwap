package com.skillswap.user_service.dto;


import lombok.Data;

@Data
public class UserLoginDto {
    private String usernameOrEmail;
    private String password;
}
