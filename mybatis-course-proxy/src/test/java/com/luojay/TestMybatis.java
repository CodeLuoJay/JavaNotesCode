package com.luojay;

import com.luojay.dao.StudentDao;
import com.luojay.domain.Student;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

/**
 * @author LuoJay
 * @version V1.0.0
 * 测试mybatis的测试类
 * @date 2021/5/7 1:35
 */
public class TestMybatis {
    /**
     * 测试使用动态方式插入数据
     */
    @Test
    public void testInsert() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
        int row = studentDao.insertStudent(new Student(5, "test", "test@qq.com", 26));
        System.out.println(row);
    }

    /**
     * 测试使用动态方式查询数据
     */
    @Test
    public void testSelect() {
        // 使用动态代理的方式获取studentDao对象 --> sqlSession.getMapper(Class class) 参数：类的字节码文件
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
        // 验证是否通过JDK动态代理 --> 输出打印是com.sun.proxy.$Proxy4
        System.out.println(studentDao.getClass().getName());
        List<Student> rows = studentDao.findAllStudents();
        rows.forEach(System.out::println);
    }

}
