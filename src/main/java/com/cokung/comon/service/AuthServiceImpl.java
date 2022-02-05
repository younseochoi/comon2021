package com.cokung.comon.service;

import com.cokung.comon.domain.repository.MemberRepository;
import com.cokung.comon.dto.MemberDto;
import com.cokung.comon.dto.UserRole;
import com.cokung.comon.response.exception.CustomException;
import com.cokung.comon.response.exception.ErrorCode;
import com.cokung.comon.utils.JwtUtil;
import com.cokung.comon.utils.RedisUtil;
import io.lettuce.core.RedisCommandExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService{
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;


    @Value("${spring.mail.link}")
    private String VERIFICATION_LINK;

    @Autowired
    public AuthServiceImpl(MemberRepository memberRepository, PasswordEncoder passwordEncoder, RedisUtil redisUtil, EmailService emailService, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.redisUtil = redisUtil;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
    }


    @Override
    public String signUpUser(MemberDto memberDto) {
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        return memberRepository.save(memberDto.toEntity()).toDto().getId();
    }


    @Override
    public MemberDto signInUser(String id, String password) {
        MemberDto memberDto = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)).toDto();
        if(!passwordEncoder.matches(password, memberDto.getPassword())) throw new CustomException(ErrorCode.MISMATCH_PASSWORD);
        return memberDto;
    }


    @Override
    public void sendVerificationMail(String requestId){
        String verificationToken = jwtUtil.generateVerificationToken(requestId);
        MemberDto memberDto = memberRepository.findById(requestId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)).toDto();
        redisUtil.setDataExpire(verificationToken, requestId, 60L);
        emailService.sendMail(memberDto.getEmail(), "회원가입 인증 메일입니다. \n", VERIFICATION_LINK + verificationToken);
    }


    @Override
    public String verifyEmail(String verificationToken) {
        String targetMemberId = redisUtil.getData(verificationToken);
        if(targetMemberId == null) throw new CustomException(ErrorCode.VERIFICATION_EMAIL_NOT_FOUND);
        return modifyUserRole(targetMemberId, UserRole.USER);
    }


    @Override
    @Transactional
    public String modifyUserRole(String memberId, UserRole userRole) {
        MemberDto memberDto = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)).toDto();
        memberDto.setRole(userRole);
        return memberRepository.save(memberDto.toEntity()).toDto().getId();
    }


    @Override
    public MemberDto findById(String id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND))
                .toDto();
    }


    @Override
    public Map<String, String> generateAccessTokenAndRefreshToken(MemberDto memberDto) {
        Map<String, String> tokens = new HashMap<>();
        tokens.put(JwtUtil.ACCESS_TOKEN_NAME, jwtUtil.generateAccessToken(memberDto.getId()));
        tokens.put(JwtUtil.REFRESH_TOKEN_NAME, jwtUtil.generateRefreshToken(memberDto.getId()));
        return tokens;
    }


    @Override
    public boolean confirmIdDuplication(String id) {
        return memberRepository.findById(id).isPresent();
    }
}
