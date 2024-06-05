package com.jungle.week13.post.dto;


import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    private String title;
    private String content;
    private String link;
    private String category;
    private Integer score;
    private String author;
    private String password;


}
