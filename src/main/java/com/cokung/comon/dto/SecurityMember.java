package com.cokung.comon.dto;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class SecurityMember extends User {
    private static final long serialVerionUid = 1L;

    public SecurityMember(MemberDto memberDto) {
        super(memberDto.getId(), "{noop}" + memberDto.getPassword(), AuthorityUtils.createAuthorityList(memberDto.getRole().toString()));
    }
}
