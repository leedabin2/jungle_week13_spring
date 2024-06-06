package com.jungle.week13.post.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    @NotBlank(message = "제목을 입력하세요.")
    private String title;
    private String content;
    private String link;
    private String category;
    private Integer score;
    @NotBlank(message = "작성자명은 필수 입력입니다.")
    private String author;
    @NotBlank(message = "비밀번호는 필수 입력입니다.")
    private String password;


}
