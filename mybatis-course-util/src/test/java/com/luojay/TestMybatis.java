package com.luojay;

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

    /**
     * 测试插入
     *
     */
    @Test
    public void testInsert(){
        // 1.利用工具类从配置文件中获取获取sqlSession
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        // 2.指定要指定的sql语句的标识，sql映射文件中的namespace+ "."+标签的id值
        String sqlId = "com.luojay.dao.StudentDao"+"."+"insertStudent";
        // 3.执行sql语句，通过sqlId找到语句
        int rows = sqlSession.insert(sqlId, new Student(3, "bobi", "bobi@qq.com", 25));
        // 4.输出结果
        System.out.println(rows);
        // 5.提交事务并关闭连接
        sqlSession.commit();
    }

    /**
     * 测试查询
     */
    @Test
    public void TestSelect(){
        // 1.利用工具类从配置文件中获取获取sqlSession
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        // 2.指定要指定的sql语句的标识，sql映射文件中的namespace+ "."+标签的id值
        String sqlId = "com.luojay.dao.StudentDao"+"."+"findAllStudents";
        // 3.执行sql语句，通过sqlId找到语句
        List<Student> studentList = sqlSession.selectList(sqlId);
        // 4.输出结果
        studentList.forEach(System.out::println);
        // 5.提交事务并关闭连接
        MybatisUtils.closeSqlSession(sqlSession);
    }
}
