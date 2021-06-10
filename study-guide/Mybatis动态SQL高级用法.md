# Mybatis动态SQL高级用法

为什么还要强调动态SQL的高级用法，源于我利用Mybatis逆向工程或者MybatisCodeHelper这些插件快速生成CRUD代码时，它们写的动态SQL语句比较工整，而且抽取的比较合理，所以我自己又额外再补充一些动态SQL的用法。

因为只要掌握了前面的动态SQL的基本标签`where`、`if`、`foreach`等标签的话，其实可以完成日常CRUD的百分之八十的需求了，还有剩下的百分之二十的需求可能要结合本文的相关标签去进行学习，才能写出比较灵活的Mybatis动态语句。

## 1.`trim`标签

### 1.1.多个`and`关键字案例引入思考问题

举个例子，之前我们利用`where`去掉第一个`and`关键字，现在我们想想，除了这种方法，对于下面的SQL还有没有其他的办法解决多余的`and`关键字吗？比如下面的SQL语句。

```sql
 <select id="findStudentWithoutTrim" resultType="com.luojay.domain.Student">
        select id,name,email,age from Student
        where
        <if test="id != null">
            id = #{id}
        </if>
        <if test="name != null">
            and name like  concat('%',#{name},'%')
        </if>
        <if test="age != null">
            and age =  #{age}
        </if>
    </select>
```

如果传进来的`id`为空，那么后面无论是`name`还是`age`有值，都会报错。因为`where`会接上`and `关键字再拼接参数值进SQL语句，这样的SQL语句就不符合语法。

### 1.2.去除SQL语句中多余的`and`关键字的常用方法

解决上面的办法其实有三种

1. `where`后面用`1=1`恒等式连接，这样后面紧接的`and`关键字也能顺利拼接，形成`where 1=1 and name like '%xxx%' and age = 24`这样SQL语句。

   ```sql
    <select id="findStudentWithoutTrim" resultType="com.luojay.domain.Student">
           select id,name,email,age from Student
           where 1=1
           <if test="id != null">
               id = #{id}
           </if>
           <if test="name != null">
               and name like  concat('%',#{name},'%')
           </if>
           <if test="age != null">
               and age =  #{age}
           </if>
    </select>
   ```

   但是这样写在阿里巴巴的规范的手册是不建议的，显得比较low

2. 用`where`标签包裹住参数条件，`where` 标签只会在至少有一个条件成立返回 SQL 子句的情况下才去插入`where`子句。并且会将开头子句中如果第一个关键字为`and`或`or`去除掉，剩下的`and`或者`or`不会去除。

   ```sql
    <select id="findStudentWithoutTrim" resultType="com.luojay.domain.Student">
           select id,name,email,age from Student
           <where> 
           <if test="id != null">
               id = #{id}
           </if>
           <if test="name != null">
               and name like  concat('%',#{name},'%')
           </if>
           <if test="age != null">
               and age =  #{age}
           </if>
           </where> 
    </select>
   ```

3. 用`trim`标签包裹住条件语句，并且使用`prefix`这个前缀属性和`prefixOverrides`前缀重写属性，拼接`where`和去掉`and`。

   ```sql
   <select id="findStudentWithTrim" resultType="com.luojay.domain.Student">
           select id,name,email,age from Student
           <trim prefix="where" prefixOverrides="and |or">
               <if test="id != null">
                   id = #{id}
               </if>
               <if test="name != null">
                   and name like  concat('%',#{name},'%')
               </if>
               <if test="age != null">
                   and age =  #{age}
               </if>
           </trim>
   </select>
   ```

   使用`<trim prefix="where" prefixOverrides="and |or">`会将开头子句中如果第一个关键字为`and`或`or`去除掉，剩下的`and`或者`or`不会去除。

### 1.3.`trim`标签基本用法

Mybatis的`trim`标签可以用于去除sql语句中多余的and关键字，逗号，或者给sql语句前拼接 `where`、`set`以及`values(` 等前缀，或者添加`)`等后缀，可用于选择性插入、更新、删除或者条件查询等操作。

1. prefix属性：给sql语句拼接的前缀
2. suffix属性：给sql语句拼接的后缀
3. prefixOverrides属性：去除sql语句前面的关键字或者字符，如果该属性指定为`and`，当sql语句的开头为`and`，trim标签将会去除该`and`,如果为`and |or`，则会匹配开头是`and`或者`or`，将其中一个去掉。
4. suffixOverrides属性：去除sql语句后面的关键字或者字符，和`prefixOverrides`相反，它是去除末尾的字符，一般用来去除SQL中多余的逗号。

### 1.4.去除SQL语句中多余的逗号

去除多余的逗号，一般用在`insert`语句中比较多。还是直接上案例来讲解吧。

```sql
<insert id="insertStudentWithTrimDeleteComma">
        insert into student
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="id != null">
                id,
            </if>
            <if test="name != null">
                name,
            </if>
            <if test="age != null">
                age,
            </if>
            <if test="courseId != null">
                courseId,
            </if>
        </trim>
        <trim prefix="value(" suffix=")" suffixOverrides=",">
            <if test="id != null">
                #{id},
            </if>
            <if test="name != null">
                #{name},
            </if>
            <if test="age != null">
                #{age},
            </if>
            <if test="courseId != null">
                #{courseId},
            </if>
        </trim>
    </insert>
```

## 2.`set`标签

### 2.1基本用法

`set`标签和`where`标签功能有点类似，`where`标签主要用来去除查询语句中第一个`and`或者`or`关键字，`set`会动态地在行首插入 SET 关键字，并会删掉额外的逗号。

这里我们先回想一下我们手写更新的SQL语句，特别是要更新多个字段的SQL语句，以更新学生表为例子。

```sql
update student set name = 'luojay',age = 25,eamil = 'luojay@qq.com' where id  = 1;
```

我们如果想要用Mybatis实现`name`、`age`、`eamil`传进哪一个，就更新对应的值拼接SQL语句一起更新多个字段的语句，就可能要Mybatis的`set`标签。

### 2.2`set`标签去除多余的逗号

学习案例代码简单

```java
// StudentDao.java    
	/**
     * 通过Set删除多余逗号来更新学生记录
     * @param student {@link Student}
     * @return 影响的行数
     */
    int updateStudentWithSet(Student student);

		//	测试类中的关键代码
	 public void testUpdateStudentWithSet() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
        Student student = new Student();
        student.setId(4);
        student.setAge(25);
        student.setName("lilei01");
        student.setCourseId(1);
        studentDao.updateStudentWithSet(student);
    }

```

`StudentDao.xml`

```sql
<update id="updateStudentWithSet" parameterType="com.luojay.domain.Student">
        update student
        <set>
            <if test="name!=null">
                name = #{name},
            </if>
            <if test="age!=null">
                age = #{age},
            </if>
            <if test="email!=null">
                email = #{email},
            </if>
        </set>
        where id = #{id}
</update>
```

上述SQL中，Mybatis实现set删除`update student SET name = ?, age = ?,`末尾的逗号拼接成SQL语句`update student SET name = ?, age = ? where id = ? `

### 2.3.利用`trim`标签实现`set`等价功能

```xml
<update id="updateStudentWithTrim" parameterType="com.luojay.domain.Student">
        update student
        <trim prefix="set" suffixOverrides=",">
            <if test="name!=null">
                name = #{name},
            </if>
            <if test="age!=null">
                age = #{age},
            </if>
            <if test="email!=null">
                email = #{email},
            </if>
        </trim>
        where id = #{id}
    </update>
```

这里的`trim`标签，用到的原理也很简单，就是用到前缀属性`prefix`,在SQL`update student`之后插入`set`关键字，接着用后缀覆盖属性`suffixOverrides`,将满足条件的更新字段依次插入然后在`where id =#{id}`之前将末尾的逗号删除替换掉

## 3.`choose`、`when`、`otherwise`标签结合使用

有时候，我们不想使用所有的条件，而只是想从多个条件中选择一个使用。针对这种情况，MyBatis 提供了 `choose`标签，它有点像 Java 中的 switch 语句。

如果将它和Java的`switch`分支语句进行类比，那么`choose`相当`switch`，`when`相当于`case`，`otherwise`相当于`default`。

那么来实现一个简单的需求，查询学生，可以通过姓名，年龄来查询，当这个两个年条件都不满足的时候，就查询出课程ID为`1`的所有学生。

```xml
<select id="findStudentWithChooseWhenOtherWise" resultType="com.luojay.domain.Student">
        select id,name,email,age,courseId from Student
        <where>
            <choose>
                <when test="name != null and name != ''">
                    and name like concat('%',#{name},'%')
                </when>
                <when test="age != null ">
                    and age =#{age}
                </when>
                <otherwise>
                    and courseId = 1
                </otherwise>
            </choose>
        </where>
    </select>
```

日志输出

```bash
==>  Preparing: select id,name,email,age,courseId from Student WHERE courseId = 1 
==> Parameters: 
<==    Columns: id, name, email, age, courseId
<==        Row: 0, luojay, luojay@qq.com, 24, 1
<==        Row: 2, bobi, bobi@qq.com, 24, 1
<==      Total: 2
Student(id=0, name=luojay, email=luojay@qq.com, age=24, courseId=1)
Student(id=2, name=bobi, email=bobi@qq.com, age=24, courseId=1)
```

## 4.`sql`和`include`标签重复使用SQL子句

### 4.1.案例引入

我们在日常工作中肯定会遇到查询多个同样的列名，但是因为查询的条件不同，经常要写两个查询方法。这时候，对于相同的SQL子句，我们其实可以用`sql`和`include`将它公共部分抽取出来，然后调用这些公共子句，来简化代码的书写。

举个例子，我有一个需求，查询所有学生的信息和批量查询学习`id`为`0,1`的所有学生信息，那么按照不抽取的做法写的SQL语句如下

```sql
 <select id="findStudentBySQL" resultType="com.luojay.domain.Student">
        select id, name,email,age,courseId
        from student
    </select>
    
    <select id="findStudentByIds" resultType="com.luojay.domain.Student">
        select id, name,email,age,courseId
        from Student
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

那么两个方法都重复使用到`id, name,email,age,courseId`,那么我们可以考虑将这部分有`sql`和`include`抽取出来。

当然可能有人会说，直接用`select * from student`不就得了。那是在列名比较少，数据的体量少的时候可以这么做。试想一下当一个表有十列以上的列名，而我们只需要指定的部分信息，同时两个方法都用到，那么`sql`和`include`必然是最佳的选择。

### 4.2.案例学习

对于上面的方法，我们稍稍改造就可以实现复用

```sql
	<sql id="Base_Column_List">
            id, name,email,age,courseId
    </sql>
    <select id="findStudentBySQL" resultType="com.luojay.domain.Student">
        select
        <include refid="Base_Column_List"/>
        from student
    </select>
```

首先，案例用`sql`标签包裹住查询的列名，然后指定唯一的`id`属性来表示这个代码片段。

然后在查询方法中通过`include`中的`refid`属性来复用代码片段，这样就可以不用重复写列名查询。

通过上面的方法，对案例中的代码进行改造，再写个测试类，就可以轻松完成这个标签的使用。
# 案例源码
[JavaNotesCode/mybatis-dynamic-sql-advanced at master · CodeLuoJay/JavaNotesCode (github.com)](https://github.com/CodeLuoJay/JavaNotesCode/tree/master/mybatis-dynamic-sql-advanced)



