package com.cokung.comon.controller;

import com.cokung.comon.dto.LoginMemberDto;
import com.cokung.comon.dto.MemberDto;
import com.cokung.comon.dto.RequestVerifyEmail;
import com.cokung.comon.dto.VerificationMemberDto;
import com.cokung.comon.response.success.SuccessCode;
import com.cokung.comon.response.success.SuccessResponse;
import com.cokung.comon.service.AuthService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/users")
@Slf4j
public class MemberController {
    private final AuthService authService;

    @Autowired
    public MemberController(AuthService authService) {
        this.authService = authService;
    }

    @ApiOperation(value = "아이디 중복 확인", notes = "해당 아이디의 중복을 확인합니다.")
    @PostMapping("confirmation")
    public ResponseEntity confirmIdDuplication(@RequestBody VerificationMemberDto verificationMemberDto) {
        log.info(verificationMemberDto.toString());
        if(authService.confirmIdDuplication(verificationMemberDto.getId())) {
            return SuccessResponse.toResponseEntity(SuccessCode.USER_CONFIRM_FAIL);
        } else {
            return SuccessResponse.toResponseEntity(SuccessCode.USER_CONFIRM_SUCCESS, verificationMemberDto.getId());
        }
    }

    @ApiOperation(value = "회원가입", notes = "회원가입 진행")
    @PostMapping("sign-up")
    public ResponseEntity signUpUser(@RequestBody MemberDto memberDto) {
        return SuccessResponse.toResponseEntity(SuccessCode.USER_CREATED, authService.signUpUser(memberDto));
    }

    @ApiOperation(value = "로그인", notes = "로그인 진행")
    @PostMapping("sign-in")
    public ResponseEntity signInUser(@RequestBody LoginMemberDto loginMemberDto) {
        log.info(loginMemberDto.toString());
        final MemberDto memberDto = authService.signInUser(loginMemberDto.getId(), loginMemberDto.getPassword());
        Map<String, String> tokens = authService.generateAccessTokenAndRefreshToken(memberDto);
        return SuccessResponse.toResponseEntity(SuccessCode.USER_LOGIN_SUCCESS, tokens);
    }

    @ApiOperation(value = "인증 메일 보내기", notes = "유저가 이메일 인증을 원하면 요청합니다.")
    @PostMapping("verification")
    public ResponseEntity verifyUser(@RequestBody RequestVerifyEmail requestVerifyEmail) {
        log.info("requestVerifyEmail: :::  " + requestVerifyEmail.getId());
        authService.sendVerificationMail(requestVerifyEmail.getId());
        return SuccessResponse.toResponseEntity(SuccessCode.SEND_VERIFICATION_MAIL_SUCCESS);
    }

    @ApiOperation(value = "이메일 인증 완료하기", notes = "유저가 인증 링크를 누르면 실행됩니다.")
    @GetMapping("verification/{key}")
    public ResponseEntity getVerify(@PathVariable String key) {
    return SuccessResponse.toResponseEntity(SuccessCode.VERIFICATION_MAIL_SUCCESS, authService.verifyEmail(key));
    }
}
