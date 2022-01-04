//package com.homepageTest.board.domain.entity;
//
//import lombok.*;
//
//import javax.persistence.*;
//
//@Getter
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PROTECTED) //@builder 쓰려면 매개변수가 없는 기본 생성자를 무조건 선언해줘야함. 권한은 protected
//@Table(name="categorys")
//@Data
//@Builder
//public class Category {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY) //기본키 생성을 데이터베이스에게 위임
//    private int category_id;
//
//    @Column(name="category_name",nullable = false,length = 10)
//    private String category_name;
//
//
//}
