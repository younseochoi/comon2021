package com.cokung.comon.response.success;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    // 200 OK
    LOGIN_SUCCESS(HttpStatus.OK, "로그인이 정상적으로 처리 되었습니다."),
    BOARD_FOUND(HttpStatus.OK, "해당 게시글을 정상적으로 찾았습니다."),

    // 201 CREATED
    CREATED_USER(HttpStatus.CREATED, "회원가입 성공"),
    CREATED_BOARD(HttpStatus.CREATED, "게시글 생성 성공"),

    // 204 NO_CONTENT
    BOARD_NO_CONTENT(HttpStatus.NO_CONTENT, "요청은 정상이지만, 컨텐츠가 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String detail;
}
