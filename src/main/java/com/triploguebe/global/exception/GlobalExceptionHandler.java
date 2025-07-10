package com.triploguebe.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, Object>> handleCustomException(CustomException ex) {
        Map<String, Object> body = new HashMap<>();
        if (ex.getErrorCode() != null) {
            body.put("status", ex.getErrorCode().getStatus());
            body.put("message", ex.getErrorCode().getMessage());
        } else {
            body.put("status", 400);
            body.put("message", "알 수 없는 커스텀 예외입니다.");
        }
        body.put("timestamp", LocalDateTime.now());
        ex.printStackTrace();
        return ResponseEntity.status((int)body.get("status")).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", 400);
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());
        ex.printStackTrace();
        return ResponseEntity.status(400).body(body);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUsernameNotFound(UsernameNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", 404);
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());
        ex.printStackTrace();
        return ResponseEntity.status(404).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", 500);
        body.put("message", "서버 내부 오류입니다.");
        body.put("timestamp", LocalDateTime.now());
        ex.printStackTrace();
        return ResponseEntity.status(500).body(body);
    }
}
