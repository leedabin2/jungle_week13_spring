package com.jungle.week13.member.service;

import com.jungle.week13.common.dto.CommonResponse;
import com.jungle.week13.exception.DuplicateMemberException;
import com.jungle.week13.member.dto.AuthRequest;
import com.jungle.week13.member.repository.MemberRepository;
import com.jungle.week13.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public CommonResponse<String> signup(AuthRequest addMemberDto) {

        // db에서 아이디 중복 여부 확인
        // 중복이 아니라면 비밀번호를 암호화
        // 중복이라면 중복된 아이디 라는 메시지 반환
        // request dto를 member 엔티티로 변환해서 빌더
        // 엔티티를 다시 response dto로 변환 후 성공메시지, 상태코드 반환

        Optional<Member> existingMember = memberRepository.findByUsername(addMemberDto.getUsername());

        if (existingMember.isPresent()) {
            throw new DuplicateMemberException("이미 존재하는 아이디 입니다.");
        }

        try {
            Member member = Member.builder()
                    .username(addMemberDto.getUsername())
                    .password(passwordEncoder.encode(addMemberDto.getPassword()))
                    .role(Member.MemberRole.USER)
                    .build();

            memberRepository.save(member);
            log.info("회원가입 성공",addMemberDto.getUsername());


            return CommonResponse.success("회원 가입 성공","회원가입을 환영합니다.");
        } catch (DataIntegrityViolationException e) { // 데이터 무결성 검사
            log.error("회원 가입 실패 ",addMemberDto.getUsername(), e.getMessage());
            return CommonResponse.error(HttpStatus.BAD_REQUEST.value(),"회원가입 실패");
        }


    }

}
