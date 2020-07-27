package com.github.llyb120.json2;

/**
 * @Author: Administrator
 * @Date: 2020/7/27 15:50
 */
public class Json2 {

    public static Obj2 parse(Object obj){
        if(obj instanceof String){
            return new JSON2Parser((String)obj).parse();
        } else {
            return null;
        }
    }


}
