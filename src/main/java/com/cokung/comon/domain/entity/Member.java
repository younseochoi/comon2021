package com.cokung.comon.domain.entity;

import com.cokung.comon.dto.MemberDto;
import com.cokung.comon.dto.UserRole;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Entity
@Builder
@EnableJpaAuditing
public class Member {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @NotNull
    private String id;
    @NotNull
    private String password;
    @NotNull
    private String email;
    @NotNull
    private String birthDate;
    @NotNull
    private String name;

    @CreatedDate
    private LocalDateTime joinDate;
    @NotNull
    private UserRole role = UserRole.NP_USER;

    // 대학 정보
    @NotNull
    private String college; // 단과대
    @NotNull
    private String department; // 학부
    @NotNull
    private String major; // 전공

    public MemberDto toDto() {
        return MemberDto.builder()
                .memberId(memberId)
                .id(id)
                .password(password)
                .email(email)
                .birthDate(birthDate)
                .name(name)
                .joinDate(joinDate)
                .role(role)
                .college(college)
                .department(department)
                .major(major)
                .build();
    }
}
