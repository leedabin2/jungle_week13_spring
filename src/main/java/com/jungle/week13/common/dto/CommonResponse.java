package com.jungle.week13.common.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {

    private HttpStatus code;
    private String message;
    private T data;
//    private PostData<T> data;


    // 정적 팩토리 메서드
    public static <T> CommonResponse<T> success(T data,String message){
        return CommonResponse.<T>builder()
                .code(HttpStatus.OK) // 일반 성공 응답
                .message(message)
                .data(data)
                .build();
    }

    public static <T> CommonResponse<T> error(HttpStatus code, String message){
        return CommonResponse.<T>builder()
                .code(code)
                .message(message)
                .build();
    }


}
