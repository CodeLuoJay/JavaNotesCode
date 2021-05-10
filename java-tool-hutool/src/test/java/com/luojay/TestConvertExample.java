package com.luojay;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RuntimeUtil;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 用来测试hutool中的类型转换
 */
public class TestConvertExample
{
    /**
     * 测试Integer转String
     */
    @Test
    public void testConvertIntegerToString()
    {
       // 以往的类型转换
        int number = 10;
        String strNumber = String.valueOf(number);
        System.out.println("strNumber:"+strNumber);

        // 利用hutool的Convert类来做类型转换
        String toStrNumber = Convert.toStr(number);
        System.out.println("toStrNumber:"+toStrNumber);
    }

    /**
     * 测试String转Integer
     */
    @Test
    public void testConvertStringToNumber()
    {
        // 以往的类型转换

        String strNumber = "中文123";
        // System.out.println("strNumber:"+Integer.valueOf(strNumber));

        // 利用hutool的Convert类来做类型转换
        Integer toNumber = Convert.toInt(strNumber);
        System.out.println("toStrNumber:"+toNumber);

    }

    /**
     * 测试数组类型转换
     */
    @Test
    public void testConvertArrayToString()
    {
        // 以往转字符串[1,2,3,4,5]输出
        long[] longArray = {1,2,3,4,5};
        System.out.println(Arrays.toString(longArray));

        // 利用hutool的Convert类来做类型转换
        System.out.println(Convert.toStr(longArray));
    }

    /**
     * 测试数组类型转换各种类型数组
     */
    @Test
    public void testConvertArray()
    {
        // 以往转字符串[1,2,3,4,5]输出
        String[] stringArray = {"1","2","3","4","5"};
        Integer[] integerArray = new Integer[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            integerArray[i] = Integer.valueOf(stringArray[i]);
        }


        // 利用hutool的Convert类来做Int数组类型转换
        Integer[] toIntArray = Convert.toIntArray(stringArray);
        // 利用hutool的Convert类来做Long数组类型转换
        Long[] toLongArray = Convert.toLongArray(stringArray);
    }

    /**
     * 测试日期类型转换
     */
    @Test
    public void testConvertDate() throws ParseException {

        // 以往字符串转为日期
        String str="2012-12-12";
        Date parse = new SimpleDateFormat("yyyyy-MM-dd").parse(str);
        System.out.println("SimpleDateFormat parse: "+parse);

        //  hutool日期转换
        Date date = Convert.toDate(str);
        System.out.println("hutool convert date: "+date);

        String str1="2012/12/12";
        System.out.println("hutool convert date: "+Convert.toDate(str1));
    } 
    
    /**
     * 测试集合类型转换
     */
    @Test
    public void testConvertCollection() {

        // 字符串数组转集合
        String[] aaa = {"hello", "luojay", "bobby"};
        List<String> strings = Arrays.asList(aaa);
        List<String> objects = (List<String>) Convert.toList(aaa);
        // lambda表达式输出，其实还可以简化为方法引用 System.out::println
        objects.forEach(s -> System.out.println(s));

        // 定义基本类型数组转换
        int[] ccc={21,23};
        // List<Integer> ints = Arrays.asList(ccc);  int[] 无法转换成Integer

        //  hutool可以做到int[] 无法转换成Integer
        List<Integer> objects1 = (List<Integer>) Convert. toList(ccc);
        objects1. forEach(cc->System. out. println(cc));

    }



    /**
     * 测试命令行
     */
    @Test
    public void TestCommand(){
        String str = RuntimeUtil.execForStr("ipconfig");
        System.out.println(str);
    }

    

}
