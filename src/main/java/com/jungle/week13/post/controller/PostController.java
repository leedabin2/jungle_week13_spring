package com.jungle.week13.post.controller;


import com.jungle.week13.common.dto.CommonResponse;
import com.jungle.week13.post.dto.PostRequest;
import com.jungle.week13.post.dto.PostResponse;
import com.jungle.week13.post.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostRequest dto) {

        // 변환된 dto 객체를 서비스 레이어의 메서드에 전달
        CommonResponse<PostResponse> postResponse = postService.createPostAndSave(dto);

        return ResponseEntity.status(200).body(postResponse);
    };




}
