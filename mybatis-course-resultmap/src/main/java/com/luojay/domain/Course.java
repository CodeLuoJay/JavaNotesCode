package com.luojay.domain;

import java.util.List;

/**
 * @author LuoJay
 * @version V1.0.0
 * 课程表实体类
 * @date 2021/5/18 0:31
 */
public class Course {
    /** 课程id */
    private Integer courseId;
    /** 课程名称 */
    private String courseName;
    /** 学生 */
    private List<Student> student;

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public List<Student> getStudent() {
        return student;
    }

    public void setStudent(List<Student> student) {
        this.student = student;
    }

    public Course() {
    }
    //  测试一对多开启才开这个方法
    // @Override
    // public String toString() {
    //     String Course =  "Course{" +
    //             "courseId=" + courseId +
    //             ", courseName='" + courseName + '\'' + '}';
    //      StringBuilder builder = new StringBuilder(); ;
    //      builder.append("Student{ ");
    //     for (Student student1 : student) {
    //         builder.append(student1.toString());
    //         builder.append(",");
    //     }
    //     builder.append("}");
    //     return Course+builder.toString();
    //
    // }


    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", student=" + student +
                '}';
    }
}
