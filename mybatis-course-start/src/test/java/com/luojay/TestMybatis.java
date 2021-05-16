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
    @Test
    public void testInsert() throws IOException {
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
        String sqlId = "com.luojay.dao.StudentDao"+"."+"insertStudent";
        //  7.执行sql语句，通过sqlId找到语句
        int rows = sqlSession.insert(sqlId, new Student(3, "bobi", "bobi@qq.com", 25));
        //  8.输出结果
        System.out.println(rows);
        //  9.mybatis默认不会自动提交事务，所以在insert，update，delete后需要手动提交事务
        sqlSession.commit();
        //  10.关闭连接
        sqlSession.close();
    }
}
