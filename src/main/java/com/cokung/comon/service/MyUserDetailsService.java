package com.cokung.comon.service;

import com.cokung.comon.domain.entity.Member;
import com.cokung.comon.domain.repository.MemberRepository;
import com.cokung.comon.dto.MemberDto;
import com.cokung.comon.dto.SecurityMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<Member> member = memberRepository.findById(userId);
        if(member.isPresent()) {
            return new SecurityMember(member.get().toDto());
        }
        throw new UsernameNotFoundException(userId + " : 사용자가 존재하지 않음");
    }
}
