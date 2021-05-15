package com.luojay.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author LuoJay
 * @version V1.0.0
 * 学生实体类
 * @date 2021/5/6 23:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private Integer id;
    /** 名字 */
    private String name;
    /** 电子邮件 */
    private String email;
    /** 年龄 */
    private int age;
}
