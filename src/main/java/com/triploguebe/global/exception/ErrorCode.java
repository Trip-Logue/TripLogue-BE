package com.triploguebe.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다."),
    UNAUTHORIZED(401, "인증되지 않았습니다."),
    FORBIDDEN(403, "권한이 없습니다."),
    BAD_REQUEST(400, "잘못된 요청입니다."),
    // 회원가입 관련
    DUPLICATE_USERNAME(400, "이미 존재하는 사용자 이름입니다."),
    DUPLICATE_EMAIL(400, "이미 존재하는 이메일입니다."),
    // 비밀번호
    INVALID_PASSWORD(400, "기존 비밀번호가 일치하지 않습니다."),
    // 여행기록
    INVALID_LOCATION(400, "위치 정보가 올바르지 않습니다."),
    TRIP_NOT_FOUND(404, "해당 여행 기록이 존재하지 않습니다."),
    //친구 요청 관련
    ALREADY_FRIENDS(400, "이미 요청을 보냈거나 친구입니다."),
    FRIENDSHIP_NOT_FOUND(404, "친구 요청을 찾을 수 없습니다."),
    CANNOT_ADD_SELF(400, "자기 자신은 친구로 추가할 수 없습니다.");


    private final int status;
    private final String message;
}
