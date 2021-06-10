package com.luojay.dao;

import com.luojay.domain.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author LuoJay
 * @version V1.0.0
 * 学生数据访问层接口
 * @date 2021/5/6 23:52
 */
public interface StudentDao {


    /**
     * 通过动态sql查询学生信息
     *
     * @param id   id
     * @param name 名字
     * @param age  年龄
     * @return {@link Student}
     */
    Student findStudentByDynamicSql(@Param("id")Integer id,
                                    @Param("name")String name,
                                    @Param("age")Integer age);


    /**
     * 通过id的集合查询多个学生
     *
     * @param ids id
     * @return {@link Student}
     */
    List<Student> findStudentByIds(@Param("ids")List<Integer> ids);

    /**
     * 不使用trim和where存在多个and条件查询学生
     *
     * @param id id
     * @param name name
     * @param age age
     * @return {@link Student}
     */
    Student findStudentWithoutTrim(@Param("id")Integer id,
                                @Param("name")String name,
                                @Param("age")Integer age);
    /**
     * 使用trim和where存在多个and条件查询学生
     *
     * @param id id
     * @param name name
     * @param age age
     * @return {@link Student}
     */
    Student findStudentWithTrim(@Param("id")Integer id,
                                @Param("name")String name,
                                @Param("age")Integer age);

    /**
     *
     * 通过trim删除多余逗号来插入学生记录
     * @param student {@link Student}
     * @return 影响的行数
     */
    int insertStudentWithTrimDeleteComma(Student student);

    /**
     * 通过Set删除多余逗号来更新学生记录
     * @param student {@link Student}
     * @return 影响的行数
     */
    int updateStudentWithSet(Student student);

    /**
     * 通过trim来实现删除多余逗号来更新学生记录
     * @param student {@link Student}
     * @return 影响的行数
     */
    int updateStudentWithTrim(Student student);

    /**
     * 通过choose when otherwise标签查询学生信息
     * 需求：如果姓名和年龄条件不为空，则从其中选择一个查询
     * 否则默认查询出课程ID为1的学生
     * @param name 姓名
     * @param age   年龄
     * @return  {@link List<Student>}
     */
    List<Student> findStudentWithChooseWhenOtherWise(@Param("name")String name,
                                                     @Param("age")Integer age);

    /**
     * 通过sql标签重用所有列SQL子句查询学生信息
     * @return {@link List<Student>}
     */
    List<Student> findStudentBySQL();
}
