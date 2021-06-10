## 1.项目介绍

一直以来我都认为提升自己的编程能力就是要多动手敲代码，由于自己实在不聪明，所以只能以视频为主线的学习方式去学习Java领域的技术和框架。所以该项目是以我看过的B站编程视频的案例为主，做的一个实战代码学习项目。

实战案例中代码均来自B站中视频，以视频中案例为基础，可能会在代码案例中加入个人的优化，希望能帮助有需要的小伙伴，也希望自己能够坚持下来做好自己的学习规划。

## 2.目录介绍

### 2.1当前目录结构

| 目录             | 说明             | 对应文章                                                     |
| ---------------- | ---------------- | ------------------------------------------------------------ |
| study-guide      | 所有文章集合     | -                                                            |
| java-tool-hutool | hutool工具类学习 |                                                              |
| java-tool-lombok | lombok插件学习   |                                                              |
| mybatis-course-start   | mybatis入门学习  | [Mybatis入门学习一](study-guide/Mybatis入门学习一.md) |
| mybatis-course-util   | mybatis入门学习  | [Mybatis入门学习二](study-guide/Mybatis入门学习二.md) |
| mybatis-course-dao and proxy   | mybatis入门学习  | [Mybatis入门学习三](study-guide/Mybatis入门学习三.md) |
| mybatis-course-resulttype   | mybatis入门学习  | [Mybatis入门学习四](study-guide/Mybatis入门学习四-resultType.md) |
| mybatis-course-resultMap   | mybatis入门学习  | [Mybatis入门学习五](study-guide/Mybatis入门学习五-resultMap.md) |
| mybatis-dynamic-sql   | mybatis入门学习  | [Mybatis入门学习六](study-guide/Mybatis入门学习六-dynamicSql.md) |
| mybatis-main-properties   | mybatis入门学习  | [Mybatis入门学习七](study-guide/Mybatis入门学习七-properties.md) |
| mybatis-plugins-pageHelper   | mybatis入门学习  | [Mybatis入门学习八](study-guide/mybatis分页插件.md) |
| mybatis-dynamic-sql-advanced   | mybatis入门学习  | [Mybatis入门学习九](study-guide/Mybatis动态SQL高级用法.md) |

### 2.2目录结构说明

简单以`mybatis-course-start `模块构建整体目录树，以此来介绍项目的一个基础框架。

```markdown
│  .gitignore
│  JavaNotesCode.iml
│  pom.xml
│  
├─.idea
│                                              
├─mybatis-course-start 
│  │  pom.xml
│  │  
│  ├─src
│  │  ├─main
│  │  │  ├─java
│  │  │  │                  
│  │  │  └─resources
│  │  │          
│  │  └─test
│  │      ├─java
│  │      │              
│  │      └─resources
│                          
└─study-guide
```

首先项目中用到`Maven`聚合模块，`JavaNotesCode`是根目录，`pom.xml`是父工程的`Maven`依赖，各种模块的依赖都会在父模块的`dependencyManagement`进行统一依赖管理。

```xml
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>JavaNotesCode</artifactId>
        <groupId>com.luojay</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>mybatis.course.util</artifactId>
    <name>mybatis-course-util</name>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

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
    <build>
        <!--指定资源文件-->
        <resources>
            <!--指定跟dao同包的xml文件打包到类路径下-->
            <resource>
                <directory>src/main/java</directory>
                <!--包括目录下的.propertis .xml 文件都会被扫描到-->
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>false</filtering>
            </resource>
            <!--同时指定资源文件夹的所有文件打包到类路径下-->
            <resource>
                <directory>src/main/resources</directory>
            </resource>
        </resources>
    </build>
</project>
```

如果单独下载某个模块，之后不知道依赖的版本号可以在父工程的`pom.xml`找到对应的版本号，添加进子模块理论上可以完成单模块的代码学习。

## 3.下载和导入

### 3.1项目下载

```bash
git clone https://github.com/CodeLuoJay/JavaNotesCode.git
```

### 3.2项目导入

在IDE中导入，以IDEA为默认的开发工具，导入后，右击模块Maven-> Add As  Maven Project 即可以完成单个模块导入

