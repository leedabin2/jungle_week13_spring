package com.jungle.week13.exception;

import com.jungle.week13.member.dto.AuthRequest;

public class MemberNotFoundException extends RuntimeException {
    public  MemberNotFoundException(String message) {
        super(message);
    }
}
