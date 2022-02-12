package com.cokung.comon.response.exception;

public class ResponseMessage {
    public static final String POST_SUCCESS = "게시글 업로드 성공";
    public static final String POST_FAIL = "게시글 업로드 실패";
    public static final String READ_POST = "게시글 조회 성공";
    public static final String NOT_FOUND_BOARD = "게시글을 찾을 수 없습니다.";
    public static final String NOT_FOUND_CATEGORY = "카테고리를 찾을 수 없습니다.";

    public static final String UPDATE_POST = "게시글 수정 성공";
    public static final String DELETE_POST = "게시글 삭제 성공";

    public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러";
    public static final String DB_ERROR = "데이터 베이스 에러";

    //------------------------------------------------------------------

    public static final String JOIN_SUCCESS = "회원가입 성공";
    public static final String JOIN_FAIL = "회원가입 실패";
    public static final String LOGIN_SUCCESS = "로그인 성공";
    public static final String LOGIN_FAIL = "로그인 실패";

}
