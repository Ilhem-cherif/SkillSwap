package com.skillswap.user_service.dto;

import lombok.Data;

@Data
public class UserProfileUpdateDto {
    private String displayName;
    private String bio;
    private String location;
}
