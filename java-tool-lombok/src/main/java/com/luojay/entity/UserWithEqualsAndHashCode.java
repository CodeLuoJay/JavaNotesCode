package com.luojay.entity;

import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author LuoJay
 * @version V1.0.0
 * 测试Lombok的EqualsAndHashCode
 * @date 2021/5/3 23:01
 */
@EqualsAndHashCode
public class UserWithEqualsAndHashCode {
    private String id;
    private String name;
    private Integer age;
    private Date birthday;
}
