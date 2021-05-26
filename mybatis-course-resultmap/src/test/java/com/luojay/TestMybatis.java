package com.luojay;

import com.luojay.dao.CourseDao;
import com.luojay.dao.StudentDao;
import com.luojay.domain.Course;
import com.luojay.domain.Student;
import com.luojay.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.sql.*;


/**
 * @author LuoJay
 * @version V1.0.0
 * 测试mybatis的测试类
 * @date 2021/5/7 1:35
 */
public class TestMybatis {
    @Test
    public void testFindStudentById() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
        System.out.println(studentDao.findStudentById(1));
    }
    @Test
    public void findStudentWithCourse() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
        System.out.println(studentDao.findStudentWithCourse(1));
    }

    @Test
    public void findStudentWithAssociate() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
        System.out.println(studentDao.findStudentWithAssociate(1));
    }
    @Test
    public void findCourseWithStudentCollection() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        CourseDao courseDao = sqlSession.getMapper(CourseDao.class);
        Course courseWithStudent = courseDao.findCourseWithStudent(1);
        System.out.println(courseWithStudent.toString());
    }
    
    @Test
    public void findStudentByJDBC() throws Exception {
        // 打开连接(简写dbUrl,dbUser,dbPwd)
        Connection conn = DriverManager.getConnection("dbUrl","dbUser","dbPwd");
        // 创建执行对象
        Statement stmt = conn.createStatement();
        // 执行sql语句
        ResultSet rs= stmt.executeQuery("select id,name,email,age from student");
        while(rs.next()){
            Student student = new Student();
            student.setId(rs.getInt("id"));
            student.setName(rs.getString("name"));
        }
    }
    @Test
    public void findStudentWithAlias(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
        System.out.println(studentDao.findStudentWithAlias(1));
    }
}
