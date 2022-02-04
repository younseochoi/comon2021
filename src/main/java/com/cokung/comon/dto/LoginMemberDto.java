package com.cokung.comon.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class LoginMemberDto {
    private String id;
    private String password;
}
