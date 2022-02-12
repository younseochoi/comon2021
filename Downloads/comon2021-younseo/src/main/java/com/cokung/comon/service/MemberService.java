package com.cokung.comon.service;

import com.cokung.comon.config.WebSecurityConfig;
import com.cokung.comon.domain.entity.Member;
import com.cokung.comon.domain.repository.MemberRepository;
import com.cokung.comon.dto.MemberDto;
import com.cokung.comon.response.exception.CustomException;
import com.cokung.comon.response.exception.ErrorCode;
import com.cokung.comon.util.JwtUtil;
import com.cokung.comon.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
        private final PasswordEncoder passwordEncoder;
        private final RedisUtil redisUtil;
        private final MailAuthService mailAuthService;
        private final JwtUtil jwtUtil;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, RedisUtil redisUtil, MailAuthService mailAuthService, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.redisUtil = redisUtil;
        this.mailAuthService = mailAuthService;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public Long signUpUser(MemberDto memberDto){
        Long id = -1L;
        try{
            /*받아온 userPassword로 BcryptPasswrodEncoder를 통해서
             * 암호화한 hassPassword생성 후 db에 저장*/
            String hashPassword = passwordEncoder.encode(memberDto.getPassword());
            memberDto.setPassword(hashPassword);
            id = memberRepository.save(memberDto.UserEntity()).getMemberId();
            return id;
        }catch (Exception e){
            return -1L;
        }
    }

    @Transactional
    public MemberDto signInUser(String id, String password){
        try{
            MemberDto memberDto = memberRepository.findById(id)
                    .orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND)).toDto();

            String hashPassword= memberDto.getPassword();
            if(!passwordEncoder.matches(password,hashPassword.toString()))throw new CustomException(ErrorCode.MISMATCH_PASSWORD);
            System.out.println(memberDto);
            return memberDto;

        }catch (Exception e){
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }

    }

    @Transactional
    public MemberDto findById(String id){
        try{
            return memberRepository.findById(id)
                    .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)).toDto();
        }catch (Exception e){
            throw new RuntimeException("error");
        }
    }


    public Map<String, String> generateAccessTokenAndRefreshToken(MemberDto memberDto) {
        Map<String, String> tokens = new HashMap<>();
        tokens.put(JwtUtil.ACCESS_TOKEN_NAME, jwtUtil.generateAccessToken(memberDto.getId()));
        tokens.put(JwtUtil.REFRESH_TOKEN_NAME, jwtUtil.generateRefreshToken(memberDto.getId()));
        return tokens;
    }

    public boolean confirmIdDuplication(String id) {
        return memberRepository.findById(id).isPresent();
    }
}
