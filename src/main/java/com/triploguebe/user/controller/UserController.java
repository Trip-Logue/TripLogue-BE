package com.triploguebe.user.controller;

import com.triploguebe.user.dto.LoginRequest;
import com.triploguebe.user.dto.PasswordUpdateRequest;
import com.triploguebe.user.dto.SignUpRequest;
import com.triploguebe.user.dto.UserResponse;
import com.triploguebe.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.HttpHeaders;
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

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(Principal principal) {
        String username = principal.getName();

        userService.deleteUser(username);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "회원 탈퇴가 완료되었습니다.");
        response.put("success", true);

        return ResponseEntity.ok(response);
    }

}
