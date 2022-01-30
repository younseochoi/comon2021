package com.cokung.comon.domain.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
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
    private Long member_id;

    @Column(length = 10,nullable = false,unique = true)
    private String id;

    @Column(length=100,nullable = false,unique = true)
    private String email;

    @Column(length = 300, nullable = false)
    private String password;

    @Column(length = 15,nullable = false)
    private String name; //이름

    @Column(length = 20, nullable = false)
    private String dept; //학부

    @Column(length = 20, nullable = false)
    private String major; //학과

    @Column(length = 9, nullable = false)
    private String studentId;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private MemberRole role;

    @Column(nullable = false)
    private String birth;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date joinDate; //가입 날짜



}
