package com.triploguebe.user.service;

import com.triploguebe.global.exception.CustomException;
import com.triploguebe.global.exception.ErrorCode;
import com.triploguebe.user.dto.SignUpRequest;
import com.triploguebe.user.dto.UserResponse;
import com.triploguebe.user.entity.User;
import com.triploguebe.user.repository.UserRepository;
import com.triploguebe.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

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

    public UserResponse login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        List<String> roles = Collections.singletonList("USER");
        String jwtToken = jwtProvider.createToken(user.getUsername(), roles, EXPIRE_MILLIS);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setProfileImageUrl(user.getProfileImageUrl());
        userResponse.setToken(jwtToken);

        return userResponse;
    }

    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        userRepository.delete(user);
    }
}
