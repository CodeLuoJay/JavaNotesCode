package com.luojay;

import com.luojay.domain.Student;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author LuoJay
 * @version V1.0.0
 * 测试类
 * @date 2021/5/7 0:37
 */
public class MyBatisApp {
    public static void main(String[] args) throws IOException {
        // 访问mybatis读取student数据
        //  1.定义mybatis主配置文件的名称，从类路径的根开始(target/classes)
        String config = "mybatis.xml";
        // 2.读取这个config表示的文件
        InputStream in = Resources.getResourceAsStream(config);
        //  3.创建sqlSessionFactoryBuilder对象
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        // 4.创建sqlSessionFactory
        SqlSessionFactory sessionFactory = builder.build(in);
        // 5.获取sqlSession，从sessionFactory中获取
        SqlSession sqlSession = sessionFactory.openSession();
        //  6.指定要指定的sql语句的标识，sql映射文件中的namespace+ "."+标签的id值
        String sqlId = "com.luojay.dao.StudentDao"+"."+"findAllStudents";
        //  7.执行sql语句，通过sqlId找到语句
        List<Student> studentList = sqlSession.selectList(sqlId);
        //  8.输出结果
        studentList.forEach(System.out::println);
    }
}
