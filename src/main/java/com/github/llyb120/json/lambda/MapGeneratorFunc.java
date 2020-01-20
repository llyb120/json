package com.github.llyb120.json.lambda;

public interface MapGeneratorFunc<X,Y> {
    Object call(X k, Y v) throws Exception;
}
