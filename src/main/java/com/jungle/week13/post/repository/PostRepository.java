package com.jungle.week13.post.repository;

import com.jungle.week13.member.entity.Member;
import com.jungle.week13.post.entity.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>{
    Optional<Post> findById(Long id);
    List<Post> findByMember(Member member, Sort updatedAt);
}