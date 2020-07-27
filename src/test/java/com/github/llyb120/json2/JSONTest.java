package com.github.llyb120.json2;

import cn.hutool.core.io.FileUtil;
import org.junit.Test;

/**
 * @Author: Administrator
 * @Date: 2020/7/23 17:44
 */
public class JSONTest {

    @Test
    public void test(){
        String json = FileUtil.readString("../../test/test.json", "UTF-8");
        JSON2Parser parser = new JSON2Parser(json);
        Obj2 obj = parser.parse();

        for (int i = 0; i < 10; i++) {
            obj.set(String.format("ri.ding.%d.fkfk", i), i);
        }
//        obj.ss("*.fkfk")
        System.out.println(
            obj.toString()
        );
        int d = 3;
    }
}
