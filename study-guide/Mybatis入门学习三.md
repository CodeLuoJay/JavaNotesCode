## 引言

前面的入门学习案例中文章一和二中，似乎都给人一种错觉，只需要用到`StudentDao.xml`再加上`Student.java`以及连接数据库的操作配置和工具类，就可以完成整体流程，`StudentDao.java`似乎没有多大用处。

附上学习案例一二链接：

[JavaNotesCode/Mybatis入门学习一.md at master · CodeLuoJay/JavaNotesCode (github.com)](https://github.com/CodeLuoJay/JavaNotesCode/blob/master/study-guide/Mybatis入门学习一.md)

[JavaNotesCode/Mybatis入门学习二.md at master · CodeLuoJay/JavaNotesCode (github.com)](https://github.com/CodeLuoJay/JavaNotesCode/blob/master/study-guide/Mybatis入门学习二.md)

有这样的想法也是对的，因为我们都没有将案例代码再进一步简化。那还有哪些地方存在不足可以简化的呢？

```java
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
        MybatisUtils.closeSqlSession(sqlSession);
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
```

在测试类的代码中，不难发现测试的代码还涉及到业务相关的代码，如**1、2、5步骤等业务相关的代码**，其实在实际开发，测试类只需要做到调用接口，接口返回数据这样才是合理的规范。

那么我们其实需要用到`StudentDao`的实现类，把1、2、5的步骤代码写在实现类中，就可以优化上面的问题，使程序更加规范。

## 1.Mybatis使用传统的dao方式查询数据

传统的dao方式就是写一个dao的实现类,如`StudentDao`接口需要写一个`StudentDaoImpl`,然后实现类每一个方法中实现数据库连接、操作数据库以及会话对象的关闭。

这样就可以实现测试类中调用接口就返回数据，无需关心内容逻辑代码实现，知道思路便可以开整，那么我们可以来实现这样的dao方式查询数据

`StudentDao.java`

```java
// 接口定义操作数据库的方法
public interface StudentDao {
    List<Student> findAllStudents();
    int insertStudent(Student student);
}
```

`StudentDaoImpl.java`

```java
//	接口的每一个方法实现数据库的连接和操作数据库
public class StudentDaoImpl implements StudentDao {
    @Override
    public List<Student> findAllStudents() {
    	//	使用之前的工具类进行数据库的连接
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
        sqlSession.commit();
        sqlSession.close();
        return row;
    }
}
```

`StudentDao.xml`

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.luojay.dao.StudentDao">
    <select id="findAllStudents" resultType="com.luojay.domain.Student">
        select id,name,email,age from Student order by id
    </select>
    <insert id="insertStudent" parameterType="com.luojay.domain.Student">
        insert into student values (#{id},#{name},#{email},#{age})
    </insert>
</mapper>
```

`Student.java`

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
//	使用lombok注解生成get和set
public class Student {
    private Integer id;
    private String name;
    private String email;
    private int age;
}
```

`MybatisUtils.java`

```java
//	之前抽取出来的方便连接数据库的工具类
public class MybatisUtils {
    private static SqlSessionFactory sessionFactory;
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
```

`TestMybatis`

```java
public class TestMybatis {
    @Test
    public void testInsert() {
        //	对应测试类就简化很多
        StudentDaoImpl studentDao = new StudentDaoImpl();
        int row = studentDao.insertStudent(new Student(5, "test", "test@qq.com", 26));
        System.out.println(row);
    }
    @Test
    public void testSelect() {
        //	测试类只关心接口调用有无数据返回
        StudentDaoImpl studentDao = new StudentDaoImpl();
        List<Student> rows = studentDao.findAllStudents();
        rows.forEach(System.out::println);
    }
}

```

完整的目录截图，其中配置文件的内容略过没有写出来，完整的可以查看源码或者查看之前的入门案例[JavaNotesCode/Mybatis入门学习一.md at master · CodeLuoJay/JavaNotesCode (github.com)](https://github.com/CodeLuoJay/JavaNotesCode/blob/master/study-guide/Mybatis入门学习一.md)。

![image-20210516165350284](https://gitee.com/codeluojay/TyproaImage/raw/master/images/image-20210516165350284.png)

这样子，我们对第一个例子又进行一个简化，相当于第二次简化

## 2.Mybatis使用传统的dao方式查询数据的原理

### 2.1整体的执行流程：

`StudentDao` -> `StudentDaoImpl`->`StudentDao.xml`

1. StudentDao定义操作数据库规范，如定义查询所有的学生的接口规范
2. StudentDaoImpl定义操作数据库行为，如连接数据库、查询数据
3. StudentDao.xml定义具体操作数据库的数据sql语句

### 2.2具体的执行流程步骤：

![image-20210515111911979](https://gitee.com/codeluojay/TyproaImage/raw/master/images/image-20210515111911979.png)

### 2.3存在的不足：

1. sqlId是写死的，这是硬编码的弟弟行为，如果需要改动则又要改动方法里面的代码

2. sqlSession还需要手动指定sqlId跟它对应的方法，如果匹配不上也会报错，如`sqlId`是`insert into xxx`，那么我指定查询方法为`selectList`也会报错，编译器检查不出来

   所以基于以上两点，我们不得不又去思考和找寻规律和方法去优化这些不足。

从测试类的方法调用分析问题并找到规律去优化案例

![image-20210515125624320](https://gitee.com/codeluojay/TyproaImage/raw/master/images/image-20210515125624320.png)

## 3.Mybatis使用动态代理的方式查询数据

### 3.1动态代理案例

有了上述的分析之后，我们就可以使用Mybatis的动态代理的方式去优化之前的案例，只需要在测试类中改变获取Dao接口的方式，将它改变成使用动态代理的方式获取，这里用到的`sqlSession.getMapper(Class class)`

```java
public class TestMybatis {
    /**
     * 测试使用动态方式查询数据
     */
    @Test
    public void testSelect() {
        // 使用动态代理的方式获取studentDao对象 
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
        // 验证是否通过JDK动态代理 --> 输出打印是com.sun.proxy.$Proxy4
        System.out.println(studentDao.getClass().getName());
        List<Student> rows = studentDao.findAllStudents();
        rows.forEach(System.out::println);
    }

}
```

`sqlSession.getMapper(StudentDao.class)`内部使用Java的反射方式，去获取方法名、参数、返回值等等信息，然后执行对应的SQL语句。

### 3.2总结和源码下载

这样就可以把传统的`StudentDaoImpl`干掉，不用写实现类，也不用我们自己手动提交事务，关闭`SqlSesssion`连接对象，整一个目录结构也清爽疑点。

![image-20210516171151331](https://gitee.com/codeluojay/TyproaImage/raw/master/images/image-20210516171151331.png)

我们可以通过`studentDao.getClass().getName()`来验证是否是使用动态代理的方式，结果打印出来的结果是`com.sun.proxy.$Proxy4`,证明它是使用sun公司的proxy方式，这种方式就是JDK动态代理，使用动态代理的方式也是企业中开发主流的开发方式。

这是一个两个案例结合的一篇文章，所以有两处源码，对应着上面两种不同方式。

[JavaNotesCode/mybatis-course-dao at master · CodeLuoJay/JavaNotesCode (github.com)](https://github.com/CodeLuoJay/JavaNotesCode/tree/master/mybatis-course-dao)

[JavaNotesCode/mybatis-course-proxy at master · CodeLuoJay/JavaNotesCode (github.com)](https://github.com/CodeLuoJay/JavaNotesCode/tree/master/mybatis-course-proxy)