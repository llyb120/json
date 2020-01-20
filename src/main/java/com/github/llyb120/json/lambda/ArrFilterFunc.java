package com.github.llyb120.json.lambda;

public interface ArrFilterFunc<T>{
    boolean call(T t);
}