package com.luojay.entity;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;


/**
 * @author LuoJay
 * @version V1.0.0
 * @date 2021/5/3 18:40
 * 测试Lombok的类使用
 */
@Slf4j
public class UserTest {
    @Test
    public void testUser(){
        User user = new User();
        user.setId("1").setName("luojay").setBirthday(new Date());
        System.out.println(user);
    }


}