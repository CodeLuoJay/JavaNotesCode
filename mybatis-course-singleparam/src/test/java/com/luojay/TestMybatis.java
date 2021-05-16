package com.luojay;

import com.luojay.dao.StudentDao;
import com.luojay.domain.Student;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author LuoJay
 * @version V1.0.0
 * 测试mybatis的测试类
 * @date 2021/5/7 1:35
 */
public class TestMybatis {
    @Test
    public void testFindStudentById() throws IOException {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
        System.out.println(studentDao.findStudentById(1));
    }
}
