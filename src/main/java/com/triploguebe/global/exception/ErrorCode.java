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
    BAD_REQUEST(400, "잘못된 요청입니다.");

    private final int status;
    private final String message;
}
