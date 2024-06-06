package com.jungle.week13.post.repository;

import com.jungle.week13.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>{
    Optional<Post> findById(Long id);

}