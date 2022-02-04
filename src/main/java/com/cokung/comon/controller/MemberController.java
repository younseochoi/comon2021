package com.cokung.comon.controller;

import com.cokung.comon.dto.LoginMemberDto;
import com.cokung.comon.dto.MemberDto;
import com.cokung.comon.dto.RequestVerifyEmail;
import com.cokung.comon.response.exception.DefaultResponse;
import com.cokung.comon.response.exception.ResponseMessage;
import com.cokung.comon.response.exception.StatusCode;
import com.cokung.comon.response.success.SuccessCode;
import com.cokung.comon.response.success.SuccessResponse;
import com.cokung.comon.service.AuthService;
import com.cokung.comon.utils.CookieUtil;
import com.cokung.comon.utils.JwtUtil;
import com.cokung.comon.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/user")
@Slf4j
public class MemberController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;

    @Autowired
    public MemberController(AuthService authService, JwtUtil jwtUtil, CookieUtil cookieUtil, RedisUtil redisUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
        this.redisUtil = redisUtil;
    }


    @GetMapping
    public String hello() {
        return "hello";
    }


    @GetMapping("signUp")
    public String signUpUser() {
        return "signUp";
    }


    @PostMapping("/signUp")
    public ResponseEntity signUpUser(MemberDto memberDto) throws Exception {
        System.out.println(memberDto.toString());
        authService.signUpUser(memberDto);
        return SuccessResponse.toResponseEntity(SuccessCode.CREATED_USER, memberDto);
    }


    @GetMapping("/login")
    public String login() {
        System.out.println("login page");
        return "login";
    }


    @PostMapping("/login")
    public ResponseEntity login(LoginMemberDto loginMemberDto, HttpServletRequest req, HttpServletResponse res) throws Exception {
        final MemberDto memberDto = authService.loginUser(loginMemberDto.getId(), loginMemberDto.getPassword());
        Map<String, String> tokens = authService.generateAccessTokenAndRefreshToken(memberDto);
        return SuccessResponse.toResponseEntity(SuccessCode.LOGIN_SUCCESS, tokens);
    }


    @GetMapping("/verify")
    public String verify(HttpServletRequest req) {
        Cookie[] cookieList = req.getCookies();
        for(Cookie cookie: cookieList) {
            System.out.println(cookie.getValue());
        }
        return "verify";
    }


    @PostMapping("/verify")
    public ResponseEntity verify(RequestVerifyEmail requestVerifyEmail, HttpServletRequest req, HttpServletResponse res) {
        try {
            Cookie[] cookieList = req.getCookies();
            String key = cookieUtil.getCookie(req, JwtUtil.REFRESH_TOKEN_NAME).getValue();
            System.out.println("controller: requestverifyemail: " + requestVerifyEmail.getId());
            MemberDto memberDto = authService.findById(requestVerifyEmail.getId());
            System.out.println(memberDto.toString());
            authService.sendVerificationMail(memberDto, key);
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_POST), HttpStatus.OK);
        }
        catch (Exception e) {
            System.out.println("error");
            return new ResponseEntity(DefaultResponse.res(StatusCode.NOT_FOUND, ResponseMessage.READ_POST), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/verify/{key}")
    public ResponseEntity getVerify(@PathVariable String key) {
        try {
            authService.verifyEmail(key);
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_POST), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.NOT_FOUND, ResponseMessage.READ_POST), HttpStatus.NOT_FOUND);
        }
    }
}
