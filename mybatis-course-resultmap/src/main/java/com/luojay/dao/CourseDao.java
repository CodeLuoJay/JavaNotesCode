package com.luojay.dao;

import com.luojay.domain.Course;

/**
 * @author LuoJay
 * @version V1.0.0
 * @date 2021/5/18 0:33
 * 课程实体类Dao
 */
public interface CourseDao {

    /**
     * 根据ID查询课程
     * @param courseId
     * @return {@link Course}
     */
    Course findCourseById(Integer courseId);

    /**
     * 查询课程并且还查询出该课程下所有学生信息
     *
     * @param courseId 进程id
     * @return {@link Course}
     */
    Course findCourseWithStudent(Integer courseId);
}
