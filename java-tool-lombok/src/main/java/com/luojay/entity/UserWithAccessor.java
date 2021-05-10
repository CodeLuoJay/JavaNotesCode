package com.luojay.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author LuoJay
 * @version V1.0.0
 * @date 2021/5/4 0:06
 * 测试lombok的开启@Accessors调用
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserWithAccessor {
    private String id;
    private String name;
    private Integer age;
    private Date birthday;
}
