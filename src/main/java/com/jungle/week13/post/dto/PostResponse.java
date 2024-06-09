package com.jungle.week13.post.dto;


import com.jungle.week13.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String link;
    private String category;
    private Integer score;
    private String author;
//    private String password;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    /* 정적 팩토리 메서드 */
    public static PostResponse of(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .link(post.getLink())
                .category(post.getCategory())
                .score(post.getScore())
                .author(post.getAuthor())
                .created_at(post.getCreatedAt())
                .updated_at(post.getUpdatedAt())
                .build();

    }
}
