package com.luojay.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
/**
 * @author CodeLuoJay
 * @date 2021/5/3 17:37
 * @version V1.0.0
 * 测试Lombok使用实体类User
 */
@Data
@Accessors(chain = true)
public class User {
    private String id;
    private String name;
    private Integer age;
    private Date birthday;
}
