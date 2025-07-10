package com.triploguebe.user.dto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String profileImageUrl;
    private String token;
}

