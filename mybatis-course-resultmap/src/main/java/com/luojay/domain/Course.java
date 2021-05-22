package com.luojay.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LuoJay
 * @version V1.0.0
 * 课程表实体类
 * @date 2021/5/18 0:31
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Course {
    /** 课程id */
    private Integer courseId;
    /** 课程名称 */
    private String courseName;
    /** 学生 */
    private Student student;
}
