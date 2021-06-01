package com.luojay;

import com.luojay.dao.StudentDao;
import com.luojay.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;


/**
 * @author LuoJay
 * @version V1.0.0
 * 测试mybatis的测试类
 * @date 2021/5/7 1:35
 */
public class TestMybatis {
    @Test
    public void testFindAllStudents() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
        System.out.println(studentDao.findAllStudents());
    }

    @Test
    public void testSelectStudentByAlias() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
        System.out.println(studentDao.selectStudentByAlias());
    }

    @Test
    public void testSelectStudentBySingleAlias() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
        System.out.println(studentDao.selectStudentBySingleAlias());
    }

}
