# Mybatis动态SQL语句

动态sql语句只得是sql语句的内容是会动态变化的，可以根据条件变化到不同的sql语句称之为动态SQL语句。

Mybatis动态SQL语句主要是指where部分发生变化，动态sql的实现，使用的是mybatis提供的标签来实现的

用的比较多的是`<if>`，`<where>`，`<foreach>`这三个标签

## 1.动态SQL语句`if`标签使用

### 1.1基本用法

`<if>`是判断条件的，通常用来判断Java对象的属性值是否为空，从而生成不同的SQL语句

基本语法：

```markdown
<if test=判断java对象的属性值">
	部分sql语句
</if>
```

### 1.2学习案例

比如一个多条件查询语句，根据学生的ID、姓名、年龄来查找学生个人信息，有时候会用到ID，有时候会用到姓名，有时候会用年龄。那么使用`if`标签就可以写一个SQL语句，根据传入进来的条件是年龄就加上年龄的筛选条件，是姓名就加上姓名的查询。

`StudentDao.java`定义查询的方法

```java
Student findStudentByDynamicSql(Integer id,String name,Integer age);
```

`StudentDao.xml`编写动态SQL语句

```xml
    <select id="findStudentByDynamicSql" resultType="com.luojay.domain.Student">
        select id,name,email,age from Student
        where 1=1
        <!--id不为空拼接的动态sql-->
        <if test="id !=null">
            and id = #{id}
        </if>
        <!--name不为空拼接的动态sql-->
        <if test="name !=null and name != ''">
            and name = #{name}
        </if>
        <!--age不为空拼接的动态sql-->
        <if test="age !=null and age != ''">
            and age = #{age}
        </if>
    </select>
```

`TestMybatis`编写测试方法

```java
 	@Test
    public void testDynamicSqlByIf() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
        //  根据学生的id查询
        Student studentId = studentDao.findStudentByDynamicSql(1, null, null);
        System.out.println(studentId);
        //  根据学生的name查询
        Student studentName = studentDao.findStudentByDynamicSql(null, "luojay", null);
        System.out.println(studentName);
        //  根据学生的age查询
        Student studentAge = studentDao.findStudentByDynamicSql(null, null, 24);
        System.out.println(studentAge);
    }
```

日志输出

```bash
==>  Preparing: select id,name,email,age from Student where 1=1 and id = ? 
==> Parameters: 1(Integer)
<==    Columns: id, name, email, age
<==        Row: 1, mysqlTest, mysql@qq.com, 25
<==      Total: 1
Student(id=1, name=mysqlTest, email=mysql@qq.com, age=25)
==>  Preparing: select id,name,email,age from Student where 1=1 and name = ? 
==> Parameters: luojay(String)
<==    Columns: id, name, email, age
<==        Row: 0, luojay, luojay@qq.com, 24
<==      Total: 1
Student(id=0, name=luojay, email=luojay@qq.com, age=24)
==>  Preparing: select id,name,email,age from Student where 1=1 and age = ? 
==> Parameters: 24(Integer)
<==    Columns: id, name, email, age
<==        Row: 0, luojay, luojay@qq.com, 24
<==      Total: 1
Student(id=0, name=luojay, email=luojay@qq.com, age=24)
```

### 1.3案例总结

上述案例中，我使用`where 1=1`和`if`标签来拼接查询条件，根据传进`id=1`的时候，`and id = #{id}`的条件语句就会拼接到`where 1=1`之后

根据传进来的`name=luojay`的时候，`and name = #{name}`就会拼接在`where 1=1`之后

根据传进来的`age=24`的时候，`and age = #{age}`就会拼接在`where 1=1`之后

你可以将mybatis的`if`标签类比为Java的`if`分支的用法，这样就可以很好的理解。

## 2.动态SQL语句`where`标签使用

`<if>`是用来拼接`where`和查询条件，从而生成不同的SQL语句。怎么理解呢？上面`if`标签中，我用到`where 1=1`来保证后面的`id`和`name`以及`age`都能成功拼接到SQL语句上。

但是用`where 1=1`这个恒成立的语句感觉不是很优雅，如果去掉`1=1`，那么后面的`and id = #{id}`就会直接拼接到`where`的后面，形成`where and id = 1`这样查询语句。很显然，这是不符合SQL查询语法的语句

应该要改成`where id =1`才能符合SQL语法，那没有什么不用写`1=1`冗余的恒等式，又能去掉`and`的条件关键字呢，`where`标签就能做到这一点。

### 2.1基本用法

`<where>`标签是mybatis用来去除，where后面第一个动态条件中`and`或者`or`关键字，然后把其余的sql语句拼接在`where`后面。

基本语法

```markdown
<where>
	and或者or 条件语句
<where>
```

例如上面的`if`多条件查询语句的案例中，改一下换成`where`语句，就变成下面这样

```xml
    <select id="findStudentByDynamicSql" resultType="com.luojay.domain.Student">
        select id,name,email,age from Student
        <where> 
        <!--id不为空拼接的动态sql-->
        <if test="id !=null">
            and id = #{id}
        </if>
        <!--name不为空拼接的动态sql-->
        <if test="name !=null and name != ''">
            and name = #{name}
        </if>
        <!--age不为空拼接的动态sql-->
        <if test="age !=null and age != ''">
            and age = #{age}
        </if>
        <where> 
    </select>
```

然后`id=0和name=luojay`的时候，mybatis会将`where`标签转化为`where`关键字，然后根据`id=0`将一个条件`and id=#{id}`的`and`去掉，变成`where id=0`。

然后再拼接后面`and name = #{name}`,变成`where id=0 and name = 'luojay'`的SQL语句

最终完成两个条件的拼接，需要注意的是**`where`只会去除第一个条件的`and`或者`or`，像第二个条件`and name =#{name}`的条件，是不会去除的。**

在实际开发中，`where`一般搭配`if`标签一起使用，用于组合成最基本的动态语句查询。

## 3.动态SQL语句`foreach`标签使用

在实际开发中，可能会遇到通过多个id查询一个集合的学生信息。比如`select * from Student where id in(0,1,2,3)`这种需求。

面对这种开发需求的时候，一般常用的解决方法有两种。

一种在业务层中将多个ID拼接以逗号分割的字符串，然后以字符串参数形式传递给动态SQL。

```xml
 <select id="findStudentByIds" resultType="com.luojay.domain.Student">
        select id,name,email,age from Student
        <where> 
        <!--ids不为空拼接的动态sql-->
        <if test="ids !=null">
            and id in (#{ids})
        </if>
        <where> 
    </select>
```

另一种在mybatis中使用`foreach`中循环遍历，拼接成字符串形成动态参数

```xml
 <select id="findStudentByIds" resultType="com.luojay.domain.Student">
        select id,name,email,age from Student
        <where>
            <if test="ids !=null ">
            and id in
                <foreach collection="ids" item="item" open="(" close=")" separator=",">
                    #{item}
                </foreach>
            </if>
        </where>
</select>
```

### 3.1基本用法

`<foreach>`标签是mybatis用来将集合元素遍历出来，并且提供自定义的拼接方式的标签。

`<foreach>`基本语法：

1. collection属性：指定要遍历的集合参数名
2. item属性：遍历出来的每一项元素的别名
3. open属性：以什么字符连接遍历出来的元素的开头
4. close属性：以什么字符连接遍历出来的元素的结尾
5. separator属性：遍历出来的每一项元素之间以什么字符分割

```xml
 <foreach collection="ids" item="item" open="(" close=")" separator=",">
      #{item}
 </foreach>
```

这段SQL语句的解释：假设`ids=[0,1,2,3]`

将`ids`的集合里面的每一项`item`,遍历拼接以`(`开头和以`)`结尾的括号中，并且以逗号连接每一个`item`，最终输出的的结果是`(0,1,2,3)`

配合`if`标签内部的`and id in`会生成`and id in (0,1,2,3)`，再结合`where`标签，生成的动态SQL语句是`select id,name,email,age from Student where id in (0,1,2,3)` 

### 3.2学习案例

`StudentDao.java`定义根据ids查询多个学生信息

```java
    /**
     * 通过id的集合查询多个学生
     *
     * @param ids id
     * @return {@link Student}
     */
    List<Student> findStudentByIds(@Param("ids")List<Integer> ids)
```

`StudentDao.xml`定义查询SQL语句，并且使用`foreach`标签来遍历

```
<select id="findStudentByIds" resultType="com.luojay.domain.Student">
        select id,name,email,age from Student
        <where>
            <if test="ids !=null ">
            and id in
                <foreach collection="ids" item="item" open="(" close=")" separator=",">
                    #{item}
                </foreach>
            </if>
        </where>
 </select>
```

`testMybatis.java`编写测试方法

```java
	 @Test
    public void testFindStudentByForeach() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
        //  根据学生的id查询
        List<Integer> ids = Arrays.asList(0, 1, 2, 3);
        List<Student> studentList = studentDao.findStudentByIds(ids);
        studentList.forEach(System.out::println);
    }
```

输出日志

```markdown
==>  Preparing: select id,name,email,age from Student WHERE id in ( ? , ? , ? , ? ) 
==> Parameters: 0(Integer), 1(Integer), 2(Integer), 3(Integer)
<==    Columns: id, name, email, age
<==        Row: 0, luojay, luojay@qq.com, 24
<==        Row: 1, mysqlTest, mysql@qq.com, 25
<==        Row: 2, bobi, bobi@qq.com, 25
<==        Row: 3, bobi, bobi@qq.com, 25
<==      Total: 4
Student(id=0, name=luojay, email=luojay@qq.com, age=24)
Student(id=1, name=mysqlTest, email=mysql@qq.com, age=25)
Student(id=2, name=bobi, email=bobi@qq.com, age=25)
Student(id=3, name=bobi, email=bobi@qq.com, age=25)
```

从打印出来的日志可以看出，foreach标签确实如我们之前提到，将ids的中id按照我们制定`(0,1,2,3)`这种形式拼接，最终查询多个学生信息。

## 4.总结和源码下载

`if`、`where`、`foreach`这三个标签实际开发中是经常用到，是最最最基础的三个动态语句的标签，所以必须要掌握这三个标签的使用。

[JavaNotesCode/mybatis-dynamic-sql at master · CodeLuoJay/JavaNotesCode (github.com)](https://github.com/CodeLuoJay/JavaNotesCode/tree/master/mybatis-dynamic-sql)

如果文章对你有帮助，请帮忙点个Start支持我一下，另外如果想持续跟进本教程，可以点击fork将本仓库克隆到你的仓库。