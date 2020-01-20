package com.github.llyb120.json.lambda;

import java.util.Map;

public interface GeneratorFunc<T> {
    Object call(T t) throws Exception;
}
