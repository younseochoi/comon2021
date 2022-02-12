package com.cokung.comon.service;

import com.cokung.comon.domain.entity.Member;
import com.cokung.comon.domain.entity.MemberRole;
import com.cokung.comon.domain.repository.MemberRepository;
import com.cokung.comon.dto.MemberDto;
import com.cokung.comon.response.exception.CustomException;
import com.cokung.comon.response.exception.ErrorCode;
import com.cokung.comon.util.EmailServicelmpl;
import com.cokung.comon.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.crossstore.ChangeSetPersister;

import javax.swing.text.ChangedCharSetException;
import java.util.UUID;

@Configuration
public class MailAuthService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private EmailServicelmpl emailService;

    @Autowired
    private MemberRepository memberRepository;

    public void sendVerficationMail(MemberDto memberDto){
        try{
            String VERIFICATION_LINK = "http://localhost:8080/user/verify/";
            if(memberDto == null ) throw new ChangeSetPersister.NotFoundException();
            UUID uuid = UUID.randomUUID();
            redisUtil.setDataExpire(uuid.toString(),memberDto.getId(),60*30L);
            emailService.sendMail(memberDto.getEmail(),"회원가입 인증메일입니다.",VERIFICATION_LINK+uuid.toString());
        }catch (Exception e){

        }
    }


    public void verifyEmail(String key){
        try{
            System.out.println("여기는 mailAuthService "+key);
            String userId =redisUtil.getData(key); //여기 알 수 없는 공백이 한가득 들어가서 trim()으로 공백제거
            System.out.println(userId.trim());
            MemberDto memberDto = memberRepository.findById(userId.trim())
                            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)).toDto();

            if(memberDto == null) throw new ChangeSetPersister.NotFoundException();
            modifyMemberRole(memberDto, MemberRole.ROLE_USER);
            redisUtil.deleteData(key);
        }catch (Exception e){

        }
    }

    public void modifyMemberRole(MemberDto memberDto, MemberRole memberRole){
        memberDto.setRole(memberRole);
        System.out.println(memberDto);
        memberRepository.save(memberDto.UserEntity());
    }
}
