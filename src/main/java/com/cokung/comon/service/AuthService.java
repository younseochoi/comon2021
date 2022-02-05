package com.cokung.comon.service;

import com.cokung.comon.dto.MemberDto;
import com.cokung.comon.dto.UserRole;

import java.util.Map;

public interface AuthService {
    String signUpUser(MemberDto memberDto);
    MemberDto signInUser(String id, String password);
    void sendVerificationMail(String requestId);
    String verifyEmail(String key);
    String modifyUserRole(String memberId, UserRole userRole);
    MemberDto findById(String id);
    Map<String,String> generateAccessTokenAndRefreshToken(MemberDto memberDto);
    boolean confirmIdDuplication(String id);
}
