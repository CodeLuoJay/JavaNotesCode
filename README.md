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

### 2.2目录结构说明

简单以`mybatis-course`模块构建整体目录树，以此来介绍项目的一个基础框架。

```markdown
│  .gitignore
│  JavaNotesCode.iml
│  pom.xml
│  
├─.idea
│                                              
├─mybatis-course
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
	<!--工程的父模块-->
    <groupId>com.luojay</groupId>
    <artifactId>JavaNotesCode</artifactId>
    <version>1.0-SNAPSHOT</version>
    <modules>
        <module>java-tool-lombok</module>
        <module>mybatis-course</module>
        <module>java-tool-hutool</module>
    </modules>
    <!--2.此标签内定义版本号-->
    <properties>
        <junit.version>4.12</junit.version>
        <lombok.version>1.18.12</lombok.version>
        <mybatis.version>3.5.1</mybatis.version>
        <mysql.version>5.1.9</mysql.version>
        <hutool.version>5.3.7</hutool.version>
    </properties>
    <!--3.依赖管理-->
    <dependencyManagement>
        <dependencies>
            <!--junit-->
            <dependency>
                <groupId>junit</groupId>
                <artifactId>junit</artifactId>
                <version>${junit.version}</version>
            </dependency>
    </dependencyManagement>
```

如果单独下载某个模块，之后不知道依赖的版本号可以在父工程的`pom.xml`找到对应的版本号，添加进子模块理论上可以完成单模块的代码学习。

## 3.下载和导入

### 3.1项目下载

```bash
git clone https://github.com/CodeLuoJay/JavaNotesCode.git
```

### 3.2项目导入

在IDE中导入，以IDEA为默认的开发工具，导入后，右击模块Maven-> Add As  Maven Project 即可以完成单个模块导入

