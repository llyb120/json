package com.github.llyb120.json2;

/**
 * @Author: Administrator
 * @Date: 2020/7/27 15:46
 */
public class ErrorPathException extends RuntimeException{

    public ErrorPathException(String path){
        super(String.format("路径 %s 必须为一个直接的路径", path));
    }

}
