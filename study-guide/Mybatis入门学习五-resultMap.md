# Mybatis入门学习五

通过前面的学习，基本已经能用Mybatis完成大部分日常开发的需求，但是还有一些需求，比如说当列名和Java实体类的属性名不一致，还有处理一对多，多对多关系的时候，resultType就可能有点局限了。

所以我们还需要学习Mybatis的resultMap，为了更好地学习resultMap，在之前的学习文章基础之上，需要添加多一张表`course`课程表，跟`student`学生表通过`couseId`列作为关联关系连接起来。

![image-20210525231158946](https://gitee.com/codeluojay/TyproaImage/raw/master/images/image-20210525231158946.png)

## 1.resultMap处理数据库列名和JavaBean属性名不一致问题

之前学习resultType的时候我们是利用mybatis根据JavaBean自动封装查询的数据结果，而resultMap是提供我们自定义封装JavaBean的一个mapper标签。

### 1.1学习案例

resultMap的使用例子

```xml
   <!--resultMap标签，id：resultMap的引用唯一标识 type：对应的javabean的全限定类名-->
	<resultMap id="aliasMap" type="com.luojay.domain.Student">
        <!--封装数据库主键列名规则和JavaBean属性名对应规则-->
        <id column="id" property="id"/>
        <!--封装数据库除主键列名外的列名和JavaBean属性名对应规则-->
        <result column="name" property="name"/>
        <result column="age" property="age"/>
        <result column="email" property="email"/>
        <!--数据库列名为courseId JavaBean属性名为cId的封装规则-->
        <result column="courseId" property="cId"></result>
    </resultMap>
```

`studentDao.java`

```java
    /**
     * 查询学生(列名和javabean不一致情况)
     *
     * @param id id
     * @return {@link Student}
     */
    Student findStudentWithAlias(@Param("id") Integer id);
```

`StudentDao.xml`

```xml
    <!--自定义列名和属性名不不一致的封装规则-->
    <resultMap id="aliasMap" type="com.luojay.domain.Student">
        <id column="id" property="id"/>
        <result column="name" property="name"/>
        <result column="age" property="age"/>
        <result column="email" property="email"/>
        <result column="courseId" property="cId"></result>
    </resultMap>
    <select id="findStudentWithAlias" resultMap="aliasMap">
        select s.id, s.name,s.age, s.email, s.courseId
        from student s where id = ${id}
    </select>
```

日志输出结果

```
Setting autocommit to false on JDBC Connection [com.mysql.jdbc.JDBC4Connection@6fdbe764]
==>  Preparing: select s.id, s.name,s.age, s.email, s.courseId from student s where id = 1 
==> Parameters: 
<==    Columns: id, name, age, email, courseId
<==        Row: 1, mysqlTest, 25, mysql@qq.com, 2
<==      Total: 1
Student{id=1, name='mysqlTest', email='mysql@qq.com', age=25, cId=2, course=null}
```

### 1.2案例总结

resultMap的使用用法：

1. id属性：resultMap的引用唯一标识
2. type属性：对应的javabean的全限定类名
3. id标签：指定主键列的封装规则，id如果定义主键会mybatis底层有优化
4. result标签：指定除了主键列外的列名的封装规则
5. column属性：指定数据库表的列名
6. property属性：指定对应的javaBean属性名

使用resultMap可以很好自定义封装JavaBean规则，这样通常可以解决实际开发中，数据库列名和JavaBean属性名不一致的问题。

resultMap解决列名和属性名不一致的案例

可以看出，`courseId`确实按照我们自定义的结果封装为`cId`

> 另外补充一个处理列名和属性名不一致的方法，可以在写SQL时使用AS关键字，重命名列名和属性名一致

比如下面的SQL直接将`courseId`命名为`cId`,即可以封装跟JavaBean属性一致的名字
```sql
 select s.id, s.name,s.age, s.email, s.courseId as cId from student s where id = ${id}
```

## 2.resultMap处理实体类中有对象的属性

我们可能会遇到这样的需求,查询学生的信息时候需要把课程信息也查询出来，这样就可能需要在学生信息里面添加一个课程类的属性`Course course`才能满足需求，而SQL则需要写关联查询才能完成这个需求。

那resultMap是如何封装这种JavaBean中包含对象类型的属性名规则的呢，通过下面的案例就可以很快学习这种封装规则。

### 2.1学习案例

两个实体类`Student.java`和`Course.java`

```java
//	课程实体类
public class Course {
    /** 课程id */
    private Integer courseId;
    /** 课程名称 */
    private String courseName;
    /** 学生 */
    private Student student;
}
//	学生实体类
public class Student {
    private Integer id;
    /** 名字 */
    private String name;
    /** 邮件 */
    private String email;
    /** 年龄 */
    private int age;
    /** 课程ID */
    private int cId;
    /** 课程 */
    private Course course;
}
```

`StudentDao.java`定义查询出带有课程的方法

```java
    /**
     * 查询带有课程学生课程信息
     *
     * @param id id
     * @return {@link Student}
     */
    Student findStudentWithCourse(@Param("id") Integer id);
```

`StudentDao.xml`定义`resultMap`和查询方法`findStudentWithCourse`

```xml
    <!--resultMap标签，id：resultMap的引用唯一标识 type：对应的javabean的全限定类名-->
    <resultMap id="studentMap" type="com.luojay.domain.Student">
        <!--id标签：主键列名对应的标签 column：数据库的列名 property：javabean对应的属性名-->
        <id column="id" property="id"/>
        <!--result标签：非主键列对应的标签 column：数据库的列名 property：javabean对应的属性名-->
        <result column="name" property="name"/>
        <result column="email" property="email"/>
        <result column="age" property="age"/>
        <result column="courseId" property="course.courseId"/>
        <result column="courseName" property="course.courseName"/>
    </resultMap>
	<!--findStudentWithCourse对应的查询方法-->
    <select id="findStudentWithCourse" resultMap="studentMap">
        select s.id, s.name,s.age, s.email, s.courseId,c.courseName
        from student s
                 left join course c on s.courseId = c.courseId
        where id = ${id}
    </select>
```

### 2.1案例总结

通过上面的xml可以看出，其实就是在`result`中指定列名`courseId`和JavaBean中`courese`属性名匹配，在通过加`.`的方式引用`Course`的`courseId`,从而匹配上`Course`中`courseId`属性进行一一封装。

## 3.resultMap处理一对一关系映射封装

除了上面通过属性名的引用的方式`course.courseId`来指定JavaBean的对象属性外，还可以用到`resultMap`的中`association`标签来处理自定义封装规则，这里比较常见的是用来处理一对一的封装关系。

比如有这样的：查询学生的信息，同时也要查询学生对应的课程信息，并且封装成一个JavaBean对象输出，这时就需要用到`resultMap`的中`association`才能解决这样的需求。

### 3.1学习案例

`StudentDao.java`

```java
    /**
     * 查询带有课程学生课程信息
     *
     * @param id id
     * @return {@link Student}
     */
    Student findStudentWithAssociate(@Param("id") Integer id);
```

`StudentDao.xml`

```xml
    <!--级联映射associate用法（一对一）-->
    <resultMap id="associateMap" type="com.luojay.domain.Student">
        <id column="id" property="id"/>
        <result column="name" property="name"/>
        <result column="age" property="age"/>
        <result column="email" property="email"/>
        <!--association标签：映射对象类型的字段 property：javabean对象类型的属性名 javaType：对象的全限定类名-->
        <association property="course" javaType="com.luojay.domain.Course">
            <!--association标签内部映射对应的数据库列名和javabean属性名-->
            <id column="courseId" property="courseId"/>
            <result column="courseName" property="courseName"/>
        </association>
    </resultMap>

    <!--级联映射associate用法（一对一）-->
    <select id="findStudentWithAssociate" resultMap="associateMap">
        select s.id, s.name,s.age, s.email, s.courseId,c.courseName
        from student s
                 inner join course c on s.courseId = c.courseId
        where id = ${id}
    </select>
```

输出的日志

```markdown
==>  Preparing: select s.id, s.name,s.age, s.email, s.courseId,c.courseName from student s inner join course c on s.courseId = c.courseId where id = 1 
==> Parameters: 
<==    Columns: id, name, age, email, courseId, courseName
<==        Row: 1, mysqlTest, 25, mysql@qq.com, 2, C语言
<==      Total: 1
Student{id=1, name='mysqlTest', email='mysql@qq.com', age=25, cId=0, course=Course{courseId=2, courseName='C语言', student=null}}
```

### 3.2案例总结

案例中使用到`resultMap`中的`association`标签，它的基本用法如下：

1. association标签：映射对象类型的字段
2.  property属性：javabean对象类型的属性名 
3. javaType属性：对象的全限定类名
4. association内部的标签如`id`和`result`这些跟`resultMap`的使用一样。

## 4.resultMap处理一对多关系映射封装

实际开发中，还会遇到这样的需求。比如在查询课程信息时并且还要查询出该课程下所有学生信息，这明显就是课程信息和学生信息形成一对多得关系映射，那么我们就可以用`resultMap`中的`associateMap`

还是通过案例来理解，在`CourseDao.java`定义一个查询课程并且还查询出该课程下所有学生信息的方法

### 4.1学习案例

```java
    /**
     * 查询课程并且还查询出该课程下所有学生信息
     *
     * @param courseId 进程id
     * @return {@link Course}
     */
    Course findCourseWithStudent(Integer courseId);
```

在`CourseDao.xml`中写对应的SQL语句查询方法`findCourseWithStudent`

```xml
<!--级联映射associate用法（一对一）-->
    <resultMap id="associateCourseMap" type="com.luojay.domain.Course">
        <id column="courseId" property="courseId"/>
        <result column="courseName" property="courseName"/>
        <!--	collection标签：映射对象类型的字段 
				property：javabean对象类型的属性名 
				ofType：对象的全限定类名
		-->
        <collection property="student" ofType="com.luojay.domain.Student">
            <id column="id" property="id"/>
            <result column="name" property="name"/>
            <result column="age" property="age"/>
            <result column="email" property="email"/>
        </collection>
    </resultMap>
	<!-- findCourseWithStudent 查询方法-->
    <select id="findCourseWithStudent" resultMap="associateCourseMap">
        select s.id, s.name,s.age, s.email, s.courseId,c.courseName
        from course c
                 left join student s on s.courseId = c.courseId
        where c.courseId = #{courseId}
    </select>
```

```java
//	课程实体类
public class Course {
    /** 课程id */
    private Integer courseId;
    /** 课程名称 */
    private String courseName;
    /** 学生 */
    private List<Student> student;
}
```

最后查询的日志输出

```markdown
Setting autocommit to false on JDBC Connection [com.mysql.jdbc.JDBC4Connection@428640fa]
==>  Preparing: select s.id, s.name,s.age, s.email, s.courseId,c.courseName from course c left join student s on s.courseId = c.courseId where c.courseId = ? 
==> Parameters: 1(Integer)
<==    Columns: id, name, age, email, courseId, courseName
<==        Row: 0, luojay, 24, luojay@qq.com, 1, Java程序设计
<==        Row: 2, bobi, 25, bobi@qq.com, 1, Java程序设计
<==      Total: 2
Course{courseId=1, courseName='Java程序设计'}Student{ Student{id=0, name='luojay', email='luojay@qq.com', age=24, cId=0, course=null},Student{id=2, name='bobi', email='bobi@qq.com', age=25, cId=0, course=null},}

```

### 4.2案例总结

通过mapper可以看出，我们还需要学习`resultMap`中`collection`的用法，它就是用来处理自定义的一对多的关系封装结果集。

collection标签用法：

1. property属性：javabean对象类型的属性名 
2. ofType属性：对象的全限定类名
3. collection标签内部的标签用法就跟result的基础用法一致，id对应主键列，result对应非主键列

## 5.文章配套源码
`resultMap`在实际工作中用的比较多是用来处理别名不一致的问题，同时还用来处理一对一，一对多的查询，所以在学习中很有必要掌握这个参数的使用。

[JavaNotesCode/mybatis-course-resultmap at master · CodeLuoJay/JavaNotesCode (github.com)](https://github.com/CodeLuoJay/JavaNotesCode/tree/master/mybatis-course-resultmap)

如果文章对你有帮助，请帮忙点个Start支持我一下，另外如果想持续跟进本教程，可以点击fork将本仓库克隆到你的仓库。

