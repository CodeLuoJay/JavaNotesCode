# Mybatis高度耦合入门学习一

## 1.引言

实际工作中，用的比较多得Mybatis和JPA两个ORM框架，框架说白就是一个半成品的模板，搭建成一个架构。可以简单理解为框架就是里面的业务代码需要程序员写一下就欧了的好东西。

## 2.什么是Mybatis

### 2.1介绍

MyBatis 是一款优秀的持久层框架，它支持自定义 SQL、存储过程以及高级映射。MyBatis 免除了几乎所有的 JDBC 代码以及设置参数和获取结果集的工作。MyBatis 可以通过简单的 XML 或注解来配置和映射原始类型、接口和 Java POJO（Plain Old Java Objects，普通老式 Java 对象）为数据库中的记录。

摘自官网[mybatis – MyBatis 3 | 简介](https://mybatis.org/mybatis-3/zh/index.html)

### 2.2痛点

在没有学习`Mybatis`之前，我们通常都会在JDBC中操作数据库，需要在代码中注册数据库的驱动来连接数据库，创建`Connection`对象并保存查询结果，创建 `Statement`对象用来执行sql语句，利用`ResultSet`获取记录集等等步骤。

这样的业务代码和数据库的操作混在一起开发模式并不友好，每写一个查询语句就要重复写上述代码，造成代码量比较多，开发效率低。

并且需要关注Connection，Statement，ResultSet对象创建和销毁，对ResultSet查询的结果，需要自己封装为List和Map这些结果集。

伟大又聪明的程序员就会想到把连接数据库代码和执行sql的代码分离出来，并且封装成一个工具类`DBUtils`来进行效率的提升，Mybatis就是一个比较高度简洁的`DBUtils`工具类的封装。

## 3.如何使用Mybatis

### 3.1创建Maven工程

![image-20210510221020166](https://gitee.com/codeluojay/TyproaImage/raw/master/images/image-20210510221020166.png)

### 3.2Mybatis依赖引入

在`Maven`中引入依赖，依赖可以参考[Releases · mybatis/mybatis-3 (github.com)](https://github.com/mybatis/mybatis-3/releases)的版本引入，此外还需要引入`mysql`依赖，为了简化项目引入`lombok`插件依赖，需要`IDEA`安装`lombok`插件使用，有了基础的`mybatis`依赖及剩下的依赖配合，就能搭建一个简单基于`Maven`工程的`mybatis`学习案例。

```xml
    <dependencies>
        <!--junit Test-->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <scope>test</scope>
        </dependency>
        <!--mybatis Orm-->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
        </dependency>
        <!--mysql Driver-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <!--lombok plugins-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
    </dependencies>
```

在项目中我使用到Maven的聚合项目的中版本管理，统一用来依赖的版本，以防止版本冲突，所以会看到每个`<dependency/>`标签都没有对应的版本，是因为我在父工程中统一引入了依赖，如果只想单独搞一个案例，只需要写入对应版本即可，附上父工程的版本。

![image-20210510221737974](https://gitee.com/codeluojay/TyproaImage/raw/master/images/image-20210510221737974.png)

### 3.3创建数据库及实体类

数据库、表、数据的sql语句

```sql
# 建表SQL
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` int(11) NOT NULL DEFAULT 0,
  `name` varchar(80) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `email` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `age` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (0, 'luojay', 'luojay@qq.com', 24);

SET FOREIGN_KEY_CHECKS = 1;
```

实体类`Student.java`

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private Integer id;
    private String name;
    private String email;
    private int age;
}
```

### 3.4创建访问数据库接口和对应的XML文件

访问数据库接口`StudentDao.java`

```java
public interface StudentDao {
    /**
     * 查询所有学生
     * @return
     */
    List<Student> findAllStudents();

    /**
     * 插入学生
     * @param student 学生
     * @return
     */
    int insertStudent(Student student);
}
```

访问数据库接口对应XML文件`StudentDao.xml`

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace：命名空间 StudentDao能找到StudentDao.xml一一对应的映射实例-->
<mapper namespace="com.luojay.dao.StudentDao">
    <!--resultType:表示结果类型 是sql语句执行后得到的ResultSet，遍历这个ResultSet得到java对象的类型，resultType的值是全限定类名-->
    <select id="findAllStudents" resultType="com.luojay.domain.Student">
        select id,name,email,age from Student order by id
    </select>
    <!--参数类型是对象类型，只要对象属性能一一对上就可以直接用了-->
    <insert id="insertStudent" parameterType="com.luojay.domain.Student">
        insert into student values (#{id},#{name},#{email},#{age})
    </insert>
</mapper>
```

### 3.5编写Mybatis连接数据库的配置文件

连接数据库的配置文件`mybatis.xml`

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<!--configuration：mybatis主配置文件信息-->
<configuration>
    <settings>
        <!--设置mybatis日志，输出到控制台-->
        <setting name="logImpl" value="STDOUT_LOGGING"/>
    </settings>
    <!--环境配置：配置数据库 default必须和某个environment的id值一样 告诉mybatis使用哪个数据库的连接信息，也就是访问哪个数据库-->
    <environments default="dev">
        <!--environment：一个数据库信息的配置（环境） id：一个唯一值，自定义，表示环境的名称-->
        <!--用于开发的数据库环境实例-->
        <environment id="dev">
            <!--transactionManager mybatis的事务类型 type：JDBC(表示使用jdbc中的Connection对象的commit,rollback)-->
            <transactionManager type="JDBC"/>
            <!--dataSource type：表示数据源的类型，POOLED表示使用连接池-->
            <dataSource type="POOLED">
                <!--driver url username password 是固定的，不能自定义的-->
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/springdb"/>
                <property name="username" value="root"/>
                <property name="password" value="root"/>
            </dataSource>
        </environment>
    <!--（sql mapper）sql映射文件的位置-->
    <mappers>
        <!--一个mapper标签指定一个文件的位置，从类路径开始的路径信息  类路径：target/classes-->
        <mapper resource="com/luojay/dao/StudentDao.xml"/>
    </mappers>
</configuration>
```

### 3.6创建测试类

测试类主要有两个,一个是`MybatisApp`使用`main`方法测试查询方法，另外一个测试类是使用`junit`单元测试来测试插入方法。

```java
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
```

```java
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

    }
}
```

### 3.7案例总结和源码下载

运行测试类的插入和查询方法，查看控制台日志，会输出相应的语句记录

```bash
==>  Preparing: select id,name,email,age from Student order by id 
==> Parameters: 
<==    Columns: id, name, email, age
<==        Row: 0, luojay, luojay@qq.com, 24
<==        Row: 1, mysqlTest, mysql@qq.com, 23
<==        Row: 2, bobi, bobi@qq.com, 24
<==        Row: 3, bobi, bobi@qq.com, 25
<==      Total: 4
Student(id=0, name=luojay, email=luojay@qq.com, age=24)
Student(id=1, name=mysqlTest, email=mysql@qq.com, age=23)
Student(id=2, name=bobi, email=bobi@qq.com, age=24)
Student(id=3, name=bobi, email=bobi@qq.com, age=25)
```

**案例目录结构图**

![image-20210612123159012](F:\JavaNotesCode\study-guide\Mybatis入门学习一.assets\image-20210612123159012-1623472825357.png)

完成上述步骤，这样就完成高度耦合的Mybatis入门学习案例。在实际开发中，不会存在这样案例代码。

[JavaNotesCode/mybatis-course-dao at master · CodeLuoJay/JavaNotesCode (github.com)](https://github.com/CodeLuoJay/JavaNotesCode/tree/master/mybatis-course-dao)

如果文章对你有帮助，请帮忙点个Start支持我一下，另外如果想持续跟进本教程，可以点击fork将本仓库克隆到你的仓库

