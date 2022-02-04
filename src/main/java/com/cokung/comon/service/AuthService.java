package com.cokung.comon.service;

import com.cokung.comon.dto.MemberDto;
import com.cokung.comon.dto.UserRole;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.Map;

public interface AuthService {
    void signUpUser(MemberDto memberDto);
    MemberDto loginUser(String id, String password) throws Exception;
    void sendVerificationMail(MemberDto memberDto, String key) throws ChangeSetPersister.NotFoundException;
    void verifyEmail(String key) throws ChangeSetPersister.NotFoundException;
    void modifyUserRole(MemberDto memberDto, UserRole userRole);
    MemberDto findById(String id);
    Map<String,String> generateAccessTokenAndRefreshToken(MemberDto memberDto);
}
