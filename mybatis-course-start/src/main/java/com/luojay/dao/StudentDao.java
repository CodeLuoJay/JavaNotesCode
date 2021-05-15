package com.luojay.dao;

import com.luojay.domain.Student;

import java.util.List;

/**
 * @author LuoJay
 * @version V1.0.0
 * 学生数据访问层接口
 * @date 2021/5/6 23:52
 */
public interface StudentDao {
    /**
     * 查询所有学生
     * @return
     */
    List<Student> findAllStudents();

    /**
     * 插入学生
     * @param student 学生
     * @return
     */
    int insertStudent(Student student);
}
