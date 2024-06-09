package com.jungle.week13.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Component
@RequiredArgsConstructor
public class JwtValidator {

    private final JwtUtil jwtUtil;

    /* token 의 Bearer 문자열을 제거하고 유효성 검증을 하는 메서드 */
    public boolean validateAccessToken(String token) {
        token = token.replace("Bearer" , ""); // "Bearer 문자열 제거"
        return JwtUtil.validateToken(token);
    }

    public ResponseEntity<?> checkTokenValidity(String token) {
        boolean isValid = validateAccessToken(token);
        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 access token");
        }
        return null;
    }

    public ResponseEntity validateRequestToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return checkTokenValidity(token);
    }

    /* 헤더에서 토큰 추출 */
    public String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /* 토큰에서 사용자 정보 가져오기 */
    public Claims getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtUtil.getSecretKey()).parseClaimsJws(token).getBody();
    }
}
