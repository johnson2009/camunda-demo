package com.example.camundademo.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserParam {
    // 账号，唯一
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    // 密码
    private String password;
}
