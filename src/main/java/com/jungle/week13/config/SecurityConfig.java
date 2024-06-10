package com.jungle.week13.config;

import com.jungle.week13.jwt.JwtValidator;
import com.jungle.week13.jwt.JwtValidationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtValidator jwtValidator;

    public SecurityConfig(JwtValidator jwtValidator) {
        this.jwtValidator = jwtValidator;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/post").authenticated() // POST,GET /api/post는 인증 필요
                        .requestMatchers(HttpMethod.GET, "/api/post").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/post/{id}").authenticated() // PUT, DELETE, GET /api/post/{id}는 인증 필요
                        .requestMatchers(HttpMethod.DELETE,"/api/post/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET,"/api/post/{id}").authenticated()
                        .anyRequest().permitAll() // 나머지 요청은 인증 없이 허용
                )
                    .formLogin(formLogin -> formLogin.disable()) // 폼 로그인 비활성화 (JWT 토큰 인증 사용 예정)
                    .addFilterBefore(jwtValidationFilter(), AnonymousAuthenticationFilter.class)
                    .csrf(AbstractHttpConfigurer::disable) // CSRF 공격 방어 비활성화 - 추후 httponly cookie 쓰려면 활성화
                    .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080","http://localhost:3000")); // 허용할 도메인
//        configuration.addAllowedOrigin("*");
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드
        configuration.setAllowedHeaders(List.of("*"));
//        configuration.addAllowedHeader("*");
//        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Access-Control-Allow-Headers", "Authorization, x-xsrf-token, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, " +
                "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private JwtValidationFilter jwtValidationFilter() {
        return new JwtValidationFilter(jwtValidator);
    }
}

