package com.cokung.comon.dto;

import org.apache.tomcat.jni.User;
import org.springframework.security.core.authority.AuthorityUtils;

public class SecurityMember extends User {
    private static final long serialVerionUid = 1L;

//    public SecurityMember(MemberDto memberDto){
//        super(memberDto.getId(),"{noop}"+memberDto.getPassword(), AuthorityUtils.createAuthorityList(memberDto.getRole().toString()));
//    }
}
