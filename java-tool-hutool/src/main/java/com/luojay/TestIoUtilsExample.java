package com.luojay;

import cn.hutool.core.io.IoUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 *
 * @author luojay
 * 测试hutool的文件IOUtil工具类
 */
public class TestIoUtilsExample
{
    public static void main( String[] args ) throws Exception {
        // testFileCopy();
        testSimpleFileCopy();
        // testFileCopyByIOUtils();
    }

    /**
     * 测试文件拷贝
     */
    public static void testFileCopy() throws Exception {
        File inFile = new File("F:\\JavaLearingCode\\java-tool-hutool\\src\\main\\resources\\testFile.txt");
        FileInputStream is = new FileInputStream(inFile);
        File outFile = new File("F:\\JavaLearingCode\\java-tool-hutool\\src\\main\\resources\\testFileCopy.txt");
        FileOutputStream os = new FileOutputStream(outFile);

        //  开始读和写流,需要考虑文件的大小是否超过int的最大值，因为write是以int最大值写进去，所以需要定义一个缓冲区，每次读取固定缓冲区大小的内容
        //  怎么知道is读到文件末尾呢？当read()方法读取到末尾时，会返回-1
        //  所以可以通过循环的方式先读进缓冲区，然后把缓冲区的内容写出去新的文件，直到读到末尾时才结束循环【不知道读多少次】
        int len = 0;
        byte[] buffer = new byte[1024];
        while(true){
            //  不足1024就只读剩下,最后一次一定读不到内容才返回-1
            len = is.read(buffer);
            if(len == -1){
                break;
            }
            os.write(buffer,0,len);
        }
        is.close();
        os.close();
    }

    /**
     * 简化后测试文件拷贝
     */
    public static void testSimpleFileCopy() throws Exception {
        FileInputStream is = new FileInputStream(new File("F:\\JavaLearingCode\\java-tool-hutool\\src\\main\\resources\\testFile.txt"));
        FileOutputStream os = new FileOutputStream(new File("F:\\JavaLearingCode\\java-tool-hutool\\src\\main\\resources\\testSimpleFileCopy.txt"));

        int len;
        byte[] buffer = new byte[1024];
        while((len = is.read(buffer))!=-1){
            os.write(buffer,0,len);
        }
        is.close();
        os.close();
    }

    /**
     * 使用hutool的文件拷贝
     * @throws Exception
     */
    public static void testFileCopyByIOUtils() throws Exception {
        File inFile = new File("F:\\JavaLearingCode\\java-tool-hutool\\src\\main\\resources\\testFile.txt");
        FileInputStream is = new FileInputStream(inFile);
        File outFile = new File("F:\\JavaLearingCode\\java-tool-hutool\\src\\main\\resources\\testFileCopyHutool.txt");
        FileOutputStream os = new FileOutputStream(outFile);
        IoUtil.copy(is,os,1024);
    }

}
