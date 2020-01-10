package com.github.llyb120.json;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClassInfo {
    public Map<String, Method> getters = new ConcurrentHashMap<>();
    public Map<String, Method> setters = new ConcurrentHashMap<>();
    public Map<String,FieldInfo> fields = new ConcurrentHashMap<>();

}
