package com.jungle.week13.post.service;


import com.jungle.week13.common.dto.CommonResponse;
import com.jungle.week13.exception.InvaildPasswordException;
import com.jungle.week13.exception.PostNotFoundException;
import com.jungle.week13.post.dto.PostDeleteRequest;
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
import java.util.Objects;
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

    /* 게시글 작성 관련 메서드 */
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
            savePost = postRepository.save(post);

            log.info("post 작성 저장 성공");

            if (savePost == null) {
                throw new RuntimeException("post : db에 저장 실패");
            }


            /* 정적 팩토리 메서드 패턴 */
            PostResponse postResponse = PostResponse.of(savePost);

            return CommonResponse.success(postResponse, "게시글 작성 완료");

        } catch (Exception e) {
            log.error("post 작성 저장 실패 : ",e);
            return CommonResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "게시글 작성 실패");
        }
    };

    /* 전체 게시글을 조회하는 메서드 */
    public CommonResponse<List<PostResponse>> getAllPost() {

        try {
            // 날짜 순으로 내림차순 정렬
            List<Post> existingPost = postRepository.findAll(Sort.by(Sort.Direction.DESC,"updatedAt"));

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

    /* 선택 게시글 조회하는 메서드 */
    public CommonResponse<PostResponse> getPostById(final long id) {
         try {
             Post post = postRepository.findById(id)
                     .orElseThrow(() -> new PostNotFoundException(id));

             log.info("post 선택 게시글 조회 성공");

             // db에 가져온 선택 게시글을 dto로 변환
             /* 정적 팩토리 메서드 패턴 */
             PostResponse postResponse = PostResponse.of(post);

             return CommonResponse.success(postResponse, "선택 게시글 조회 성공");

         } catch (Exception e) {
             log.error("post 선택 게시글 조회 실패");
             return CommonResponse.error(500,"선택 게시글 목록 조회 실패");

         }
    };

    /* 게시글 수정하는 메서드 */
    public CommonResponse<PostResponse> updatePost(final long id, PostRequest dto) {

        // id에 해당하는 게시글 찾아오기
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        // 비밀번호와 일치하는지 확인하기
        if (Objects.equals(post.getPassword(), dto.getPassword())) {
            // dto를 다시 수정된 걸로 post 변경
            Post.builder()
                    .title(dto.getTitle())
                    .author(dto.getAuthor())
                    .content(dto.getContent())
                    .link(dto.getLink())
                    .category(dto.getCategory())
                    .score(dto.getScore())
                    .password(dto.getPassword())
                    .build();

            Post updatePost = postRepository.save(post);

            PostResponse postResponse = PostResponse.of(updatePost);

            return CommonResponse.success(postResponse, "update 게시글 수정 완료");
        }

      return CommonResponse.error(500, "update 게시글 수정 실패");
    };

    /* 게시글을 삭제하는 메서드 */
    public CommonResponse<String> deletePost(final long id, PostDeleteRequest dto) {
        Post post = postRepository.findById(id)
                .orElseThrow( () -> new PostNotFoundException(id));

        if (Objects.equals(post.getPassword(), dto.getPassword())) {
            log.info("delete 게시글 삭제 완료");
            postRepository.delete(post);
            return CommonResponse.success("게시글 삭제 성공","게시글 삭제를 성공했습니다.");
        } else {
            log.error("delete 게시글 삭제 실패");
            throw new InvaildPasswordException("비밀번호가 일치하지 않습니다.");
        }
    }

}
