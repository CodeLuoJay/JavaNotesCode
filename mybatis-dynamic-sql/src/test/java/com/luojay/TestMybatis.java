package com.luojay;

import com.luojay.dao.StudentDao;
import com.luojay.domain.Student;
import com.luojay.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author LuoJay
 * @version V1.0.0
 * 测试mybatis的测试类
 * @date 2021/5/7 1:35
 */
public class TestMybatis {


    /**
     * 测试使用动态sql查询数据
     */
    @Test
    public void testDynamicSqlByIf() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
        //  根据学生的id查询
        Student studentId = studentDao.findStudentByDynamicSql(1, null, null);
        System.out.println(studentId);
        //  根据学生的name查询
        Student studentName = studentDao.findStudentByDynamicSql(null, "luojay", null);
        System.out.println(studentName);
        //  根据学生的age查询
        Student studentAge = studentDao.findStudentByDynamicSql(null, null, 24);
        System.out.println(studentAge);
    }
    @Test
    public void testFindStudentByForeach() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
        //  根据学生的id查询
        List<Integer> ids = Arrays.asList(0, 1, 2, 3);
        List<Student> studentList = studentDao.findStudentByIds(ids);
        studentList.forEach(System.out::println);
    }



}
