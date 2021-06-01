package com.luojay.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

/**
 * @author LuoJay
 * @version V1.0.0
 * Student的视图对象
 * @date 2021/6/2 0:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Alias("student_vo")
public class StudentVO {
    private Integer id;
    private String name;
    private String email;
    private Integer age;
}
