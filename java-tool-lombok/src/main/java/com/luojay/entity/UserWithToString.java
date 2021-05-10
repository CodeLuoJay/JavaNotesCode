package com.luojay.entity;

import lombok.ToString;

import java.util.Date;

/**
 * @author LuoJay
 * @version V1.0.0
 * @date 2021/5/3 21:53
 * 测试lombok的@ToString
 * 1.ToString中of只能用在类上，指定要包含的字段
 * 2.注解内部的注解如Include和Exclude字段可以用在字段上
 * 3.includeFieldNames 参数设置为 true，可以为 toString ()方法的输出添加一些清晰度(但也相当长)
 * 4.通过将 callSuper 设置为 true，可以将 toString 的超类实现的输出包含到输出中。
 * 但要实现 java.lang 中 toString ()的方法如果是默认的实现，对象将几乎是没有意义的，
 * 当您正在扩展另一个类，并且父类实现Object的ToString，同时子类继承父类要输出父类的toString才有意义。
 * 5.@ToString.Include(rank = -1)可以用来设置单个字段打印的顺序，默认的顺序为0
 * 6.@ToString.Include(name = "some other name")
 */
@ToString
public class UserWithToString {
    @ToString.Exclude
    private String id;
    @ToString.Include
    private Integer age;
    @ToString.Include(rank = 1)
    private String name;
    @ToString.Include(name = "生日")
    private Date birthday;
}
