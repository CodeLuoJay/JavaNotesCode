package com.luojay;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.junit.Test;

/**
 * @author LuoJay
 * @version V1.0.0
 * 测试IdUtil工具类使用
 * @date 2021/5/9 1:32
 */
public class TestIdUtilExample {

    /**
     * 测试雪花算法生成ID(分布式系统经常用到)
     */
    @Test
    public void testSnowFlakeId(){
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        long id = snowflake.nextId();
        System.out.println(id);
    }
    /**
     * 测试UUID
     */
    @Test
    public void testUUID(){
        //生成的UUID是带-的字符串，类似于：a5c8a5e8-df2b-4706-bea4-08d0939410e3
        String uuid = IdUtil.randomUUID();
        System.out.println("uuid: "+uuid);
        //生成的是不带-的字符串，类似于：b17f24ff026d40949c85a24f4f375d42
        String simpleUUID = IdUtil.simpleUUID();
        System.out.println("simpleUUID: "+simpleUUID);
    }


}
