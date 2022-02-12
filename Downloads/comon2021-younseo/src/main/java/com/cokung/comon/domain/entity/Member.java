package com.cokung.comon.domain.entity;

import com.cokung.comon.dto.MemberDto;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@ToString
@Builder
public class Member {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long memberId;

    @Column(length = 10,nullable = false,unique = true)
    private String id;

    @Column(length=100,nullable = false,unique = true)
    private String email;

    @Column(length = 300, nullable = false)
    private String password;

    @Column(length = 15,nullable = false)
    private String name; //이름

    @Column(length = 25,nullable = false)
    private String college; //단과대

    @Column(length = 30, nullable = false)
    private String department; //학부

    @Column(length = 30, nullable = false)
    private String major; //학과

    @Column(length = 9, nullable = false)
    private String studentId;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private MemberRole role;

    @Column(nullable = false)
    private String birthDate;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date joinDate; //가입 날짜

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
                .studentId(studentId)
                .build();
    }


}
