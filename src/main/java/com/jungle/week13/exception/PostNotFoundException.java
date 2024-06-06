package com.jungle.week13.exception;

public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException(final long id) {
        super("선택된 게시글을 찾을 수 없습니다. ");
    }
}
