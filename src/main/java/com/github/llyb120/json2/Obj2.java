package com.github.llyb120.json2;

import java.util.Map;

/**
 * @Author: Bin
 * @Date: 2020/7/27 9:35
 */
public class Obj2 {

    Map<String, Object> map;

    Obj2(Map map){
        this.map = map;
    }

    public String s(String path){
        return "";
    }

    public String ss(String path){
        return "";
    }

    public String sl(String path){
        return "";
    }

    public Obj2 set(String path, Object value){
        map.put(path, value);
        return this;
    }


    @Override
    public String toString() {
        JSON2Encoder encoder = new JSON2Encoder(this);
        return encoder.encode();
    }
}
