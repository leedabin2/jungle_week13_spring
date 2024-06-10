package com.jungle.week13.post.service;


import com.jungle.week13.common.dto.CommonResponse;
import com.jungle.week13.exception.InvaildPasswordException;
import com.jungle.week13.exception.MemberNotFoundException;
import com.jungle.week13.exception.PostNotFoundException;
import com.jungle.week13.jwt.JwtValidator;
import com.jungle.week13.member.entity.Member;
import com.jungle.week13.member.repository.MemberRepository;
import com.jungle.week13.post.dto.PostByMemberResponse;
import com.jungle.week13.post.dto.PostRequest;
import com.jungle.week13.post.dto.PostResponse;
import com.jungle.week13.post.entity.Post;
import com.jungle.week13.post.repository.PostRepository;
import jakarta.servlet.http.HttpServletRequest;
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

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final JwtValidator jwtValidator;

    /* 게시글 작성 관련 메서드 */
    public CommonResponse<PostResponse> createPostAndSave(PostRequest dto, HttpServletRequest request) {

        // 토큰에서 사용자 정보를 추출
        String username = jwtValidator.getUserNameFromToken(request);

        // member respository 에서 username 가져옴
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException("해당 회원이 없습니다."));

        Post post = Post.builder()
                        .title(dto.getTitle())
                        .content(dto.getContent())
                        .example(dto.getExample())
                        .link(dto.getLink())
                        .source(dto.getSource())
                        .is_success(dto.getIs_success())
                        .is_review(dto.getIs_review())
                        .note(dto.getNote())
                        .code(dto.getCode())
                        .language(dto.getLanguage())
                        .member(member)
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
            return CommonResponse.error(HttpStatus.BAD_REQUEST, "게시글 작성 실패");
        }
    };

    /* 해당 사용자의 게시글을 조회하는 메서드 */
    public CommonResponse<List<PostByMemberResponse>> getAllPost(HttpServletRequest request) {

        try {
            String username = jwtValidator.getUserNameFromToken(request);

            Member member = memberRepository.findByUsername(username)
                    .orElseThrow(() -> new MemberNotFoundException("해당 회원이 없습니다."));

            // 해당 멤버가 있는  날짜 순으로 내림차순 정렬
            List<Post> existingPost = postRepository.findByMember(member, Sort.by(Sort.Direction.DESC,"updatedAt"));

            // 존재하지 않는다면, 에러 처리하는 부분 추가
            if (existingPost.isEmpty()) {
                return CommonResponse.error(HttpStatus.NOT_FOUND,"게시글이 존재하지 않습니다.");
            }

            log.info("post 해당 회원의 전체 게시글 조회 성공");
            // db에 가져온 목록을 리스트 dto로 변환해서 리턴해줌
            List<PostByMemberResponse> responses = existingPost.stream()
                    .map(post -> modelMapper.map(post, PostByMemberResponse.class))
                    .collect(Collectors.toList());
            return CommonResponse.success(responses,"해당 회원의 게시글 목록 조회 성공");


        } catch (Exception e) {
            log.error("post 전체 게시글 조회 실패" ,e);
            return CommonResponse.error(HttpStatus.BAD_REQUEST,"게시글 목록 조회 실패");
        }
    };

    /* 해당 유저의 선택 게시글 조회하는 메서드 */
    public CommonResponse<PostResponse> getPostById(final long id,HttpServletRequest request) {
         try {
             Post post = postRepository.findById(id)
                     .orElseThrow(() -> new PostNotFoundException(id));

             log.info("post 선택 게시글 조회 성공");

             String username = jwtValidator.getUserNameFromToken(request);

            if (Objects.equals(post.getMember().getUsername(),username)) {

                // db에 가져온 선택 게시글을 dto로 변환
                /* 정적 팩토리 메서드 패턴 */
                PostResponse postResponse = PostResponse.of(post);

                return CommonResponse.success(postResponse, "선택 게시글 조회 성공");
            }
            return CommonResponse.error(HttpStatus.BAD_REQUEST,"선택 게시글 목록 조회 실패");

         } catch (Exception e) {
             log.error("post 선택 게시글 조회 실패");
             return CommonResponse.error(HttpStatus.BAD_REQUEST,"선택 게시글 목록 조회 실패");

         }
    };

    /* 게시글 수정하는 메서드 */
    public CommonResponse<PostResponse> updatePost(final long id, PostRequest dto,HttpServletRequest request) {

        // id에 해당하는 게시글 찾아오기
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        // 게시글의 username 을찾아서 토큰에서 추출한 username과 post의 username과 같으면
        String username = jwtValidator.getUserNameFromToken(request);

        if (Objects.equals(post.getMember().getUsername(), username)) {
            // dto를 다시 수정된 걸로 post 변경
            post.update(dto);
            post = postRepository.save(post);

            PostResponse postResponse = PostResponse.of(post);

            return CommonResponse.success(postResponse, "update 게시글 수정 완료");
        }

      return CommonResponse.error(HttpStatus.BAD_REQUEST, "update 게시글 수정 실패");
    };

    /* 게시글을 삭제하는 메서드 */
    public CommonResponse<String> deletePost(final long id, HttpServletRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow( () -> new PostNotFoundException(id));

        String username = jwtValidator.getUserNameFromToken(request);

        if (Objects.equals(post.getMember().getUsername(), username)) {
            log.info("delete 게시글 삭제 완료");
            postRepository.delete(post);
            return CommonResponse.success("게시글 삭제 성공","게시글 삭제를 성공했습니다.");
        } else {
            log.error("delete 게시글 삭제 실패");
            throw new InvaildPasswordException("비밀번호가 일치하지 않습니다.");
        }
    }

}
