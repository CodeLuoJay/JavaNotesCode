# Mybatis分页插件

## 1.引言

Mybatis-PageHelper是一个国人刘增辉基于Mybatis框架写的一个分页组件，让我们写SQL的时候可以简化分页语句书写。

## 2.如何使用

在之前的Mybatis学习案例七的基础之上，再引入这个分页助手的学习，目的就是为了更好的理解和方便日常的开发。

### 2.1基本使用

首先必须默认是mybatis的orm依赖框架之上，然后再引入Mybatis-PageHelper并且在Mybatis的主配置文件中引入配置一个插件，然后再调用PageHelper的相关方法即可。

### 2.2案例教程

1.引入依赖

在maven中央仓库搜索PageHelper依赖，具体的地址可以点进下面的链接

[Maven Repository: com.github.pagehelper » pagehelper » 5.2.0 (mvnrepository.com)](https://mvnrepository.com/artifact/com.github.pagehelper/pagehelper/5.2.0)

2.主配置文件中使用`plugins`标签配置插件

引入时需要注意顺序：

plugins在配置文件中的位置必须符合要求，否则会报错。  `<plugins>`必须在`< environments>`之前

```xml
<plugins>
    <plugin interceptor="com.github.pagehelper.PageInterceptor"></plugin>
</plugins>
```

3.`StudentDao.java`接口查询方法

```java
public interface StudentDao {
    /**
     * 通过分页插件来查询学生
     *
     * @return {@link List<Student>}
     */
    List<Student> selectStudentByPageHelper();
}
```

4.`StudentDao.xml`

```
<mapper namespace="com.luojay.dao.StudentDao">
    <select id="selectStudentByPageHelper" resultType="student">
        select id,name,email,age from Student order by id
    </select>
</mapper>
```

3.在查询方法前调用`PageHelper.startPage(pageNum,pageSize)`

```java
    //	pageNum:第几页，从1开始 pageSize：一页要查询多少条数据
    public void testSelectStudentByPageHelper() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
        PageHelper.startPage(1,3);
        List<Student> studentList = studentDao.selectStudentByPageHelper();
        studentList.forEach(System.out::println);
    }
```

4.查询日志

```markdown
2021-06-03 22:08:56,891 [main] DEBUG [SQL_CACHE] - Cache Hit Ratio [SQL_CACHE]: 0.0
2021-06-03 22:08:57,093 [main] DEBUG [com.luojay.dao.StudentDao.selectStudentByPageHelper_COUNT] - ==>  Preparing: SELECT count(0) FROM Student 
2021-06-03 22:08:57,168 [main] DEBUG [com.luojay.dao.StudentDao.selectStudentByPageHelper_COUNT] - ==> Parameters: 
2021-06-03 22:08:57,195 [main] DEBUG [com.luojay.dao.StudentDao.selectStudentByPageHelper_COUNT] - <==      Total: 1
2021-06-03 22:08:57,200 [main] DEBUG [com.luojay.dao.StudentDao.selectStudentByPageHelper] - ==>  Preparing: select id,name,email,age from Student order by id LIMIT ? 
2021-06-03 22:08:57,201 [main] DEBUG [com.luojay.dao.StudentDao.selectStudentByPageHelper] - ==> Parameters: 3(Integer)
2021-06-03 22:08:57,204 [main] DEBUG [com.luojay.dao.StudentDao.selectStudentByPageHelper] - <==      Total: 3
Student(id=0, name=luojay, email=luojay@qq.com, age=24)
Student(id=1, name=mysqlTest, email=mysql@qq.com, age=25)
Student(id=2, name=bobi, email=bobi@qq.com, age=25)
```

## 3.PageHelper分页插件的原理

首先看下我数据库的所有数据，一共式4条数据。

![image-20210603222747507](https://gitee.com/codeluojay/TyproaImage/raw/master/images/image-20210603222747507.png)



1. 在调用查询学生的方法时之前，会先调用`SELECT count(0) FROM Student `去数据库中查询学生表的总数据，这条语句查询的数据是4。
2. 然后再调用我们mapper对应SQL语句`select id,name,email,age from Student order by id`,并且再后面加上`limit ?`。
3. `limit`是mysql数据库用来限制查询数据的关键字，`limit`后面可以跟两个参数，第一个是从第几条开始（默认从0开始，0可以缺省不写，默认就是从0开始），第二个是查询的数据的数量。
4. 所以`limit ?`实际上就是`limit 0,3`，意思就是从第1条开始查（mysql中的数据索引从0开始就是第一条），一共查询3条。

那么我们可以试试`PageHelper.startPage(2,3);`看看打印的日志

```
2021-06-03 22:42:58,588 [main] DEBUG [com.luojay.dao.StudentDao.selectStudentByPageHelper] - ==>  Preparing: select id,name,email,age from Student order by id LIMIT ?, ? 
2021-06-03 22:42:58,589 [main] DEBUG [com.luojay.dao.StudentDao.selectStudentByPageHelper] - ==> Parameters: 3(Long), 3(Integer)
2021-06-03 22:42:58,594 [main] DEBUG [com.luojay.dao.StudentDao.selectStudentByPageHelper] - <==      Total: 1
```

跟据SQL语句和传递的参数可以知道，完整执行的SQL语句是`select id,name,email,age from Student order by id LIMIT 3, 3 `

那么这一步就可以具体来将执行的原理：

1. 首先先去数据库查询总数为4，然后根据`pageSize=3`，计算分页。
2. 4条数据，每页查3条，一共可以分2页。
3. 然后我们调用`PageHelper.startPage(2,3);`时，是这样的一个计算方法：
   - （pageNum-1）* pageSize ==> （2-1）*3 得出limit第一个参数 3 
   -  然后把pageSize传递给limit第二个参数，所以是limit 3,3

## 4.日常开发常用的方法

日常开发中，我们可能会涉及后端要传递分页的数据给前端，包含总数，每页数目，开始页的页码，结束页的页码等等。

所以一般常用下面的方法对这些数据进行查询封装

```java
public class TestMybatis {
    @Test
    public void testGeneralPageHelperMethod() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
        PageHelper.startPage(2,3);
        List<Student> studentList = studentDao.selectStudentByPageHelper();
        PageInfo pageInfo = new PageInfo(studentList);
        System.out.println(pageInfo);
    }
}
```

打印出来的`pageInfo`对象信息

```markdown
PageInfo{pageNum=2, pageSize=3, size=1, startRow=4, endRow=4, total=4, pages=2, list=Page{count=true, pageNum=2, pageSize=3, startRow=3, endRow=6, total=4, pages=2, reasonable=false, pageSizeZero=false}[Student(id=3, name=bobi, email=bobi@qq.com, age=25)], prePage=1, nextPage=0, isFirstPage=false, isLastPage=true, hasPreviousPage=true, hasNextPage=false, navigatePages=8, navigateFirstPage=1, navigateLastPage=2, navigatepageNums=[1, 2]}
```

这样就可以各取所需给到对应的前端所需要的分页信息。

## 文章源码学习
[JavaNotesCode/mybatis-plugins-pagehelper at master · CodeLuoJay/JavaNotesCode (github.com)](https://github.com/CodeLuoJay/JavaNotesCode/tree/master/mybatis-plugins-pagehelper)