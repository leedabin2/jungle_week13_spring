package com.jungle.week13.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
@RequiredArgsConstructor
/* 토큰의 생성, 복호화, 검증 */
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final long EXPIRATION_TIME = TimeUnit.SECONDS.toMillis(3600); // 1 hour

    @Value("${custom.jwt.secretKey}")
    private String secretKey;
    private static Key SECRET_KEY;
    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        /* HMAC-SHA256 알고리즘을 사용하여 시크릿키를 복호화 후 key 생성 */
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        SECRET_KEY = Keys.hmacShaKeyFor(bytes);
    }
    public static Key getSecretKey() {
        return SECRET_KEY;
    }


    /* 토큰 생성 */
    public static String createToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username",username);

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiredDate = new Date(nowMillis + EXPIRATION_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SECRET_KEY, signatureAlgorithm)
                .compact();
    }

    /* 토큰 유효성 검사 로직 */
    public static boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
            System.out.println("Token claims: " + claims.toString());
            return true;
        } catch ( SecurityException | MalformedJwtException e) {
            log.info("유효하지 않는 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT token 입니다.");
        }  catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.", e);
        }  catch (IllegalArgumentException e) {
            log.info("잘못된 JWT 토큰 입니다.");
            throw new IllegalArgumentException();
        }
        return false;
    }

    /* 토큰에서 사용자 정보 가져오기 */
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJwt(token).getBody();
    }


}
