package com.jungle.week13.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostDeleteRequest {

        @NotBlank(message = "비밀번호는 필수 입력입니다.")
        private String password;

}
