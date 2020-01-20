package com.github.llyb120.json.reflect;

import java.lang.reflect.Field;

public class FieldInfo {
    public Field field;
    public String name;
    public long offset;

    public FieldInfo(Field field){
        this.field = field;
        this.name = field.getName();
        this.offset = ReflectUtil.unsafe.objectFieldOffset(field);
    }

}
