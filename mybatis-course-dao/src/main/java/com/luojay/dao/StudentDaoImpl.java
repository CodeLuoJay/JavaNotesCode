package com.luojay.dao;

import com.luojay.MybatisUtils;
import com.luojay.domain.Student;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * 使用传统dao方式实现mybatis连接数据库查询数据
 * @author LuoJay
 * @version V1.0.0
 * @date 2021/5/14 22:05
 */
public class StudentDaoImpl implements StudentDao {
    @Override
    public List<Student> findAllStudents() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        String sqlId = "com.luojay.dao.StudentDao.findAllStudents";
        List<Student> student = sqlSession.selectList(sqlId);
        sqlSession.commit();
        sqlSession.close();
        return student;
    }

    @Override
    public int insertStudent(Student student) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        String sqlId = "com.luojay.dao.StudentDao.insertStudent";
        int row = sqlSession.insert(sqlId, new Student(4, "test", "test@qq.com", 25));
        MybatisUtils.closeSqlSession(sqlSession);
        return row;
    }
}
