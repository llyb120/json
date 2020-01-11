package com.github.llyb120.json;

import java.util.Map;

public interface GeneratorFunc<T> {
    Object call(T t) throws Exception;
}
