package com.luojay.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LuoJay
 * @version V1.0.0
 * @date 2021/5/16 23:25
 * vo包下自定义参数实体类
 * vo的包命名含义：
 * vo:value object，放一些存储数据的类。比如说提交请求参数，name，age现在想把name，age传给一个service类。
 * vo:view object，从servlet把数据返回给浏览器使用的类，表示显示结果的类。
 * pojo：普通的有set，get方法的java类。普通的java对象Servlet---studentService（addStudent（MyParam param））
 * entity（domain域）：实体类，和数据库中的表对应的类，
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MyParam {
    /** 名字 */
    private String myName;
    /** 电子邮件 */
    private String myEmail;
    /** 年龄 */
    private int myAge;
}
