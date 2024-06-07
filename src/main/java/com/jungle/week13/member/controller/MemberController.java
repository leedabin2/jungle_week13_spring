package com.jungle.week13.member.controller;


import com.jungle.week13.common.dto.CommonResponse;
import com.jungle.week13.exception.DuplicateMemberException;
import com.jungle.week13.member.dto.AuthRequest;
import com.jungle.week13.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid AuthRequest addMemberDto, BindingResult bindingResult) throws MethodArgumentNotValidException {

        if (bindingResult.hasErrors()) //유효성 검증 실패 시 에러 메시지 클라이언트에게 전달
            throw new MethodArgumentNotValidException(null,bindingResult);

        try {
            CommonResponse<String> registerResponse = memberService.signup(addMemberDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse); // 201 Created
        } catch (DuplicateMemberException e) {
            // 아이디 중복 예외 처리
            return ResponseEntity.badRequest().body(CommonResponse.error(HttpStatus.CONFLICT, e.getMessage()));
        }
    }
}
