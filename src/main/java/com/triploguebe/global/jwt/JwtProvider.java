package com.triploguebe.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProvider {
    private final Key key;

    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    //토큰에서 username 추출
    public String getUsernameFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    //토큰에서 권한 정보 추출
    public List<SimpleGrantedAuthority> getAuthoritiesFromToken(String token) {
        Claims claims = parseClaims(token);
        String auth = (String) claims.get("auth");
        if (auth == null || auth.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(auth.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    //토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명", e);
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰", e);
        } catch (UnsupportedJwtException e) {
            log.info("지원하지 않는 JWT 토큰", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 비어있음", e);
        }
        return false;
    }

    //Claims 추출
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    //토큰 생성 메서드
    public String createToken(String username, List<String> roles, long expireMillis) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", String.join(",", roles));
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expireMillis);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Refresh 토큰 생성
    public String createRefreshToken(String username, long expireMillis) {
        Claims claims = Jwts.claims().setSubject(username);
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expireMillis);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // refreshToken 추출 유틸 (선택)
    public String extractTokenFromCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
