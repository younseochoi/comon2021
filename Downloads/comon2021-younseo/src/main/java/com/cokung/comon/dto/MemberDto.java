package com.cokung.comon.dto;

import com.cokung.comon.domain.entity.Member;
import com.cokung.comon.domain.entity.MemberRole;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {

    private Long memberId;
    private String id;
    private String password;
    private String email;
    private String name; //닉네임
    private String birthDate;
    private Date joinDate; //가입 날짜

    private String studentId; //학번
    private String college; //단과대
    private String department; //학부
    private String major; //학과
    private MemberRole role=MemberRole.NP_USER;


    //    @Builder
    public Member UserEntity(){
        return Member.builder()
                .memberId(memberId)
                .id(id)
                .email(email)
                .password(password)
                .name(name)
                .college(college)
                .department(department)
                .major(major)
                .studentId(studentId)
                .birthDate(birthDate)
                .role(role)
                .joinDate(joinDate)
                .build();
    }

    @Builder
    public MemberDto(Member member){
        this.memberId=member.getMemberId();
        this.id=member.getId();
        this.email=member.getEmail();
        this.password=member.getPassword();
        this.name=member.getName();
        this.college=member.getCollege();
        this.department=member.getDepartment();
        this.major=member.getMajor();
        this.role=member.getRole();
        this.joinDate=member.getJoinDate();
        this.birthDate=member.getBirthDate();
        this.studentId=member.getStudentId();

    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

}
