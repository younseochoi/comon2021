package com.cokung.comon.controller;

import com.cokung.comon.dto.LoginMemberDto;
import com.cokung.comon.dto.MemberDto;
import com.cokung.comon.dto.RequestVerifyEmail;
import com.cokung.comon.dto.VerificationMemberDto;
import com.cokung.comon.response.success.SuccessCode;
import com.cokung.comon.response.success.SuccessResponse;
import com.cokung.comon.service.AuthService;
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

    @PostMapping("confirmation")
    public ResponseEntity confirmIdDuplication(@RequestBody VerificationMemberDto verificationMemberDto) {
        log.info(verificationMemberDto.toString());
        if(authService.confirmIdDuplication(verificationMemberDto.getId())) {
            return SuccessResponse.toResponseEntity(SuccessCode.USER_CONFIRM_FAIL);
        } else {
            return SuccessResponse.toResponseEntity(SuccessCode.USER_CONFIRM_SUCCESS, verificationMemberDto.getId());
        }
    }


    @PostMapping("sign-up")
    public ResponseEntity signUpUser(@RequestBody MemberDto memberDto) {
        return SuccessResponse.toResponseEntity(SuccessCode.USER_CREATED, authService.signUpUser(memberDto));
    }


    @PostMapping("sign-in")
    public ResponseEntity signInUser(@RequestBody LoginMemberDto loginMemberDto) {
        log.info(loginMemberDto.toString());
        final MemberDto memberDto = authService.signInUser(loginMemberDto.getId(), loginMemberDto.getPassword());
        Map<String, String> tokens = authService.generateAccessTokenAndRefreshToken(memberDto);
        return SuccessResponse.toResponseEntity(SuccessCode.USER_LOGIN_SUCCESS, tokens);
    }


    @PostMapping("verification")
    public ResponseEntity verifyUser(@RequestBody RequestVerifyEmail requestVerifyEmail) {
        log.info("requestVerifyEmail: :::  " + requestVerifyEmail.getId());
        authService.sendVerificationMail(requestVerifyEmail.getId());
        return SuccessResponse.toResponseEntity(SuccessCode.SEND_VERIFICATION_MAIL_SUCCESS);
    }


    @GetMapping("verification/{key}")
    public ResponseEntity getVerify(@PathVariable String key) {
    return SuccessResponse.toResponseEntity(SuccessCode.VERIFICATION_MAIL_SUCCESS, authService.verifyEmail(key));
    }
}
