package com.jungle.week13.post.service;


import com.jungle.week13.common.dto.CommonResponse;
import com.jungle.week13.post.dto.PostRequest;
import com.jungle.week13.post.dto.PostResponse;
import com.jungle.week13.post.entity.Post;
import com.jungle.week13.post.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class PostService {
    // postrequest dto를 post 엔티티로 변환
    // 레포지토리의 save 메서드 호출하여 db에 저장
    // 저장된 post 엔티티를 postresponse dto로 변환
    // 필요한 정보만 json형태로 변환해서 반환 컨트롤러에게

    private final PostRepository postRepository;

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
}
