# Mybatis主配置文件内容

回顾一开始学习的时候建的主配置文件，里面只包含简单的三个部分的内容，分别是`setting`、`<typeAliases>`、`<environments>`、以及`mappers`这几个配置文件的内容。

在基本大差不差的学完一半的Mybatis的课程时候，因为基本按照我所写的文章和学习的视频，前面所学的内容基本上能满足日常的增删改查需求。

当然啦，mybatis还有需要深入的课程。比如理解和掌握Mybatis的执行的流程、研读MyBatis的的源码等等后续深入的课程。扯远了，在前面的基础课程其实没有比较好写文章介绍主配置文件，这里就来过一下。

## 1.学习Mybatis的`<Setting>`标签配置内容

`<Setting>`标签的内容是 MyBatis 中极为重要的调整设置，它们会改变 MyBatis 的运行时行为。这是在官网中的描述，详细在官网的 [mybatis – MyBatis 3 | 配置](https://mybatis.org/mybatis-3/zh/configuration.html#settings)找到对应的文档学习，这里主要挑一些日常开发常用的来记录一下。

### 1.1`<setting>`配置日志控制台输出

日志是开发中经常用来记录程序执行过程输出的关键信息的文件，用好日志能帮助我们快速定位程序产生问题的位置，产生问题的原因。Mybatis的日志，能很好输出SQL语句，帮助我们查看SQL的执行流程，入参和生成的查询语句，以及查询的结果集等等，所以日志是很重要的配置项。

```xml
<configuration>
    <settings>
        <!--设置mybatis日志，输出到控制台-->
        <setting name="logImpl" value="STDOUT_LOGGING"/>
    </settings>
</configuration>
```

首先讲解一下`<setting>`标签的基本用法：

1. name属性：对应是Mybatis配置项的名字，如`logImpl`指的是MyBatis 所用日志的具体实现
2. value属性：对应是Mybatis配置项的值，如`STDOUT_LOGGING`是说日志实现将日志输出到控制台

其他详细取值可以去官网[mybatis – MyBatis 3 | 配置](https://mybatis.org/mybatis-3/zh/configuration.html#settings)查看配置表的各项内容，选择配置项名和值设置即可

![image-20210601205859041](https://gitee.com/codeluojay/TyproaImage/raw/master/images/image-20210601205859041.png)

### 1.2`<setting>`配置日志其他实现输出

Mybatis 通过使用内置的日志工厂提供日志功能。内置日志工厂将会把日志工作委托给下面的实现之一：

- SLF4J
- Apache Commons Logging
- Log4j 2
- Log4j
- JDK logging

入门案例中没有配置SLF4J到Log4j的日志实现，那么Mybatis使用的JDK自带的日志门面实现`JDK logging`,也就是上面1.1中的内容了。但我留意到官网中提到可以引入log4j来实现，于是就撸了一个利用log4j实现控制台输出的小案例。

#### 1.2.1依赖引入

引入log4j的jar包，因为我使用的maven管理依赖，所以就去maven中央仓库找log4j的依赖

```xml
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
```

#### 1.2.2配置`<setting>`文件内容

按照官网把之前`<setting value="">`改成`LOG4J`

```xml
<configuration>
  <settings>
    <setting name="logImpl" value="LOG4J"/>
  </settings>
  ...
</configuration>
```

#### 1.2.1添加log4j配置文件

在应用的类路径中创建一个名为 `log4j.properties` 的文件，防止类路径下也就是`resource`目录下。文件的具体内容如下：

`log4j.properties`

```properties
# 全局日志配置
log4j.rootLogger=DEBUG,Console
# 控制台输出
log4j.appender.Console=org.apache.log4j.ConsoleAppender
log4j.appender.Console.layout=org.apache.log4j.PatternLayout
log4j.appender.Console.layout.ConversionPattern=%d [%t] %-5p [%c] - %m%n
log4j.logger.org.apache=INFO
```

这里简单说一下配置文件基本格式和语法

```properties
#	配置根Logger
log4j.rootLogger=[level],appenderName1,appenderName2, …

#	配置日志信息输出目的地Appender(全限定类名)
log4j.appender.appenderName=fully.qualified.name.of.appender.class 
#	配置日志信息的格式（布局）
log4j.appender.appenderName.layout=fully.qualified.name.of.layout.class 
#	配置日志信息输出内容的格式
log4j.appender.appenderName.layout.ConversionPattern=%d [%t] %-5p [%c] - %m%n
```

下面对`log4j.properties`的内容进行说明

`log4j.rootLogger=DEBUG,Console`定义全局的日志输出级别为`DEBUG`级别,目的地`Appender`的名字为`Console`

接下来就是对`Appender`的名字为`Console`的配置

`log4j.appender.Console=org.apache.log4j.ConsoleAppender`是配置了`Console`指向`org.apache.log4j.ConsoleAppender`这个控制台输出类的全限定类名

`log4j.appender.Console.layout = org.apache.log4j.PatternLayout`是配置了`Console`是可以灵活地指定布局模式

`log4j.appender.Console.layout.ConversionPattern=%d [%t] %-5p [%c] - %m%n`是日志自定义输出内容的格式

输出格式解释：

```markdown
%d 产生日志的时间
%t 产生日志所处的线程名称
%-5p 输出日志的级别，将占5位字符，不足5位用空格填补，-指的是在右边补齐，没有则在左边
%c 输出日志的包以及类的全名
%m 附加的信息
%n 换行
```

`log4j.logger.org.apache=INFO`是配置代表为特定的包（org.apache）配置特定的级别INFO,这样就把上面总的级别DEBU覆盖

最后运行测试类的方法

```java
	@Test
    public void testFindAllStudents() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
        System.out.println(studentDao.findAllStudents());
    }
```

输出的日志内容

```markdown

Connected to the target VM, address: '127.0.0.1:61166', transport: 'socket'
2021-06-01 23:57:14,938 [main] DEBUG [com.luojay.dao.StudentDao.findAllStudents] - ==>  Preparing: select id,name,email,age from Student order by id 
2021-06-01 23:57:15,055 [main] DEBUG [com.luojay.dao.StudentDao.findAllStudents] - ==> Parameters: 
2021-06-01 23:57:15,084 [main] DEBUG [com.luojay.dao.StudentDao.findAllStudents] - <==      Total: 4
[Student(id=0, name=luojay, email=luojay@qq.com, age=24), Student(id=1, name=mysqlTest, email=mysql@qq.com, age=25), Student(id=2, name=bobi, email=bobi@qq.com, age=25), Student(id=3, name=bobi, email=bobi@qq.com, age=25)]
```

#### 1.3常用的`<setting>`配置项

1. **mapUnderscoreToCamelCase**：是否开启驼峰命名自动映射，即从经典数据库列名 A_COLUMN 映射到经典 Java 属性名 aColumn。
2. **useGeneratedKeys**：允许 JDBC 支持自动生成主键，需要数据库驱动支持。如果设置为 true，将强制使用自动生成主键。
3. **logImpl**： 指定 MyBatis 所用日志的具体实现，未指定时将自动查找。

## 2.学习Mybatis的`<typeAliases>`标签配置内容

### 2.1为单个类配置别名

typeAlias基本用法：

1. type属性：要起别名的类的全限定类名
2. alias属性：别名名字

类型别名可为 Java 类型设置一个缩写名字。 它仅用于 XML 配置，意在降低冗余的全限定类名书写。例如：

```xml
<typeAliases>
        <typeAlias type="com.luojay.domain.student" alias="stu"/>
</typeAliases>
```

这样我们在编写`mapper`的时候，可以将返回值类型写成`stu`这种别名形式，可以减少冗余的全限定类名书写。

```xml
 <select id="selectStudentByAlias" resultType="stu">
        select id,name,email,age from Student order by id
    </select>
```

### 2.2给多个类配置别名

也可以指定一个包名，MyBatis 会在包名下面搜索需要的 Java Bean，比如：

```xml
<typeAliases>
        <package name="com.luojay.domain"/>
</typeAliases>
```

每一个在包 `com.luojay.domain` 中的 Java Bean，在没有注解的情况下，会使用 Bean 的首字母小写的非限定类名来作为它的别名，这个包的`Student.java`可以使用`student`来引用它

```xml
 <select id="selectStudentByAlias" resultType="student">
        select id,name,email,age from Student order by id
    </select>
```

### 2.3给多个类配置别名并且破坏默认的别名规则

当给多个类配置别名也就是给起别名的时候配置一个包下所有的类，此时会使用 Bean 的首字母小写的非限定类名来作为它的别名，那么如果单独想给某个类写一个特殊的别名，则可以在用`@Alias("别名")`的注解。

```java
//	起单个别名
@Data
@NoArgsConstructor
@AllArgsConstructor
@Alias("student_vo")
public class StudentVO {
    private Integer id;
    private String name;
    private String email;
    private Integer age;
}
```

然后对应的`mapper`文件

```
    <select id="selectStudentBySingleAlias" resultType="student_vo">
        select id,name,email,age from Student order by id
    </select>
```

通常会用`@Alias`解决一些别名冲突问题

## 3.学习Mybatis的`<environments>`标签配置内容

实际开发中，可能程序员用开发数据库进行开发程序，测试人员连接专用测试库测试程序。所以就可能同一个程序，可能要在开发和测试库反复切换。如果手动去改数据库连接配置信息，那肯定是很慢且很LOW的方式。

对于这种常见的需求，MyBatis 提供`<environments>`标签用于切换不同数据库环境，每一个环境对应的连接数据库信息我们提前准备好，然后我们只需要更换一个环境ID就可以做到切换不同数据库，环境ID就是标识唯一的数据库配置环境信息。

### 3.1基本用法

下面是一个常用的环境配置信息模板，以它来讲解mybatis的环境配置信息基本内容

```xml
<environments default="dev">
        <!--environment：一个数据库信息的配置（环境） id：一个唯一值，自定义，表示环境的名称-->
        <!--用于开发的数据库环境实例-->
        <environment id="dev">
            <!--transactionManager mybatis的事务类型 type：JDBC(表示使用jdbc中的Connection对象的commit,rollback)-->
            <transactionManager type="JDBC"/>
            <!--dataSource type：表示数据源的类型，POOLED表示使用连接池-->
            <dataSource type="POOLED">
                <!--driver url username password 是固定的，不能自定义的 值可以自定义-->
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/springdb"/>
                <property name="username" value="root"/>
                <property name="password" value="root"/>
            </dataSource>
        </environment>
</environments>
```

#### 3.1.1`<environments>`标签

`environments`中 default属性：比如：`default="dev"`是当前选择用dev的环境ID对应数据库配置信息连接数据库，一个`environments`可以配置多个`environment `

通常日常开发，会常见定义不同环境ID,`dev`对应开发库，`pro`对应生产库，`test`对应测试库

#### 3.1.2`transactionManager`标签

用来指定Mybatis使用事务管理器的配置，比如：type="JDBC"，type：表示事务的类型，JDBC表示使用jdbc中的Connection对象的commit,rollback等方法提交事务。

在 MyBatis 中有两种类型的事务管理器（也就是 type="[JDBC|MANAGED]"）：

- **JDBC** – 使用jdbc中的Connection对象的commit（提交）,rollback（回滚）等方法管理事务。
- **MANAGED** – 不配置事务。配置后它不会提交或回滚一个连接，而是让容器来管理事务的提交和回滚。

> **提示** 如果你正在使用 Spring + MyBatis，则没有必要配置事务管理器，因为 Spring 模块会使用自带的管理器来覆盖前面的配置。

#### 3.1.3`<environment>` 标签

每个 `environment` 中的 id：比如：`id="dev"`用来标识全局唯一对应的数据库连接信息。

#### 3.1.4`dataSource`标签

`dataSource`标签：用于配置数据源，比如：type="POOLED"。type：表示数据源的类型，POOLED表示利用“池”的概念将 JDBC 连接对象组织起来，避免了创建新的连接实例时所必需的初始化和认证时间。

有三种内建的数据源类型（也就是 type="[UNPOOLED|POOLED|JNDI]"）：

**UNPOOLED**– 这个数据源的实现会每次请求时打开和关闭连接

**POOLED**– 这种数据源的实现利用“池”的概念将 JDBC 连接对象组织起来，避免了每次操作数据库都创建新的连接实例时所必需的初始化和认证时间

**JNDI** – 这个比较老的属性值，在一些历史悠久的项目中才会看到，这个数据源实现是为了能在如 EJB 或应用服务器这类容器中使用

## 4.学习Mybatis的`<mappers>`标签配置内容

`mappers`标签是用来告诉mybatis去哪找我们写的接口方法的对应的SQL映射的xml文件，在实际开发中，通常是指定一个包名的方式来配置多个SQL映射xml文件。

每一个`<mappers>`标签下面可以配置多个`<mapper>`标签

```xml
    <mappers>
        <!--一个mapper标签指定一个文件的位置，从类路径开始的路径信息  类路径：target/classes-->
        <mapper resource="com/luojay/dao/StudentDao.xml"/>
        <mapper resource="com/luojay/dao/CourseDao.xml"/>
    </mappers>
```

### 4.1使用相对于类路径找到一对一映射文件

`mapper`使用`resource`属性是告诉mybatis用相对类路径的去查找对应的SQL映射xml文件，**类路径**是哪个路径呢？就是我们编译后项目后的target文件夹的内部的classes文件夹内就是类路径。

![image-20210602204834828](https://gitee.com/codeluojay/TyproaImage/raw/master/images/image-20210602204834828.png)

以maven项目为例讲解类路径

1. ①中的所有文件（maven项目的main文件目录下所有文件）经过maven的compile命令或者IDEA自带的构建工具，将会在项目下新建target文件夹，并将①中的所有文件按照编译规范复制到②中
2. target目录与src目录是平级目录，它下面的classes文件内部就是类路径的根路径
3. `com/luojay/dao/StudentDao.xml`就是在`target/classes/`下经过拼接找到对应的xml文件

![image-20210602205909090](https://gitee.com/codeluojay/TyproaImage/raw/master/images/image-20210602205909090.png)

### 4.2使用完全限定类名找到一对一映射文件

`StudentDao.java`是有一个全限定类名(idea 快捷建ctrl+shift+alt+a可以复制全限定类名)，对应它的字节码文件`StudentDao.class`。通过这个也可以指定一对一的SQL映射xml文件。

因此我们只要通过`<mapper>`的`class`属性，也可以找到跟它一一对应的xml文件。

```xml
<mappers>
  <mapper class="com.luojay.dao.StudentDao"/>
</mappers>
```

### 4.3使用包名找到多个映射文件

`package`标签的`name`属性就是告诉mybatis去到这个属性值的全限定命名包下`com.luojay.dao`找到mapper同名的xml文件，将它注册到`sqlSessionfactory`中。

```
 <mappers>
 		<!--使用包名注册多个mapper-->
        <package name="com.luojay.dao"/>
 </mappers>
```

这样就不用一个个手写`<mapper>`标签去对应。





文章配套源码[JavaNotesCode/mybatis-main-properties at master · CodeLuoJay/JavaNotesCode (github.com)](https://github.com/CodeLuoJay/JavaNotesCode/tree/master/mybatis-main-properties)