package com.cokung.comon.controller;

import com.cokung.comon.domain.repository.MemberRepository;
import com.cokung.comon.dto.LoginMemberDto;
import com.cokung.comon.response.exception.DefaultResponse;
import com.cokung.comon.response.exception.ResponseMessage;
import com.cokung.comon.response.exception.StatusCode;
import com.cokung.comon.dto.MemberDto;
import com.cokung.comon.response.success.SuccessCode;
import com.cokung.comon.response.success.SuccessResponse;
import com.cokung.comon.service.MailAuthService;
import com.cokung.comon.service.MemberService;
import com.cokung.comon.util.CookieUtil;
import com.cokung.comon.util.JwtUtil;
import com.cokung.comon.util.RedisUtil;
import com.sun.mail.iap.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class MemberController {

    @Autowired
    private  MemberService memberService;

    @Autowired
    private MailAuthService mailAuthService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    @PostMapping("/confirmation")
    public ResponseEntity confirmIdDuplication(@RequestParam String id) {
        try {
            if(memberService.confirmIdDuplication(id)) {
                return SuccessResponse.toResponseEntity(SuccessCode.USER_CONFIRM_FAIL);
            } else {
                return SuccessResponse.toResponseEntity(SuccessCode.USER_CONFIRM_SUCCESS, id);
            }
        }catch (Exception e){
            return SuccessResponse.toResponseEntity(SuccessCode.USER_CONFIRM_FAIL, "error");
        }
    }
    @ResponseBody
    @PostMapping("/sign-up")
    public ResponseEntity signUpUser(@RequestBody MemberDto memberDto){
        Long userId = -1L;
        try{
            //비밀번호 확인과 아이디, 이메일 중복 검사가 완료됬다는 가정하의 코드
            userId = memberService.signUpUser(memberDto);
            if (userId != -1L) {
                System.out.println("Join succeed");
                return SuccessResponse.toResponseEntity(SuccessCode.USER_CREATED,userId);
            } else {
                System.out.println("Join fail");
                return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.JOIN_FAIL, memberDto), HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.JOIN_FAIL,"error"), HttpStatus.OK);
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity signInUser(@RequestBody LoginMemberDto loginMemberDto, HttpServletRequest req, HttpServletResponse res){
        try{
            final MemberDto memberDto =memberService.signInUser(loginMemberDto.getId(),loginMemberDto.getPassword());
//            System.out.println(loginMemberDto);
//            final String token = jwtUtil.generateAccessToken(memberDto.getId());
//            final String refreshJwt= jwtUtil.generateRefreshToken(memberDto.getId());
//            Cookie accessToken = cookieUtil.createCookie(JwtUtil.ACCESS_TOKEN_NAME,token);
//            Cookie refreshToken = cookieUtil.createCookie(JwtUtil.REFRESH_TOKEN_NAME, refreshJwt);
//            redisUtil.setDataExpire(refreshJwt, memberDto.getId(),JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);
//            res.addCookie(accessToken);
//            res.addCookie(refreshToken);

            Map<String, String> tokens = memberService.generateAccessTokenAndRefreshToken(memberDto);
            System.out.println("Access Token :: "+tokens.get(JwtUtil.ACCESS_TOKEN_NAME));
            System.out.println("Refresh Token :: "+tokens.get(JwtUtil.REFRESH_TOKEN_NAME));
            redisUtil.setDataExpire(tokens.get(JwtUtil.REFRESH_TOKEN_NAME), memberDto.getId(),JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);
            return SuccessResponse.toResponseEntity(SuccessCode.USER_LOGIN_SUCCESS, tokens);
        }catch (Exception e){
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.LOGIN_FAIL, "error"), HttpStatus.OK);
        }
    }

    @PostMapping("/verfiy")
    public Response verify(MemberDto member, HttpServletRequest req, HttpServletResponse res){
       Response response;
        try{
            MemberDto memberDto = memberService.findById(member.getId());
            mailAuthService.sendVerficationMail(memberDto);
            response = new Response("성공적으로 인증메일을 발송했습니다.");
        }catch (Exception e){
            response = new Response("인증메일 발송에 실패했습니다.");
        }
        return response;
    }

    @GetMapping("/verify/{key}")
    public Response getVerify(@PathVariable String key) {
        Response response;
        try {
            mailAuthService.verifyEmail(key);
            response = new Response("메일인증 성공");

        } catch (Exception e) {
            response = new Response("메인인증 실패");
        }
        return response;
    }


}
