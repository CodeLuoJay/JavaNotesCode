## 引言

上一篇文章[[Mybatis入门学习一]](https://github.com/CodeLuoJay/JavaNotesCode/blob/master/study-guide/Mybatis%25E9%25AB%2598%25E5%25BA%25A6%25E8%2580%25A6%25E5%2590%2588%25E5%2585%25A5%25E9%2597%25A8%25E5%25AD%25A6%25E4%25B9%25A0%25E4%25B8%2580.md)对Mybatis官方的介绍入门案例进行一个初步的编写，本文章就对这个入门案例做一个简单的分析。

## 1.分析入门案例的执行流程和不足

### 1.1执行流程：

1. 创建SqlSessionFactoryBuilder对象，调用build(inputstream)方法读取并解析配置文件，返回SqlSessionFactory对象
2. 由SqlSessionFactory创建SqlSession 对象，没有手动设置的话事务默认开启
   调用SqlSession中的api，传入Statement Id和参数，内部进行复杂的处理，
3. 最后调用jdbc执行SQL语句，封装结果返回。

执行流程的详细说明：

```java
//	Resouroes:mybatis中的封装的一个文件流类，负责读取主配置文件，返回输入流对象。
Inputstream in = Resources.getResourceAsstream("mybatis.xml");

//	sqlsessionFactoryBuilder对象通过`build()`流的方式读取配置文件的内容创建sqlsessionFactory对象
sqlsessionFactoryBuilder builder = new sqlsessionFactoryBuilder();

// 创建SqlSessionFactory对象,重量级对象，程序创建一个对象耗时比较长，使用资源比较多。
//	在整个项目中，有一个就够用了。
sqlsessionFactory factory = builder.build(in);

// 源码中SqlSessionFactory是一个接口，它的接口实现类是DefaultSqlSessionFactory ,作用就是利用//opensession()获取sqlsession对象。
sqlsession sqlsession = factory.opensession();

//	opensession()：默认无参数的，获取是非自动提交事务的sqlsession对象
//	指定要指定的sql语句的标识，sql映射文件中的namespace+ "."+标签的id值
String sqlId = "com.luojay.dao.StudentDao"+"."+"findAllStudents";

// 执行sql语句，通过sqlId找到语句 sqlsession是一个接口：定义了操作数据的方法例如selectList()等
List<Student> studentList = sqlSession.selectList(sqlId);

//  9.关闭sqlSession会话连接,sqlsession对象不是线程安全的，需要在方法内部使用,执行完sql后需要关闭
//	这样能保证他的使用是线程安全的
sqlSession.close();
```

### 1.2不足之处：

1. 每次执行不同的操作数据库方法都要写重复性的连接代码，这里可以考虑用工具类去优化
2. 接口定义的方法似乎可有可无，因为直接是读取mapper.xml中的对应的命名空间+id的方法去执行对应的sql，来实现操作数据库

## 2.入门案例进行简化

### 2.1优化思路

第一个例子可以抽取将读取配置文件，连接数据库，获取`sqlsession`对象步骤简化

1. Resouroes读取mybatis配置文件
2. sqlsessionFactoryBuilder对象通过`build()`流的方式读取配置文件的内容创建sqlsessionFactory对象
3. sqlsesslonFactory通过`opensession()`获取sqlsession对象
4. 可以将提交事务和关闭事务的代码也抽取出来`commit()`和`close()`

从而简化数据库连接和获取操作数据库的语句的对象

### 2.2优化代码

```java
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
    
    public static void closeSqlSession(SqlSession sqlSession){
        if(sqlSession != null){
            sqlSession.commit();
            sqlSession.close();
        }
    }
}
```

### 2.3使用优化代码改写测试类的代码

在之前的案例中调用这个工具类来简化数据库的连接和查询

```java
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
```

### 2.4测试类运行输出结果

测试后也是可以输出对应的结果

```bash
Setting autocommit to false on JDBC Connection [com.mysql.jdbc.JDBC4Connection@206a70ef]
==>  Preparing: select id,name,email,age from Student order by id 
==> Parameters: 
<==    Columns: id, name, email, age
<==        Row: 0, luojay, luojay@qq.com, 24
<==        Row: 1, mysqlTest, mysql@qq.com, 25
<==        Row: 2, bobi, bobi@qq.com, 25
<==        Row: 3, bobi, bobi@qq.com, 25
<==      Total: 4
```

### 2.5案例总结和源码下载

完成上述步骤，这样就完成Mybatis入门学习案例优化，这样才较为贴合在实际开发中代码，实际开发中是用Spring+Mybatis整合。

[JavaNotesCode/mybatis-course-util at master · CodeLuoJay/JavaNotesCode (github.com)](https://github.com/CodeLuoJay/JavaNotesCode/tree/master/mybatis-course-util)

如果文章对你有帮助，请帮忙点个Start支持我一下，另外如果想持续跟进本教程，可以点击fork将本仓库克隆到你的仓库。