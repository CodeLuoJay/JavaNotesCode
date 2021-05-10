package com.luojay.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author LuoJay
 * @version V1.0.0
 * 测试Lombok的@Getter和@Setter的访问权限
 * @date 2021/5/3 20:33
 */
@Getter
public class UserWithAccessLevel {
    @Setter(AccessLevel.PROTECTED)
    private String id;
    @Setter(AccessLevel.PRIVATE)
    private String name;
    @Setter(AccessLevel.PACKAGE)
    private Integer age;
    @Setter(AccessLevel.NONE)
    private Date birthday;
    @Setter
    private Double height;
}
