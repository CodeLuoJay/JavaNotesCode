package com.luojay.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author LuoJay
 * @version V1.0.0
 * @date 2021/5/3 19:41
 * 测试lombok使用@Getter注解：
 * 1.在单个字段上使用，只会对该字段生成`getter`方法
 * 2.在类上使用注解。在这种情况下，这个注解对该类中的所有非静态字段起作用
 * 3.无法为 static 设置 get/set 方法，只为 final 类型设置 get 方法（因为lombok的原理）
 *  static是存放在类中的共享变量，final代表不可更改，所以无法设置set方法
 */
@Getter
@Setter
public class UserWithGetter {
    /**
     * 身份证号码（不可变）
     */
    final Long idCard = 441111111111111111L;
    static Double weight;
    private String id;
    private String name;
    private Integer age;
    private Date birthday;
}
