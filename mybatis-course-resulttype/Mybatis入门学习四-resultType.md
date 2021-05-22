# Mybatis入门学习三

## 引言

经过前面的学习，大致把Mybatis的执行流程过了一遍，接下来就是要学习如何通过传递参数给SQL语句来进行操作数据库的数据，以及操作数据库的结果如何封装成Java对象。

## 1.Mybatis接收单个传入参数`parameterType`

简单类型：Mybatis把Java的基本数据类型（包含包装类）和String都叫简单类型。

### 1.1`parameterType`设置传递参数的类型

比如在`StudentDao.java`定义一个根据学生ID查询数据的接口`findStudentById`

```java
public interface StudentDao {
    /**
     * 简单类型的参数传递
     * 根据学生ID查询数据
     * @param id id
     * @return {@link Student}
     */
    Student findStudentById(Integer id);
}
```

`StudentDao.xml`编写对应`mapper`的SQL语句时，可以使用`parameterType`进行对参数的类型

```xml
    <select id="findStudentById" parameterType="java.lang.Integer" resultType="com.luojay.domain.Student">
        select id,name,email,age from Student where id = #{StudentId}
    </select>
```

> 需要注意:
>
> 如果只要一个参数传递，接口方法参数和mapper文件的参数可以不一致
>
> 例如上面的接口是Integer id 而条件查询写的是 where id = #{StudentId}

案例学习源码：

[JavaNotesCode/mybatis-course-singleparam at master · CodeLuoJay/JavaNotesCode (github.com)](https://github.com/CodeLuoJay/JavaNotesCode/tree/master/mybatis-course-singleparam)

### 1.2`parameterType`属性的作用

1. 它的作用就是指Java代码中把数据传入到mapper文件的sql语句中，限制传递参数的类型，比如上面的`findStudentById`传递的参数类型是`Integer`,那么我们可以指定`mapper`文件中的`id`为`findStudentById`的参数类型是`java.lang.Integer`，也就是全限定类名。
2. `findStudentById`参数类型是基本数据类型`int`，那么我们需要Mybatis的设定好的别名`int`，那么在执行SQL语句的时候，会找到对应Java的基本数据类型`int`。此外，还可以Java利用自动拆箱和装箱的特性，指定Mybatis的别名为`int`，那么它会找到Java的包装类`Integer`进行组装。
3. parameterType不是强制的，mybatis通过反射机制能够发现接口参数的数类型，所以可以没有，一般在实际开发中也不写。

Mybatis将Java的基本数据类型（包含包装类）都设置了别名，可以在[mybatis – MyBatis 3 | 配置](https://mybatis.org/mybatis-3/zh/configuration.html#typeAliases)的官方文档找到对应的说明，也可以参考下面的截图。

![image-20210516225904284](https://gitee.com/codeluojay/TyproaImage/raw/master/images/image-20210516225904284.png)

### 1.3#{}内部的封装原理

使用#{}之后，mybatis执行sql是使用的jdbc中的Preparedstatement对象由mybatis执行下面的代码：

```java
//	1.Mybatis创建Connection，Preparedstatement对象(省略)
	string sql="select id，name，email，age from student where id=#{studentId}"
	Preparedstatement pst=conn. preparedstatement(sq1); pst. setInt(1,1001);
 //	2.执行sql封装为resultType="com.luojay.domain.student"这个对象
    Resultset rs=ps.executeQuery();student student=nulll while(rs.next()){
		//从数据库取表的一行数据，存到一个java对象属性中
        student student=new Student();student.setId(rs.getInt("id));
        student.setName(rs.getstring("name"));
        student.setEmail(rs.getstring("email"));
        student.setAge(rs.getInt("age"));
    }
	return student;
```

### 1.4#和$区别

1. `#`使用？在sql语句中做站位的，使用Preparedstatement执行sql，效率高
2. `#`能够避免sql注入，更安全。
3. `$`不使用占位符，是字符串连接方式，使用statement对象执行sql，效率低
4. `$`有sql注入的风险，缺乏安全性。
5. `$`可以替换表名或者列名

## 2.Mybatis传递多个传入参数，使用`param`命名参数

多个参数传递则需要使用Mybatis的注解`@Param`并设置名字进行传递

比如在`StudentDao.java`定义一个根据学生ID查询数据的接口`selectMulitParam`

```java
	//	定义多个接口参数
	List<Student> selectMulitParam(@Param("myname")String name, 
                                   @Param("myage")Integer age);
```

`StudentDao.xml`编写对应`mapper`的SQL语句时，`#{参数名}`对应`@Param("参数名")`

```xml
<select id="selectMulitParam">
	select * from student where name=#{myname} or age=#{myage}
</select>
```

案例源码学习：

[JavaNotesCode/mybatis-course-mulitparam at master · CodeLuoJay/JavaNotesCode (github.com)](https://github.com/CodeLuoJay/JavaNotesCode/tree/master/mybatis-course-mulitparam)

## 3.Mybatis返回数据封装输出结果`resultType`

Mybatis执行了sql语句，得到数据如何封装成Java对象？实际上，是通过两个参数来指定要封装的Java对象。

### 3.1使用resultType类型来封装指定的Java对象

`resultType`是结果类型意思，这是Mybatis内部封装的一个对象用来将sql语句执行完毕后的数据转为的对应的Java对象，如下面的mapper文件的语句

```java
    <select id="findStudent" parameterType="java.lang.Integer" resultType="com.luojay.domain.Student">
        select id,name,email,age from Student 
    </select>
```

### 3.2`resultType`内部执行原理

1. mybatis执行sql语句，得到数据结果集
2. 然后mybatis查找`resultType`中对应的类
3. 调用对应类的无参数构造方法，创建对象。
4. mybatis把Resultset指定列值赋值给同名的属性。（图中的黄色和蓝色的列名和属性名意义对应）

![image-20210519010216182](https://gitee.com/codeluojay/TyproaImage/raw/master/images/image-20210519010216182.png)

大致的过程跟以前我们写的JDBC的代码类似，等价于以下的代码

```java
 public void findStudentByJDBC() throws Exception {
        // 打开连接(简写dbUrl,dbUser,dbPwd，实际开发中不是这样写的)
        Connection conn = DriverManager.getConnection("dbUrl","dbUser","dbPwd");
        // 创建执行对象
        Statement stmt = conn.createStatement();
        // 执行sql语句（mybatis封装结果对象等价于从这里开始的代码）
        ResultSet rs= stmt.executeQuery("select id,name,email,age from student");
        while(rs.next()){
            Student student = new Student();
            student.setId(rs.getInt("id"));
            student.setName(rs.getString("name"));
        }
    }
```

### 3.3使用`resultType`类型来封装成`Map`类型

一般是实际开发中，除了用对应的实体类比如`Student`等，之外我们还可以使用Map集合来存储数据。Mybatis也提供封装成Map使用方式，我们只需要在`resultType`中指定封装结果的类型为`java.util.HashMap`或者`hashmap`。

下面就是对应的学习案例

`StudentDao.java`

```java
 	/**
     * 查询学生并封装结果为Map类型
     *
     * @param id id
     * @return {@link Map<String,Object>}
     */
    Map<String,Object> findStudentByMap(@Param("id") Integer id);
```

`StudentDao.xml`

```xml
    <select id="findStudentByMap" resultType="java.util.HashMap">
        select s.id, s.name,s.age, s.email from student s where id = #{id}
    </select>
```

封装成Map的原理如下

1. 将数据库的列名映射成HashMap的key，也就是键值对中的键
2. 将列名对应的数据结果映射成value，也就是键值对中的值

封装成Map的原理图如下

![image-20210522142756040](https://gitee.com/codeluojay/TyproaImage/raw/master/images/image-20210522142756040.png)

### 3.4使用`resultType`类型来封装成基本数据类型

比如实际工作会需要统计数目的需求，这时候返回的结果就是数量，一般是`Integer`或者`int`类型，这时候可以用`resultType`指定。

`StudentDao.java`

```java
    /**
     * 统计学生数量
     *
     * @return int
     */
    int countStudent();
```

`StudentDao.xml`

```xml
    <select id="countStudent" resultType="int">
        select count(id) from student s
    </select>
```

### 3.5使用`resultType`封装成`List<Object>`类型

上面所有的resultType都是封装成单个对象，实际工作中还需要将多个对象数据存放进`List`这样的集合中，这些在日常工作中也会经常用到。

测试案例如下

`StudentDao.java`

```java
    /**
     * 查询学生并封装成List<Student>类型
     *
     * @return {@link List<Student>}
     */
    List<Student> findStudents();
```

`StudentDao.xml`

```xml
    <select id="findStudents" resultType="com.luojay.domain.Student">
        select s.id, s.name,s.age, s.email from student s
    </select>
```

封装成`List<Object>`的原理：

1. 和之前封装成单个`Student findStudentById()`方法类似，JVM运用到JDK动态代理在运行时查找到`StudentDao.java`中`List<Student> findStudents`方法
2. 利用反射得到方法的返回类型是`List<Student>`，会将结果封装成`Student`类型，然后再添加进`List`集合，最终封装成`List<Student>`
3. 因此我们在写对应的mapper方法时，只要指定`resultType`为封装的结果类型，然后动态代理和反射会将对象类型添加进`List`集合中。

### 3.6使用`resultType`封装成`List<Map<String,Object>>`类型

实际开发中，可能是懒得写实体类，也会经常用到`Map<String,Object>`这样的对象来存储数据，将数据库表的列名映射成`String`，列名对应的值映射成`Object`类型，这样就比较容易输出一个规范的JSON数据给前端。

```json
//	json数据格式
[
  {
    "id": 0,
    "name": "luojay",
    "email": "luojay@qq.com",
    "age": 24,
  }
 ]
```

`StudentDao.java`

```java
    /**
     * 查询学生并封装成List<Student>类型
     *
     * @return {@link List<Student>}
     */
    List<Map<String,Object>> findStudentMaps();
```

`StudentDao.xml`

```xml
    <select id="findStudentMaps" resultType="hashmap">
        select s.id, s.name,s.age, s.email from student s
    </select>
```

相关将Java对象转换成的JSON数组对象的测试方法，使用到hutool封装好的工具类，没有用过hutool的可以参考一下hutool的官网的[JSON工具-JSONUtil (hutool.cn)](https://www.hutool.cn/docs/#/json/JSONUtil?id=parsexxx和toxxx)

```java
    @Test
    public void findStudentMaps(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentDao studentDao= sqlSession.getMapper(StudentDao.class);
        //	封装成List<Map<String,Object>>
        List<Map<String,Object>> students = studentDao.findStudentMaps();
        students.forEach(System.out::println);
        //	转换成JSON数组对象
        JSONArray objects = JSONUtil.parseArray(students);
        objects.forEach(System.out::println);
    }
```

关于Mybatis的传递参数给对应SQL语句执行和SQL语句执行结果返回封装成Java对象的教程就写到这里，几乎把日常开发中常用到的东西都总结了一下，希望自己在日常开发过程中有遗忘的时候，再回来翻这篇笔记，同时也希望能帮助到别人，更快学习Mybatis的传参和结果封装。

案例学习源码：

[JavaNotesCode/mybatis-course-resulttype at master · CodeLuoJay/JavaNotesCode (github.com)](https://github.com/CodeLuoJay/JavaNotesCode/tree/master/mybatis-course-resulttype)

