package com.triploguebe.global.jwt;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtProvider jwtProvider, UserDetailsService userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        if ((requestURI.equals("/api/users/login") && method.equals("POST")) ||
                (requestURI.equals("/api/users/signup") && method.equals("POST")) ||
                (requestURI.equals("/api/users/refresh") && method.equals("POST"))) {

            System.out.println("JWT 필터 건너뛰기: " + requestURI);
            chain.doFilter(request, response);
            return;
        }

        String token = resolveToken(request);

        try {
            if (token != null && jwtProvider.validateToken(token)) {
                String username = jwtProvider.getUsernameFromToken(token);

                //DB에서 사용자 정보 가져오기
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            chain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "만료된 JWT 토큰입니다.");
        } catch (MalformedJwtException | SecurityException e) {
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "잘못된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "지원하지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰이 비어있거나 잘못되었습니다.");
        } catch (Exception e) {
            setErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");
        }
    }

    //  Authorization 헤더에서 토큰을 먼저 꺼내고, 없으면 쿠키에서 찾음
    private String resolveToken(HttpServletRequest request) {
        // 1. Authorization 헤더에서 "Bearer {token}" 형식으로 꺼내기
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        // 2. Authorization 헤더 없으면 쿠키에서 "jwt" 또는 "refreshToken" 토큰 찾기
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("jwt") || cookie.getName().equals("refreshToken")) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    //에러 발생 시 JSON 형태로 응답
    private void setErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.format("{\"error\": \"%s\"}", message));
    }
}

