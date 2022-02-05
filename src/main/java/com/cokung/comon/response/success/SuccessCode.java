package com.cokung.comon.response.success;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    // 200 OK
    USER_LOGIN_SUCCESS(HttpStatus.OK, "로그인이 정상적으로 처리 되었습니다."),
    BOARD_FOUND_SUCCESS(HttpStatus.OK, "해당 게시글을 정상적으로 찾았습니다."),
    BOARD_UPDATE_SUCCESS(HttpStatus.OK, "해당 게시글을 정상적으로 수정했습니다."),
    BOARD_DELETE_SUCCESS(HttpStatus.OK, "해당 게시글을 정상적으로 삭제했습니다."),
    USER_CONFIRM_SUCCESS(HttpStatus.OK, "중복 아이디가 없습니다."),
    USER_CONFIRM_FAIL(HttpStatus.OK, "중복 아이디가 있습니다."),
    SEND_VERIFICATION_MAIL_SUCCESS(HttpStatus.OK, "인증 메일을 보냈습니다."),
    VERIFICATION_MAIL_SUCCESS(HttpStatus.OK, "메일이 정상적으로 인증 되었습니다."),

    // 201 CREATED
    USER_CREATED(HttpStatus.CREATED, "회원가입 성공"),
    BOARD_CREATED(HttpStatus.CREATED, "게시글 생성 성공"),

    // 204 NO_CONTENT
    BOARD_NO_CONTENT(HttpStatus.NO_CONTENT, "요청은 정상이지만, 컨텐츠가 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String detail;
}
