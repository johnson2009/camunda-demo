package com.example.camundademo.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupParam {
    // 唯一
    private String id;
    // 组名
    private String name;
    // 类型，用于角色、部门等分类
    private String type;
}
