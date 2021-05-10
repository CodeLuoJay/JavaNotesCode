package com.luojay.entity;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author LuoJay
 * @version V1.0.0
 * @date 2021/5/3 23:46
 * 测试lombok的构造器注解
 */
@AllArgsConstructor
@NoArgsConstructor
public class UserWithConstructor {
    private String id;
    private String name;
    private Integer age;
    private Date birthday;
}
