package com.jungle.week13.post.controller;


import com.jungle.week13.common.dto.CommonResponse;
import com.jungle.week13.post.dto.PostByMemberResponse;
import com.jungle.week13.post.dto.PostRequest;
import com.jungle.week13.post.dto.PostResponse;
import com.jungle.week13.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> createPost(@RequestBody @Valid PostRequest dto, HttpServletRequest request) {

        // 변환된 response dto 객체를 서비스 레이어의 메서드에서 받아옴
        CommonResponse<PostResponse> postResponse = postService.createPostAndSave(dto,request);

        return ResponseEntity.status(HttpStatus.CREATED).body(postResponse);
    };

    @GetMapping
    public ResponseEntity<CommonResponse<List<PostByMemberResponse>>> getAllPost(HttpServletRequest request) {

        CommonResponse<List<PostByMemberResponse>> responses = postService.getAllPost(request);

        return ResponseEntity.status(HttpStatus.OK).body(responses);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable("id") final long id, HttpServletRequest request) {

        CommonResponse<PostResponse> responses = postService.getPostById(id,request);

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable("id") final long id, @RequestBody @Valid PostRequest dto, HttpServletRequest request) {

        CommonResponse<PostResponse> responses = postService.updatePost(id, dto, request);

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") final long id, HttpServletRequest request) {
//        postService.deletePost(id,dto);

        CommonResponse<String> deleteResponse = postService.deletePost(id,request);
        return ResponseEntity.status(HttpStatus.OK).body(deleteResponse);
    }

}
