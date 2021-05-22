package com.luojay.dao;

import com.luojay.domain.Student;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
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


    /**
     * 一个简单类型的参数：
     * 简单类型：mybatis把java的基本数据类型和string都叫简单类型。
     * 在mapper文件获取简单类型的一个参数的值，使用#{任意字符串}
     * 因为是一个参数，所以可以随便填任意字符
     *
     * @param id id
     * @return {@link Student}
     */
    Student findStudentById(Integer id);

    List<Student> selectMulitParam(@Param("myname")String name,
                                   @Param("myage")Integer age);

}
