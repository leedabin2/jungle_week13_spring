package com.jungle.week13.common.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {

    private int code;
    private String message;
    private T data;


    // 정적 팩토리 메서드
    public static <T> CommonResponse<T> success(T data,String message){
        return CommonResponse.<T>builder()
                .code(0)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> CommonResponse<T> error(int code, String message){
        return CommonResponse.<T>builder()
                .code(code)
                .message(message)
                .build();
    }


}
