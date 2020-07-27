package com.github.llyb120.json2;

/**
 * @Author: Administrator
 * @Date: 2020/7/23 16:49
 */
public class JSONContext {


    enum Type{
        TYPE_ARRAY,
        TYPE_OBJECT,
        TYPE_KEY,
    }

    //类型
    public Type type;

    //词
    public String token;

    //如果为数组，则为当前索引
    public int index;

    public JSONContext(Type type, String token, int index) {
        this.type = type;
        this.token = token;
        this.index = index;
    }

}
