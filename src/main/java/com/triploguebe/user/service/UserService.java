package com.triploguebe.user.service;

import com.triploguebe.global.exception.CustomException;
import com.triploguebe.global.exception.ErrorCode;
import com.triploguebe.user.dto.ProfileUploadRequest;
import com.triploguebe.user.dto.SignUpRequest;
import com.triploguebe.user.dto.UserResponse;
import com.triploguebe.user.entity.User;
import com.triploguebe.user.repository.UserRepository;
import com.triploguebe.global.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.triploguebe.global.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Value("${custom.cookie.secure}")
    private boolean secureCookie;

    private final long EXPIRE_MILLIS = 1000L * 60 * 60 * 24;

    public UserResponse signUp(SignUpRequest request) {
        String username = request.getUsername();
        String email = request.getEmail();

        // DB에 이미 회원이 있는지 검증
        if (userRepository.existsByUsername(username)) {
            throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
        }

        if (userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 사용자 엔티티 생성
        User user = User.builder()
                        .username(username)
                        .password(passwordEncoder.encode(request.getPassword()))
                        .email(email)
                        .profileImageUrl(request.getProfileImageUrl())
                        .build();

        // 저장
        User saved = userRepository.save(user);

        // 저장된 사용자 정보 응답
        return UserResponse.builder()
                .id(saved.getId())
                .username(saved.getUsername())
                .email(saved.getEmail())
                .profileImageUrl(saved.getProfileImageUrl())
                .build();
    }

    public UserResponse login(String username, String password, HttpServletResponse response) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        List<String> roles = Collections.singletonList("USER");

        // 액세스 토큰 생성 (예: 1시간 유효)
        String accessToken = jwtProvider.createToken(user.getUsername(), roles, 1000L * 60 * 60);

        // 리프레시 토큰 생성 (예: 14일 유효)
        String refreshToken = jwtProvider.createRefreshToken(user.getUsername(), 1000L * 60 * 60 * 24 * 14);

        // User 엔티티에 리프레시 토큰 저장
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        // 리프레시 토큰을 HttpOnly 쿠키로 생성
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(secureCookie) // HTTPS면 true로 바꾸기
                .path("/")
                .maxAge(60 * 60 * 24 * 14) // 14일
                .build();

        // 쿠키를 Response에 추가
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        // UserResponse에 액세스 토큰 포함해서 반환
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setProfileImageUrl(user.getProfileImageUrl());
        userResponse.setToken(accessToken); // 액세스 토큰

        return userResponse;
    }

    // 리프레시 토큰 유효성 검증 메서드
    public boolean isRefreshTokenValid(String username, String refreshToken) {
        return userRepository.findByUsername(username)
                .map(user -> refreshToken.equals(user.getRefreshToken()))
                .orElse(false);
    }

    // 로그아웃 시 리프레시 토큰 삭제
    public void removeRefreshToken(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setRefreshToken(null);
            userRepository.save(user);
        });
    }

    // refreshToken 재발급(리프레시 토큰이 유효할 때)
    public String renewRefreshToken(String username, HttpServletResponse response) {
        String newRefreshToken = jwtProvider.createRefreshToken(username, 1000L * 60 * 60 * 24 * 14);

        userRepository.findByUsername(username).ifPresent(user -> {
            user.setRefreshToken(newRefreshToken);
            userRepository.save(user);
        });

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .secure(secureCookie)
                .path("/")
                .maxAge(60 * 60 * 24 * 14)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return newRefreshToken;
    }

    public void updateProfile(String username, ProfileUploadRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setProfileImageUrl(request.getProfileImageUrl());

        userRepository.save(user);
    }

    public UserResponse getCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }

    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        userRepository.delete(user);
    }

    public void updatePassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new CustomException(USER_NOT_FOUND));

        // 기존 비밀번호 검증
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // 일반 서비스 코드(여행 등록, 수정)에서 유저 조회 필요
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

}
