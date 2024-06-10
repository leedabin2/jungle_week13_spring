package com.jungle.week13.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtValidationFilter extends OncePerRequestFilter {

    private final JwtValidator jwtValidator;
    private final List<String> pathsToAuth = Arrays.asList("POST /api/post","PUT /api/post/{id}", "DELETE /api/post/{id}", "GET /api/post", "GET /api/post/{id}");
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtValidationFilter(JwtValidator jwtValidator) {
        this.jwtValidator = jwtValidator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws SecurityException, IOException, ServletException {
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();
        String requestPathWithMethod = requestMethod + " " + requestURI; // HTTP 메서드와 경로를 결합

        // 리스트에 포함된 엔드포인트에 대한 요청만 토큰 검증
        boolean match = pathsToAuth.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern,requestPathWithMethod));
        if (match) {
            ResponseEntity responseEntity = jwtValidator.validateRequestToken(request);
            if (responseEntity != null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"권한이 없는 토큰입니다.");
                return;
            } else {
                String token = jwtValidator.extractToken(request);
                Claims claims = jwtValidator.getClaimsFromToken(token);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(),null,new ArrayList<>()
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request,response);
    }

}
