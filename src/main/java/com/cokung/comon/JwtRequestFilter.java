package com.cokung.comon;

import com.cokung.comon.dto.MemberDto;
import com.cokung.comon.service.AuthService;
import com.cokung.comon.service.MyUserDetailsService;
import com.cokung.comon.utils.CookieUtil;
import com.cokung.comon.utils.JwtUtil;
import com.cokung.comon.utils.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private AuthService authService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CookieUtil cookieUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //access token
        final Cookie jwtToken = cookieUtil.getCookie(request,JwtUtil.ACCESS_TOKEN_NAME);

        String userId = null;
        String jwt =null;
        String refreshJwt = null;
        String refreshUname = null;

        try{
            if(jwtToken != null){ //access token 있으면
                jwt = jwtToken.getValue(); //access token 값 받아오기
                userId = jwtUtil.getUserId(jwt); //payload 속 userId 받아옴
                System.out.println("여기는 jwtRequestFilter "+userId);
            }
            if(userId!=null){
                MemberDto memberDto = authService.findById(userId);
                UserDetails userDetails = myUserDetailsService.loadUserByUsername(memberDto.getId());
                if(jwtUtil.isValidAccessToken(jwt, userDetails)){ //유효한 token 이면
                    jwtUtil.setSecurityAuthentication(request, userDetails);
                }
            }
        }catch (Exception e){ //유효하지 않으면 refresh token 설정
            Cookie refreshToken = cookieUtil.getCookie(request,JwtUtil.REFRESH_TOKEN_NAME);
            if(refreshToken!=null){
                refreshJwt = refreshToken.getValue();
            }
        }

        try{
            if(refreshJwt != null){
                refreshUname = redisUtil.getData(refreshJwt); //refresh token으로 redis의 userId가져옴
                System.out.println("여기는 JWTFilter 2번째 try-catch "+refreshUname);

                if(refreshUname.equals(jwtUtil.getUserId(refreshJwt))){
                    UserDetails userDetails = myUserDetailsService.loadUserByUsername(refreshUname);
                    System.out.println("if문 안 "+userDetails);
                    jwtUtil.setSecurityAuthentication(request, userDetails);

                    MemberDto memberDto = new MemberDto();
                    memberDto.setId(refreshUname);
                    String newToken =jwtUtil.generateAccessToken(memberDto.getId()); //새로운 access token 발금
                    System.out.println("newToken"+newToken);
                    Cookie newAccessToken = cookieUtil.createCookie(JwtUtil.ACCESS_TOKEN_NAME,newToken);
                    response.addCookie(newAccessToken);
                }
            }
        }catch(ExpiredJwtException e){

        }

        filterChain.doFilter(request,response);
    }
}