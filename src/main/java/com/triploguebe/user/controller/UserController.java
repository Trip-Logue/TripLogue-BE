package com.triploguebe.user.controller;

import com.triploguebe.user.dto.LoginRequest;
import com.triploguebe.user.dto.PasswordUpdateRequest;
import com.triploguebe.user.dto.SignUpRequest;
import com.triploguebe.user.dto.UserResponse;
import com.triploguebe.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        UserResponse response = userService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return "";
    }
}

