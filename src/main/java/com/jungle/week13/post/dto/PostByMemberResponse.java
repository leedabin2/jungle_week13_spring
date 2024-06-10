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
public class PostByMemberResponse {
    private Long id;
    private String title;
    private String source;
    private Boolean is_success;
    private Boolean is_review;
    private String link;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /* 정적 팩토리 메서드 */
    public static PostByMemberResponse of(Post post) {
        return PostByMemberResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .link(post.getLink())
                .source(post.getSource())
                .is_success(post.getIs_success())
                .is_review(post.getIs_review())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();

    }
}
