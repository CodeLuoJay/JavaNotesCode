package com.luojay;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author LuoJay
 * @version V1.0.0
 * 对连接数据库的工具类进行抽取封装
 * 抽取目标：保证SqlSessionFactory在整个项目中，有一个就够用了。
 * @date 2021/5/14 20:19
 */
public class MybatisUtils {
    private static SqlSessionFactory sessionFactory = null;

    static{
        String config = "mybatis.xml";
        // 2.读取这个config表示的文件
        InputStream in = null;
        try {
            in = Resources.getResourceAsStream(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //  3.创建sqlSessionFactoryBuilder对象
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        // 4.创建sqlSessionFactory
        sessionFactory = builder.build(in);
    }

    public static SqlSession getSqlSession(){
        //  保证sqlSession唯一
        SqlSession sqlSession = null;
        if(sessionFactory!=null){
            sqlSession = sessionFactory.openSession(false);
        }
        return sqlSession;
    }
}
