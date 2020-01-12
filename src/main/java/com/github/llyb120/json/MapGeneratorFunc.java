package com.github.llyb120.json;

public interface MapGeneratorFunc<X,Y> {
    Object call(X k, Y v) throws Exception;
}
