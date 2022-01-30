package com.cokung.comon.controller;

import com.cokung.comon.DefaultResponse;
import com.cokung.comon.ResponseMessage;
import com.cokung.comon.StatusCode;
import com.cokung.comon.domain.entity.Member;
import com.cokung.comon.dto.MemberDto;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CookieUtil cookieUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private MailAuthService mailAuthService;


    @ResponseBody
    @PostMapping("/join")
    public ResponseEntity join(MemberDto memberDto, HttpServletRequest req, HttpServletResponse res){
        Long userId = -1L;
        try{
            //프론트 단에서 비밀번호 확인과 아이디, 이메일 중복 검사가 완료됬다는 가정하의 코드
            userId = memberService.join(memberDto);
            System.out.println(memberDto);
            Response response = this.verify(memberDto, req, res);
            if (userId != -1L) {
                System.out.println("Join succeed");
                return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.JOIN_SUCCESS, userId), HttpStatus.OK);
            } else {
                System.out.println("Join fail");
                return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.JOIN_FAIL, memberDto), HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.JOIN_FAIL, memberDto), HttpStatus.OK);
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestParam String id, @RequestParam String password, HttpServletRequest req, HttpServletResponse res){
        System.out.println("아이디 : "+id+" 비번 : "+password);
        try{
            MemberDto memberDto =memberService.login(id, password);
            final String token = jwtUtil.generateToken(memberDto);
            final String refreshJwt= jwtUtil.generateRefreshToken(memberDto);
            Cookie accessToken = cookieUtil.createCookie(JwtUtil.ACCESS_TOKEN_NAME,token);
            Cookie refreshToken = cookieUtil.createCookie(JwtUtil.REFRESH_TOKEN_NAME, refreshJwt);
            redisUtil.setDataExpire(refreshJwt, memberDto.getId(),JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);
            res.addCookie(accessToken);
            res.addCookie(refreshToken);

            if(memberDto != null){
                return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, memberDto), HttpStatus.OK);
            }else{
                return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.LOGIN_FAIL, memberDto), HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.LOGIN_FAIL, null), HttpStatus.OK);
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
