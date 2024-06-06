package com.jungle.week13.post.service;


import com.jungle.week13.common.dto.CommonResponse;
import com.jungle.week13.post.dto.PostRequest;
import com.jungle.week13.post.dto.PostResponse;
import com.jungle.week13.post.entity.Post;
import com.jungle.week13.post.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;

@Slf4j
@Service
@AllArgsConstructor
public class PostService {
    // postrequest dto를 post 엔티티로 변환
    // 레포지토리의 save 메서드 호출하여 db에 저장
    // 저장된 post 엔티티를 postresponse dto로 변환
    // 필요한 정보만 json형태로 변환해서 반환 컨트롤러에게

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public CommonResponse<PostResponse> createPostAndSave(PostRequest dto) {

        Post post = Post.builder()
                        .title(dto.getTitle())
                        .author(dto.getAuthor())
                        .content(dto.getContent())
                        .link(dto.getLink())
                        .category(dto.getCategory())
                        .score(dto.getScore())
                        .password(dto.getPassword())

                .build();

        Post savePost = null;
        try {
            savePost = postRepository.save(post); //db에 저장
            log.info("post 작성 저장 성공");


            /* 정적 팩토리 메서드 패턴 */
            PostResponse postResponse = PostResponse.of(savePost);

            return CommonResponse.success(postResponse, "게시글 작성 완료");

        } catch (Exception e) {
            log.error("post 작성 저장 실패 : ",e);
            return CommonResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "게시글 작성 실패");
        }
    };

    public CommonResponse<List<PostResponse>> getAllPost() {

        try {
            // 날짜 순으로 내림차순 정렬
            List<Post> existingPost = postRepository.findAll(Sort.by(Sort.Direction.DESC,"createdAt"));

            // 존재하지 않는다면, 에러 처리하는 부분 추가
            if (existingPost.isEmpty()) {
                return CommonResponse.error(404,"게시글이 존재하지 않습니다.");
            }


            log.info("post 전체 게시글 조회 성공");
            // db에 가져온 목록을 리스트 dto로 변환해서 리턴해줌
            List<PostResponse> responses = existingPost.stream()
                    .map(post -> modelMapper.map(post, PostResponse.class))
                    .collect(Collectors.toList());
            return CommonResponse.success(responses,"게시글 목록 조회 성공");
//            return existingPost.stream()
//                    .map(PostResponse::of)
//                    .collect(Collectors.toList());


        } catch (Exception e) {
            log.error("post 전체 게시글 조회 실패" ,e);
            return CommonResponse.error(500,"게시글 목록 조회 실패");
        }

    };
}
