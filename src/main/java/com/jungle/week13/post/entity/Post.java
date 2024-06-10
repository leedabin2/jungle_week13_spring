package com.jungle.week13.post.entity;

import com.jungle.week13.member.entity.Member;
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

    @Column(name = "example")
    private String example;

    @Column(name = "link")
    private String link;

    @Column(name = "category")
    private String category;

    @Column(name = "is_success")
    private Boolean is_success;

    @Column(name = "is_review")
    private Boolean is_review;

    @Column(name = "note")
    private String note;

    @Column(name = "code")
    private String code;

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

    @ManyToOne(fetch = FetchType.LAZY) // Member 엔티티와 다대일 관계 설정
    @JoinColumn(name = "member_id") // 외래 키 컬럼 이름 지정 (member 테이블의 id 참조)
    private Member member;

    public void update(PostRequest dto) {
        this.title = dto.getTitle();
        this.author = dto.getAuthor();
        this.content = dto.getContent();
        this.example = dto.getExample();
        this.link = dto.getLink();
        this.category = dto.getCategory();
        this.is_success = dto.getIs_success();
        this.is_review = dto.getIs_review();
        this.note = dto.getNote();
        this.code = dto.getCode();
        this.score = dto.getScore();
        this.password = dto.getPassword();
    }
}
