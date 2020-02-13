package com.github.llyb120.json.reflect;

import com.github.llyb120.json.Util;
import com.github.llyb120.json.reflect.ClassInfo;
import com.github.llyb120.json.reflect.FieldInfo;
import sun.misc.Unsafe;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.*;

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
            return clz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                return (T) unsafe.allocateInstance(clz);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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
            return info.field.get(ins);
//            return unsafe.getObject(ins, info.offset);
        } catch (Exception e){
            return null;
        }
    }

    public static void setValue(Object ins, FieldInfo info, Object value){
        try {
            info.field.set(ins, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
//        if(value instanceof Integer){
//            unsafe.putInt(ins, info.offset, (int) value);
//        } else {
//            unsafe.putObject(ins, info.offset, value);
//        }
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

            for (Field field : clzz.getDeclaredFields()) {
                if (Modifier.isPublic(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())){
                    field.setAccessible(true);
                    String name = field.getName();
                    if(!info.fields.containsKey(name)) {
                        info.fields.put(name, new FieldInfo(field));
                    }
                }
            }
            for (Method method : clzz.getDeclaredMethods()) {
                if(method.getName().equals("getClass")){
                    continue;
                }
                if (Modifier.isPublic(method.getModifiers()) && !Modifier.isStatic(method.getModifiers())) {
                    String name = method.getName();
                    method.setAccessible(true);
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

            clzz = clzz.getSuperclass();
        }

        return info;
    }

    public static ClassType getType(Object object){
        if (object == null) {
            return ClassType.NULL;
        }
        if(object instanceof Collection){
            return ClassType.COLLECTION;
        }
        if(object instanceof Map){
            return ClassType.MAP;
        }
        if(object instanceof String){
            return ClassType.STRING;
        }
        if(object instanceof Long){
            return ClassType.LONG;
        }
        if(object instanceof Boolean){
            return ClassType.BOOLEAN;
        }
        if(object instanceof Integer){
            return ClassType.INTEGER;
        }
        if(object instanceof Double){
            return ClassType.DOUBLE;
        }
        if(object instanceof Float){
            return ClassType.FLOAT;
        }
        if(object instanceof Date){
            return ClassType.DATE;
        }
        if(object instanceof BigDecimal){
            return ClassType.BIG_DECIMAL;
        }
        Class clz = object.getClass();
        if(clz.getSimpleName().contains("[]")){
            return ClassType.ARRAY;
        }

        return ClassType.BEAN;
    }

    public static Map<String,Object> getValues(Object ins){
            HashMap<String, Object> result = new HashMap<>();
        ClassType classType = getType(ins);
        if(classType == ClassType.COLLECTION){
            int i = 0;
            for (Object in : ((Collection) ins)) {
                result.put(String.valueOf(i++), in);
            }
        }
        if(classType == ClassType.MAP){
           result.putAll((Map<? extends String, ?>) ins);
           return result;
        }
        if(classType == ClassType.ARRAY){
            int len = Array.getLength(ins);
            for (int i = 0; i < len; i++) {
                result.put(String.valueOf(i), Array.get(ins,i));
            }
            return result;
        }
        if(classType == ClassType.BEAN){
            ClassInfo classInfo = getClassInfo(ins.getClass());
            classInfo.fields.forEach((k,v) -> {
                Object value = null;
                try{
                    value = v.field.get(ins);
                } catch (Exception e){
                    e.printStackTrace();
                }
                result.put(k, value);
            });
            classInfo.getters.forEach((k,v) -> {
                Object value = null;
                try{
                    value = v.invoke(ins);
                } catch (Exception e){
                    e.printStackTrace();
                }
                result.put(k, value);
            });
            return result;
        }
        return result;
    }
}
