package com.cokung.comon.dto;

import com.cokung.comon.domain.entity.Member;
import com.cokung.comon.domain.entity.MemberRole;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import java.util.Collection;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
    private Long member_id;

    private String id;

    private String email;

    private String password;

    private String passwordCheck;

    private String studentId;

    private String name; //이름

    private String dept; //학부

    private String major; //학과
    //
    private MemberRole role=MemberRole.ROLE_NOT_PERMITED;

    private String birth;

    @CreatedDate
    @Column(updatable = false,nullable = true)
    private Date joinDate; //가입 날짜


    //    @Builder
    public Member UserEntity(){
        return Member.builder()
                .member_id(member_id)
                .id(id)
                .email(email)
                .password(password)
                .name(name)
                .dept(dept)
                .major(major)
                .studentId(studentId)
                .birth(birth)
                .role(role)
                .joinDate(joinDate)
                .build();
    }

    @Builder
    public MemberDto(Member member){
        this.member_id=member.getMember_id();
        this.id=member.getId();
        this.email=member.getEmail();
        this.password=member.getPassword();
        this.name=member.getName();
        this.dept=member.getDept();
        this.major=member.getMajor();
        this.role=member.getRole();
        this.joinDate=member.getJoinDate();
        this.birth=member.getBirth();
        this.studentId=member.getStudentId();

    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

}
