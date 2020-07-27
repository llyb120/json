package com.github.llyb120.json2;

import cn.hutool.core.io.FileUtil;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @Author: Administrator
 * @Date: 2020/7/23 17:44
 */
public class JSONTest {

    @Test
    public void test(){
        String json = FileUtil.readString("../../test/test.json", "UTF-8");
        Obj2 obj = Json2.parse(json);
        String foo = obj.s("foo.bar");
        assertEquals(foo, "");
        foo = obj.ss("foo.bar", "test");
        assertEquals(foo, "test");
        foo = obj.s("foo.bar");
        assertEquals(foo, "test");
        //正则取值
        foo = obj.s("/foo.+ar");
        assertEquals(foo, "test");
        obj.set("foo.bar", "test2");
        //通配符取值
        foo = obj.s("*.bar");
        assertEquals(foo, "test2");
        try{
            obj.set("*.bar", "test3");
        } catch (Exception e){
            assertNotNull(e);
        }
        for (int i = 0; i < 10; i++) {
            obj.set(String.format("ri.ding.%d.fkfk", i), i);
        }
        assertEquals(obj.s("ri.ding.0.fkfk"), "0");
        System.out.println(
            obj.toString()
        );
        int d = 3;
    }
}
