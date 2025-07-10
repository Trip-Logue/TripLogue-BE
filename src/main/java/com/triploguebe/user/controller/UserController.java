package com.triploguebe.user.controller;

import com.triploguebe.user.dto.*;
import com.triploguebe.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.triploguebe.user.dto.ProfileUploadRequest;

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
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
        UserResponse userResponse = userService.login(request.getUsername(), request.getPassword());

        String jwtToken = userResponse.getToken();

        ResponseCookie cookie = ResponseCookie.from("jwt", jwtToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(60 * 60 * 24)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(userResponse);
    }

    @PostMapping("/profile")
    public ResponseEntity<Map<String, Object>> updateProfile(
            @RequestBody ProfileUploadRequest request,
            Principal principal) {

        userService.updateProfile(principal.getName(), request);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "요청에 성공했습니다.");
        response.put("success", true);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("인증이 필요합니다.");
        }

        UserResponse response = userService.getCurrentUser(principal.getName());
        return ResponseEntity.ok(response);
    }


}
