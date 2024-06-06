package com.jungle.week13.post.controller;


import com.jungle.week13.common.dto.CommonResponse;
import com.jungle.week13.post.dto.PostRequest;
import com.jungle.week13.post.dto.PostResponse;
import com.jungle.week13.post.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostRequest dto) {

        // 변환된 response dto 객체를 서비스 레이어의 메서드에서 받아옴
        CommonResponse<PostResponse> postResponse = postService.createPostAndSave(dto);

        return ResponseEntity.status(HttpStatus.OK).body(postResponse);
    };

    @GetMapping
    public ResponseEntity<CommonResponse<List<PostResponse>>> getAllPost() {

        CommonResponse<List<PostResponse>> responses = postService.getAllPost();

        return ResponseEntity.status(HttpStatus.OK).body(responses);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable("id") final long id) {

        CommonResponse<PostResponse> responses = postService.getPostById(id);

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

}
