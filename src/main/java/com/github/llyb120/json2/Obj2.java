package com.github.llyb120.json2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.github.llyb120.json.Json.cast;

/**
 * @Author: Bin
 * @Date: 2020/7/27 9:35
 */
public class Obj2 {

    Map<String, Object> map;

    //提前量
    private LinkedList<String> prefixes = new LinkedList<>();

    Obj2(Map map) {
        this.map = map;
    }

    enum PathType {
        //直接取值
        DIRECT,
        //正则
        REGEX,
        //通配符
        SIMPLE_REGEX,
    }

    /**
     * 获得路径的类型
     *
     * @param path
     * @return 路径的类型
     */
    PathType getPathType(String path) {
        if (path.charAt(0) == '/') {
            return PathType.REGEX;
        } else if (path.contains("*")) {
            return PathType.SIMPLE_REGEX;
        } else {
            return PathType.DIRECT;
        }
    }

    /**
     * 字符串相关
     */
    public String s(String path) {
        return getString(path);
    }

    public String s(String path, String dft) {
        return getString(path, dft);
    }

    public String ss(String path) {
        return getFixedString(path);
    }

    public String ss(String path, String dft) {
        return getFixedString(path, dft);
    }

    public String sl(String path) {
        return "";
    }

    public String getString(String path) {
        return getString(path, "");
    }

    public String getString(String path, String dft) {
        return cast(get(path, dft), String.class);
    }

    public String getFixedString(String path) {
        return getFixedString(path, "");
    }

    public String getFixedString(String path, String dft) {
        String str = getString(path, dft);
        PathType type = getPathType(path);
        if (type == PathType.DIRECT) {
            set(path, str);
        }
        return str;
    }


    /**
     * 通用取
     *
     * @param path 路径
     * @param dft  默认值
     * @return
     */
    public Object get(String path, Object dft) {
        path = handlePrefix(path);
        PathType type = getPathType(path);
        String reg;
        switch (type) {
            case DIRECT:
                //直接取值
                for (String s : map.keySet()) {
                    if (s.equalsIgnoreCase(path)) {
                        return map.get(s);
                    }
                }
                break;

            case SIMPLE_REGEX:
                //通配符
                reg = simpleRegToReg(path);//path.replaceAll("\\*", ".+");
                for (String s : map.keySet()) {
                    if (s.matches(reg)) {
                        return map.get(s);
                    }
                }
                break;

            case REGEX:
                //正则表达式
                reg = path.substring(1);
                for (String s : map.keySet()) {
                    if (s.matches(reg)) {
                        return map.get(s);
                    }
                }
                break;

        }
        return dft;
    }

    public Object get(String path) {
        return get(path, null);
    }

    public Obj2 set(String path, Object value) {
        path = handlePrefix(path);
        PathType type = getPathType(path);
        if (type != PathType.DIRECT) {
            throw new ErrorPathException(path);
        }
        map.put(path, value);
        return this;
    }

    String handlePrefix(String path) {
        if(path.charAt(0) == '/'){
            return path;
        }
        if(prefixes.isEmpty()){
            return path;
        }
        return String.join(".", prefixes) + "." + path;
//        if ("".equals(prefix)) {
//            return path;
//        }
//        PathType prefixType = getPathType(prefix);
//        PathType pathType = getPathType(path);
//        if (prefixType == PathType.REGEX) {
//            if (pathType == PathType.REGEX) {
//                return prefix + path.substring(1);
//            } else {
//                //两个都要处理成正则
//                if (pathType == PathType.SIMPLE_REGEX) {
//                    return prefix + simpleRegToReg(path);
//                } else {
//                    return prefix + directToReg(path);
//                }
//            }
//        } else if (prefixType == PathType.SIMPLE_REGEX) {
//            if (pathType == PathType.REGEX) {
//                return "/" + simpleRegToReg(prefix) + path.substring(1);
//            } else {
//                return prefix + path;
//            }
//        } else {
//            if (pathType == PathType.REGEX) {
//                return "/" + directToReg(prefix) + path.substring(1);
//            } else {
//                return prefix + path;
//            }
//        }
    }

    /**
     * 修复simplereg的表达式
     *
     * @param simpleReg
     * @return
     */
    String simpleRegToReg(String simpleReg) {
        return directToReg(simpleReg)
            .replaceAll("\\*", ".+");
    }

    /**
     * 修复direct的表达式
     *
     * @param direct
     * @return
     */
    String directToReg(String direct) {
        return direct
            .replaceAll("\\.", "\\\\.");
    }

    /**
     * 设置提前量，在取值和赋值的时候会加上
     *
     * @return
     */
    public Obj2 enter(String prefix) {
        this.prefixes.addLast(prefix);
        return this;
    }

    /**
     * 无参数，清空prefix
     *
     * @return
     */
    public Obj2 exit() {
        if(!this.prefixes.isEmpty()){
            this.prefixes.removeLast();
        }
        return this;
    }

    public Obj2 clearPath(){
        this.prefixes.clear();
        return this;
    }


    @Override
    public String toString() {
        JSON2Encoder encoder = new JSON2Encoder(this);
        return encoder.encode();
    }
}
