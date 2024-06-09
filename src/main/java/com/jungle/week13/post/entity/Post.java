package com.jungle.week13.post.entity;

import com.jungle.week13.post.dto.PostRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "post")
public class Post{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "link")
    private String link;

    @Column(name = "category")
    private String category;

    @Column(name = "score")
    private Integer score;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "password", nullable = false)
    private String password;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void update(PostRequest dto) {
        this.title = dto.getTitle();
        this.author = dto.getAuthor();
        this.content = dto.getContent();
        this.link = dto.getLink();
        this.category = dto.getCategory();
        this.score = dto.getScore();
        this.password = dto.getPassword();
    }
}
