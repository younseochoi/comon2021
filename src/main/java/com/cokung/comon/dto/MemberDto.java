package com.cokung.comon.dto;

import com.cokung.comon.domain.entity.Member;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
@Builder
public class MemberDto {
    private Long memberId;
    private String id;
    private String password;
    private String email;
    private String birthDate;
    private LocalDateTime joinDate;
    private String name;
    private UserRole role = UserRole.NP_USER;

    // 대학 정보
    private String college;
    private String department;
    private String major;

    public Member toEntity() {
        return Member.builder()
                .memberId(memberId)
                .id(id)
                .password(password)
                .email(email)
                .birthDate(birthDate)
                .name(name)
                .role(role)
                .college(college)
                .department(department)
                .major(major)
                .build();
    }
}
