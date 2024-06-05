package com.jungle.week13.member.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private Integer code; // 상태코드
    private String message; // 회원 가입 성공 메시지 반환
    private DataMessage data; // 추가 메시지
    public static class DataMessage {
        private String message;
    }
}
