package com.github.llyb120.json.reflect;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClassInfo {
    public Map<String, Method> getters = new ConcurrentHashMap<>();
    public Map<String, Method> setters = new ConcurrentHashMap<>();
    public Map<String,FieldInfo> fields = new ConcurrentHashMap<>();


}
