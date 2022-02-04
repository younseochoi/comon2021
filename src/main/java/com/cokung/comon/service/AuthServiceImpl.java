package com.cokung.comon.service;

import com.cokung.comon.domain.repository.MemberRepository;
import com.cokung.comon.dto.MemberDto;
import com.cokung.comon.dto.UserRole;
import com.cokung.comon.response.exception.CustomException;
import com.cokung.comon.response.exception.ErrorCode;
import com.cokung.comon.utils.JwtUtil;
import com.cokung.comon.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public AuthServiceImpl(MemberRepository memberRepository, PasswordEncoder passwordEncoder, RedisUtil redisUtil, EmailService emailService, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.redisUtil = redisUtil;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void signUpUser(MemberDto memberDto) {
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        memberRepository.save(memberDto.toEntity());
    }

    @Override
    public MemberDto loginUser(String id, String password) {
        MemberDto memberDto = memberRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)).toDto();
        if(!passwordEncoder.matches(password, memberDto.getPassword())) throw new CustomException(ErrorCode.MISMATCH_PASSWORD);
        return memberDto;
    }

    @Override
    public void sendVerificationMail(MemberDto memberDto, String key){
        String VERIFICATION_LINK = "http://localhost:8080/user/verify/" + key;
        if(memberDto == null) throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        UUID uuid = UUID.randomUUID();
        redisUtil.setDataExpire(uuid.toString(), memberDto.getId(), 60 * 30L);
        emailService.sendMail(memberDto.getEmail(), "회원가입 인증 메일입니다.", VERIFICATION_LINK);
    }

    @Override
    public void verifyEmail(String key){
        String memberId = redisUtil.getData(key);
        MemberDto memberDto = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)).toDto();
        modifyUserRole(memberDto, UserRole.USER);
    }

    @Override
    @Transactional
    public void modifyUserRole(MemberDto memberDto, UserRole userRole) {
        memberDto.setRole(userRole);
        memberDto.setMemberId(memberRepository.findById(memberDto.getId()).get().getMemberId());
        System.out.println("업데이트----");
        System.out.println(memberDto.toString());
        memberRepository.save(memberDto.toEntity());
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
}
