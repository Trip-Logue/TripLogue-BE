package com.triploguebe.user.service;

import com.triploguebe.global.exception.CustomException;
import com.triploguebe.global.exception.ErrorCode;
import com.triploguebe.user.dto.SignUpRequest;
import com.triploguebe.user.dto.UserResponse;
import com.triploguebe.user.entity.User;
import com.triploguebe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // UserRepository를 생성자 방식으로 초기화
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse signUp(SignUpRequest request) {
        String username = request.getUsername();
        String email = request.getEmail();

        //DB에 이미 회원이 있는지 검증
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
}
