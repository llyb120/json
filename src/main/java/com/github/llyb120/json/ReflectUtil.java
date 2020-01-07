package com.github.llyb120.json;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 反射帮助类
 */
public class ReflectUtil {

    public static Unsafe unsafe;
    private static Map<Class, ClassInfo> classInfoMap = new WeakHashMap<>();

    static {
        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        unsafeField.setAccessible(true);
        try {
            unsafe = (Unsafe) unsafeField.get(null);
        } catch (IllegalAccessException e) {
        }
    }


    public static <T> T newInstance(Class<T> clz) {
        try {
            return (T) unsafe.allocateInstance(clz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T newInstance(String clzName){
        try {
            Class<?> clz = Class.forName(clzName);
            return (T) newInstance(clz);
        } catch (Exception e) {
            return null;
        }
    }

    public static Object getValue(Object ins, FieldInfo info){
        try{
            return unsafe.getObject(ins, info.offset);
        } catch (Exception e){
            return null;
        }
    }

    public static void setValue(Object ins, FieldInfo info, Object value){
        unsafe.putObject(ins, info.offset, value);
    }


    public static ClassInfo getClassInfo(Class clz){
        synchronized (classInfoMap){
            ClassInfo info = classInfoMap.get(clz);
            if (info == null) {
                info = analyzeClass(clz);
                classInfoMap.put(clz, info);
            }
            return info;
        }
    }

    private static ClassInfo analyzeClass(Class clz){
        ClassInfo info = new ClassInfo();
        Class clzz = clz;
        while(clzz != null && clzz != Object.class){
            clzz = clzz.getSuperclass();
        }
        for (Field field : clz.getDeclaredFields()) {
            if (Modifier.isPublic(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())){
                String name = field.getName();
                if(!info.fields.containsKey(name)) {
                    info.fields.put(name, new FieldInfo(field));
                }
            }
        }
        for (Method method : clz.getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers()) && !Modifier.isStatic(method.getModifiers())) {
                String name = method.getName();
                if(name.startsWith("set") && name.length() > 3 && Util.isLetterUpper(name.charAt(3)) && method.getParameterCount() == 1 && "void".equals(method.getReturnType().getName())){
                    name = name.substring(3,4).toLowerCase() + name.substring(4);
                    if(!info.setters.containsKey(name)){
                        info.setters.put(name, method);
                    }
                }
                if(name.startsWith("get") && name.length() > 3 && Util.isLetterUpper(name.charAt(3)) && method.getParameterCount() == 0){
                    name = name.substring(3,4).toLowerCase() + name.substring(4);
                    if(!info.getters.containsKey(name)){
                        info.getters.put(name, method);
                    }
                }
            }
        }
        return info;
    }
}
