package com.cokung.comon.service;

import com.cokung.comon.config.WebSecurityConfig;
import com.cokung.comon.domain.entity.Member;
import com.cokung.comon.domain.repository.MemberRepository;
import com.cokung.comon.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class MemberService {

    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    private WebSecurityConfig webSecurityConfig = new WebSecurityConfig();

    @Autowired
    private PasswordEncoder encoder = webSecurityConfig.getPasswordEncoder();


    @Transactional
    public Long join(MemberDto memberDto){
        Long id = -1L;
        try{
            /*받아온 userPassword로 BcryptPasswrodEncoder를 통해서
             * 암호화한 hassPassword생성 후 db에 저장*/
            String hashPassword = encoder.encode(memberDto.getPassword());
            memberDto.setPassword(hashPassword);
            id = memberRepository.save(memberDto.UserEntity()).getMember_id();
            return id;
        }catch (Exception e){
            return -1L;
        }
    }

    @Transactional
    public MemberDto login(String id, String password){
        try{
            Member member = memberRepository.findById(id);

            if(member == null) throw new Exception("존재하지않는 멤버입니다.");

            MemberDto memberDto = new MemberDto(member);

            String hashPassword= memberDto.getPassword();

            if(encoder.matches(password,hashPassword.toString())){
                System.out.println(":: Login Success ::");
                return memberDto;
            }else{
                System.out.println("비밀번호 일치하지 않습니다.");
                throw new Exception("비밀번호가 일치하지 않습니다.");
            }

        }catch (Exception e){
            return null;
        }

    }

    @Transactional
    public MemberDto findById(String id){
        try{
            Member member = memberRepository.findById(id);
            MemberDto memberDto = new MemberDto(member);
            if(memberDto != null){
                return memberDto;
            }else {
                return null;
            }

        }catch (Exception e){
            throw new RuntimeException("없는 멤버일 가능성있음");
        }
    }
}
