package com.luojay;

import com.luojay.domain.Student;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.List;

/**
 * @author LuoJay
 * @version V1.0.0
 * 测试类
 * @date 2021/5/7 0:37
 */
public class MyBatisApp {
    public static void main(String[] args) throws IOException {
        // 1.利用工具类从配置文件中获取获取sqlSession
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        //  2.指定要指定的sql语句的标识，sql映射文件中的namespace+ "."+标签的id值
        String sqlId = "com.luojay.dao.StudentDao"+"."+"findAllStudents";
        // 3.执行sql语句，通过sqlId找到语句
        List<Student> studentList = sqlSession.selectList(sqlId);
        //  4.输出结果
        studentList.forEach(System.out::println);
    }
}
