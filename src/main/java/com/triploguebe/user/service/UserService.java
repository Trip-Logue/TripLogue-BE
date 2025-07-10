package com.triploguebe.user.service;

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

}
