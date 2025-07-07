package com.triploguebe.user.controller;

import com.triploguebe.user.dto.LoginRequest;
import com.triploguebe.user.dto.SignUpRequest;
import com.triploguebe.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public String signUp(@RequestBody SignUpRequest request) {
        return "";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return "";
    }
}

