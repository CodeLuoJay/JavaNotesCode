package com.luojay;

import com.luojay.dao.StudentDaoImpl;
import com.luojay.domain.Student;
import org.junit.Test;

import java.util.List;

/**
 * @author LuoJay
 * @version V1.0.0 测试mybatis的测试类
 * @date 2021/5/7 1:35
 */
public class TestMybatis {
  @Test
  public void testInsert() {
    StudentDaoImpl studentDao = new StudentDaoImpl();
    int row = studentDao.insertStudent(new Student(5, "test", "test@qq.com", 26));
    System.out.println(row);
  }

  /**
   * List<student>studentList = studentDao.findAllStudents()；调用:
   * 1.dao对象，类型是studentDao，全限定称是：com.luojay.dao.studentDao全限定名称和namespace是一样的。
   * 2.方法名称，findAllStudents，这个方法就是mapper文件中的id值findAllStudents 3.通过dao中方法的返回值也可以确定myBatis
   * 要调用的sqlSession的方法如果返回值是List，湖用的是sqlSession.selectList（）方法。
   * 如果返回值int，或是list的，看mapper文件中的标签是<insert>，<update>就会调用sqlSession的insert，update等方法
   * mybatis的动态代理：mybatis根据dao的方法调用，获取执行sql语句的信息。 mybatis根据你的dao接口，创建出一个dao接口的实现类，并创建这个类的对象。
   * 完成sqlSession调用方法，访问数据库。
   */
  @Test
  public void testSelect() {
    // IDEA 中使用快捷键 ctrl+alt+shift+c和ctrl+alt+shift+v 能复制粘贴找到 com.luojay.dao.StudentDao（全限定类名）
    StudentDaoImpl studentDao = new StudentDaoImpl();
    List<Student> rows = studentDao.findAllStudents();
    rows.forEach(System.out::println);
  }
}
