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
}
