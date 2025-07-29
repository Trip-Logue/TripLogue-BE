package com.triploguebe.user.controller;

import com.triploguebe.global.jwt.JwtProvider;
import com.triploguebe.user.dto.*;
import com.triploguebe.user.dto.LoginRequest;
import com.triploguebe.user.dto.SignUpRequest;
import com.triploguebe.user.dto.UserResponse;
import com.triploguebe.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.triploguebe.user.dto.ProfileUploadRequest;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        UserResponse response = userService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        UserResponse userResponse = userService.login(request.getUsername(), request.getPassword(), response);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            HttpServletRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletResponse response) {

        String refreshToken = jwtProvider.extractTokenFromCookie(request, "refreshToken");

        if (refreshToken == null && authHeader != null && authHeader.startsWith("Bearer ")) {
            refreshToken = authHeader.substring(7);
        }

        if (refreshToken == null || !jwtProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("리프레시 토큰이 없거나 유효하지 않습니다.");
        }

        String username = jwtProvider.getUsernameFromToken(refreshToken);

        if (!userService.isRefreshTokenValid(username, refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("리프레시 토큰이 일치하지 않습니다.");
        }

        List<String> roles = Collections.singletonList("USER");
        String newAccessToken = jwtProvider.createToken(username, roles, 1000L * 60 * 60);

        String newRefreshToken = userService.renewRefreshToken(username, response);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", newAccessToken);
        return ResponseEntity.ok(tokenMap);
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

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteUser(Principal principal) {
        String username = principal.getName();

        userService.deleteUser(username);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "회원 탈퇴가 완료되었습니다.");
        response.put("success", true);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordUpdateRequest request, Principal principal) {
        String username = principal.getName();
        userService.updatePassword(username, request.getOldPassword(), request.getNewPassword());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "요청에 성공했습니다.");
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        userService.removeRefreshToken(username);

        return ResponseEntity.ok("정상적으로 로그아웃 처리되었습니다.");
    }

}
