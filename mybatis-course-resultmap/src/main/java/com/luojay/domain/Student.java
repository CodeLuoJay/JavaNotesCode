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
public class Student {
    private Integer id;
    /** 名字 */
    private String name;
    /** 邮件 */
    private String email;
    /** 年龄 */
    private int age;
    /** 课程 */
    private Course course;

    public Student() {
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
