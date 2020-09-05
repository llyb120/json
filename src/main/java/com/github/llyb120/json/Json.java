package com.github.llyb120.json;


import com.github.llyb120.json.lambda.GeneratorFunc;
import com.github.llyb120.json.lambda.MapGeneratorFunc;
import com.github.llyb120.json.core.JsonEncoder;
import com.github.llyb120.json.core.JsonParser;
import com.github.llyb120.json.core.StringifyOption;
import com.github.llyb120.json.reflect.ClassInfo;
import com.github.llyb120.json.reflect.ClassType;
import com.github.llyb120.json.reflect.ReflectUtil;
import org.bson.Document;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

import static com.github.llyb120.json.reflect.ReflectUtil.*;


public final class Json {
    public static final Undefined undefined = new Undefined();
    public static final String $match = "$match";
    public static final String $project = "$project";
    public static final String $sort = "$sort";
    public static final String $expand = "...";

    private Json(){}

    /**
     * json
     **/
//    public <T> T to(Class<T> clz) {
//        return Json.cast(this, clz);
//    }
//
//    public <T> T to(TypeReference<T> reference){
//        return Json.cast(this, reference);
//    }
    private static int _dealMap(Map obj, Object key, Object value){
        //展开key为map的元素
        if(key instanceof Map){
            obj.putAll((Map)key);
            return 1;
        }
        //略过key为null或者undefined的键值对
        if(key == null || key instanceof Undefined){
            return 2;
        }
        //key为生成器
        if(key instanceof Generator){
            for(Object o : (Generator) key)  {
                _dealMap(obj, o, null);
            }
            return 1;
        }
        //如果value为undefined，则无效
        if (value instanceof Undefined) {
            return 2;
        }
        if(key instanceof CharSequence){
            obj.put(key.toString(), value);
            return 2;
        }
        List<Class> strClz = a(Integer.class, Long.class, Double.class, Float.class);
        if(strClz.contains(key.getClass())){
            obj.put(key.toString(), value);
            return 2;
        }
        //被当作实体
        obj.putAll(ro(key));
        return 1;
//        if (objects[i] instanceof Map) {
//            json.putAll((Map) objects[i]);
//            i--;
//            continue;
//        }
//        //没这个元素了，则直接gg
//        if (i + 1 >= objects.length) {
//            continue;
//        }
//        //如果key为null或undefined 则视为无效
//        if (objects[i] == undefined || objects[i] == null) {
////                i--;
//            continue;
//        }
//        //key为生成器
//        if(objects[i] instanceof Generator){
//            for(Object o : (Generator)objects[i])  {
//
//            }
//        }
//
//        Object value = objects[i + 1];
//        //如果value为undefined，则无效
//        if (value == undefined) {
//            continue;
//        }
//        json.put((String) objects[i], value);
    }

    public static Obj o(Object... objects) {
        Obj json = new Obj();
        int step;
        for (short i = 0; i < objects.length; ) {
            //当key为一个map的时候，ooo会自动展开该key
            Object key = objects[i];
            Object value = null;
            if(i + 1 < objects.length){
                value = objects[i + 1];
            }
            step = _dealMap(json, key, value);
            i += step;
        }
        return json;
    }

    /**
     * 引用obj
     *
     * @return
     */
    public static Obj ro(Object map) {
        if (map == null) {
            return o();
        }
        if (map instanceof Map) {
            return new Obj().map((Map) map);
        } else {
            Obj val = cast(map, Obj.class);
            if (val == null) {
                return null;
            }
            return val;
        }
    }

    @Deprecated
    public static Obj ooo(Object... objects) {
        return o(objects);
    }

    public static Document bo(Object... objects) {
        Document document = new Document(){
            @Override
            public Object put(String key, Object value) {
                return super.put(key, castBson(value));
            }

            @Override
            public void putAll(Map<? extends String, ?> map) {
                super.putAll(castBson(map));
            }
        };
        document.putAll(o(objects));
        return document;
    }

    @Deprecated
    public static Document booo(Object... objects) {
        return bo(objects);
    }

    public static List<Document> ba(Object... objects) {
        List list = new ArrayList(){
            @Override
            public Object set(int index, Object element) {
                return super.set(index, Json.castBson(element));
            }

            @Override
            public boolean add(Object o) {
                return super.add(Json.castBson(o));
            }

            @Override
            public void add(int index, Object element) {
                super.add(index, Json.castBson(element));
            }

            @Override
            public boolean addAll(Collection c) {
                return super.addAll(Json.castBson(c));
            }

            @Override
            public boolean addAll(int index, Collection c) {
                return super.addAll(index, Json.castBson(c));
            }
        };
        list.addAll(a(objects));
        return list;
//        return castBson(a(objects));
    }

    @Deprecated
    public static List<? extends Document> baaa(Object... objects) {
        return castBson(aaa(objects));
    }


    private static void _dealList(List arr, Object object){
        if (object == null) {
            arr.add(object) ;
        } else if(object instanceof Undefined){
            return;
        } else if (object instanceof Iterable) {
            ((Iterable) object).forEach(e -> {
                if (!(e instanceof Undefined)) {
                    arr.add(e);
                }
            });
        } else if (object.getClass().getSimpleName().contains("[]")) {
            //数组的情况
            int len = Array.getLength(object);
            for (int i = 0; i < len; i++) {
                Object obj = Array.get(object, i);
                if(!(obj instanceof Undefined)){
                   arr.add(obj);
                }
            }
        } else {
            arr.add(object);
        }
    }

    /**
     * 该函数为a的升级版，相比较于普通的a，该函数有额外的行为
     * 1 null也被视作undefined
     * 2 如果是一个可以遍历的集合，则会被展开放入
     * 3 如果是一个数组，也会展开放入
     * 4 展开行为只存在于第一层
     *
     * @param objects
     * @return
     */
    @Deprecated
    public static Arr aaa(Object... objects) {
        Object[] args = Arrays.stream(objects)
                .flatMap(e -> {
                    if (e instanceof Collection) {
                        return ((Collection) e).stream();
                    } else {
                        return Arrays.asList(e).stream();
                    }
                })
                .toArray();
        return a(args);
//        Arr arr = new Arr();
//        for (Object object : objects) {
//            if (object == null || object == undefined) {
//                continue;
//            }
//            if(object instanceof Generator){
//                ((Generator) object).forEach(e ->{
//                    _dealList(arr, e);
//                });
//            } else {
//                _dealList(arr, object);
//            }
////            if (object instanceof Iterable) {
////                ((Iterable) object).forEach(arr::add);
////            } else if (object.getClass().getSimpleName().contains("[]")) {
////                //数组的情况
////                arr.addAll(Arrays.asList(object));
////            } else {
////                arr.add(object);
////            }
//        }
//        return arr;
    }

    public static <T> Arr<T> a(T... objects) {
        Arr arr = new Arr();
        for (int i = 0; i < objects.length; i++) {
            Object object = objects[i];
            if (object == undefined) {
                continue;
            }
            boolean expand = false;
            if($expand.equals(object)){
                expand = true;
                object = objects[++i];
            }
            if(object instanceof Generator){
                boolean finalExpand = expand;
                ((Generator) object).forEach(e ->{
                    if (e == null) {
                        return;
                    }
                    if(finalExpand){
                        _dealList(arr, e);
                    } else {
                        arr.add(e);
                    }
                });
            } else {
                if(expand){
                    _dealList(arr, object);
                } else {
                    arr.add(object);
                }
            }
        }
        return arr;
    }

    /**
     * 引用列表
     *
     * @param list
     * @return
     */
    public static <T> Arr<T> ra(List<T> list) {
        return new Arr<T>().list(list);
    }



//    public static <T> List<T> la(T ...objects){
//        return (List<T>) a(objects);
//    }
//
//    public static <T> List<T> laaa(Object ...objects){
//        return (List<T>) aaa(objects);
//    }

    public static <T> Generator yield(List<T> source, GeneratorFunc<T> func){
        return new Generator(source, func);
    }
    public static <T> Generator yield(T[] source, GeneratorFunc<T> func){
        return new Generator(Arrays.asList(source), func);
    }

    public static <X,Y> Generator yield(Map<X,Y> source, MapGeneratorFunc<X,Y> func){
        return new Generator(source, func);
    }

    public static Arr toTree(Collection<? extends Map> list, String parentKey, String childKey) {
        return toTree(list,parentKey,childKey,null);
    }

    public static Arr toTree(Collection<? extends Map> list, String parentKey, String childKey, String sortKey) {
        Map<String, Obj> map = new HashMap();
        for (Map map1 : list) {
            Object key = map1.get(childKey);
            if (key == null) {
                continue;
            }
            map.put((String) key, o(map1));
        }
        Arr ret = a();
        for (Obj map1 : map.values()) {
            Object key = map1.get(parentKey);
            if (key == null) {
                ret.add(map1);
                continue;
            }
            Obj par = map.get(key);
            if (par == null) {
                continue;
            }
            Arr<? super Object> child = par.aa("children");
            child.add(map.get(map1.get(childKey)));
        }
        if (sortKey != null) {
            for (Obj value : map.values()) {
                Collections.sort(value.aa("children"), (a,b) -> Integer.compare(((Obj)a).ii(sortKey), ((Obj)b).ii("sort")));
            }
            Collections.sort(ret, (a,b) -> Integer.compare(((Obj)a).ii(sortKey), ((Obj)b).ii("sort")));
        }
//        for (Json json : list) {
//            Json par = map.o(json.s(parentKey));
//            if (par == null) {
//                ret.add(json);
//                continue;
//            }
//            Json child = par.a("children");
//            if (child == null) {
//                child = a();
//                par.set("children", child);
//            }
//            child.add(json);
//        }
        return ret;
    }

//    public static <T> T get(List list, String path){
//        return _get(list,path);
//    }
//    public static <T> T get(Map map, String path){
//        return _get(map,path);
//    }
//
//    private static <T> T _get(Object map, String path){
//        Object item = map;
//        for (String s : path.split("\\.")) {
//            if(item instanceof Map){
//                if(!((Map) item).containsKey(s)){
//                    return null;
//                }
//                item = ((Map) item).get(s);
//            } else if(item instanceof List){
//                try{
//                    int i = Integer.parseInt(s);
//                    if(((List) item).size() > i){
//                        item = ((List) item).get(i);
//                    } else {
//                        return null;
//                    }
//                } catch (Exception e){
//                    return null;
//                }
//            } else {
//                return null;
//            }
//        }
//        return (T) item;
//    }

    public static String stringify(Object obj) {
        return stringify(obj, null);
    }
    public static String stringify(Object obj, StringifyOption option) {
        return new JsonEncoder(option).stringify(obj);
    }

    public static <T> T parse(String str) {
//        var s = new com.alibaba.fastjson.TypeReference<List<List>>(){};
//        var t = new TypeReference<List<List>>(){};
        return (T) new JsonParser(str).parse();
    }

    public static <T> T parse(byte[] bs) {
        return parse(new String(bs));
    }

    private static boolean canEq(Object o){
        ClassType t1 = getType(o);
        return t1 == ClassType.COLLECTION || t1 == ClassType.BEAN || t1 == ClassType.ARRAY || t1 == ClassType.MAP;
    }
    public static boolean eq(Object o1, Object o2){
        boolean flag  = Objects.equals(o1, o2);
        if(flag){
            return true;
        }
        if(canEq(o1) && canEq(o2)){
            Map m1 = ReflectUtil.getValues(o1);
            Map m2 = ReflectUtil.getValues(o2);
            HashSet<String> set = new HashSet<>();
            set.addAll(m1.keySet());
            set.addAll(m2.keySet());
            for (String o : set) {
                if(!eq(m1.get(o), m2.get(o))){
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }


//    public static Obj2 toObj(Object object){
//       return cast(object, Obj2.class);
//    }

//    public static Arr toArr(Object object){
//        return cast(object, Arr.class);
//    }

//    public static <T> T parse(String str, Class<T> clz){
//    }


//    public static <T> T parse(String str){
//        Arr arr = Json.parse("fuck");
//    }


    /******************************************************/

//    public Object toBson() {
//        return castBson(this);
//    }
//    protected static Object fromBson(Object object) {
//        if (object instanceof Collection) {
//            Arr arr = a();
//            Collection list = (Collection) object;
//            for (Object o : list) {
//                arr.add(fromBson(o));
//            }
//            ;
//            return arr;
//        } else if (object instanceof Map) {
//            Obj obj = o();
//            Map<Object, Object> map = (Map) object;
//            for (Map.Entry<Object, Object> entry : map.entrySet()) {
//                obj.put((String) entry.getKey(), fromBson(entry.getValue()));
//            }
//            return obj;
//        } else {
//            return object;
//        }
//    }

    protected static <T> T castBson(Object object) {
        if (object == null) return (T) object;
        if (object.getClass().getName().startsWith("org.bson")) {
            return (T) object;
        }
        if (object instanceof Map) {
            Map document = new Document();//newInstance("org.bson.Document");
            for (Object o : ((Map) object).entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                document.put((String) entry.getKey(), castBson(entry.getValue()));
            }
            return (T) document;
        } else if (object instanceof Collection) {
            List list = new ArrayList();
            for (Object o : ((Collection) object)) {
                list.add(castBson(o));
            }
            return (T) list;
        } else if (object.getClass().getSimpleName().contains("[]")) {
            //byte对象不进行转换
            if (object.getClass().getSimpleName().equals("byte[]")) {
                return (T) object;
            } else {
                List list = new ArrayList();
                int len = Array.getLength(object);
                for (int i = 0; i < len; i++) {
                    list.add(castBson(Array.get(object, i)));
                }
                return (T) list;
            }
        } else if(object instanceof Enum) {
            return (T) ((Enum) object).name();
        }
        if (object.getClass().getName().startsWith("java.")) {
            return (T) object;
        } else {
            //尝试转换为Document
            Map document = new Document();//newInstance("org.bson.Document");
            //字段
            ClassInfo info = ReflectUtil.getClassInfo(object.getClass());
            info.fields.forEach((k, v) -> {
                Object value = castBson(getValue(object, v));//v.get(object));
                document.put(k, value);
            });
            //getter
            info.getters.forEach((k, v) -> {
                Object value = null;
                try {
                    value = castBson(v.invoke(object));
                } catch (IllegalAccessException | InvocationTargetException e) {
                }
                document.put(k, value);
            });
//            MethodAccess ma = MethodAccess.get(object.getClass());
//            int i = 0;
//            Class[][] params = ma.getParameterTypes();
//            for (String methodName : ma.getMethodNames()) {
//                if (methodName.length() > 3) {
//                    if (methodName.startsWith("get") && CharUtil.isLetterUpper(methodName.charAt(3)) && params[i].length == 0) {
//                        document.put(methodName.substring(3, 4).toLowerCase() + methodName.substring(4), castBson(ma.invoke(object, i)));
//                    }
//                }
//                i++;
//            }
            return (T) document;
        }
    }


    public static <T> T cast(Object source, TypeReference<T> typeReference) {
        return cast(source, typeReference.getType());
    }

    public static <T> T cast(Object source, Class<T> clz) {
        return cast(source, (Type) clz);
    }

    public static <T> T cast(Object source, Type targetType) {
        try {
            return _cast(source, targetType);
        } catch (Exception e) {
            return null;
        }
    }

    private static <T> T _cast(Object source, Type targetType) {
        if (source == null) {
            return null;
        }
        Class<?> type = source.getClass();
        //基本类型
        if (targetType == Object.class) {
            return (T) source;
        }
        if (targetType == String.class) {
            if (source instanceof String) {
                if (((String) source).equalsIgnoreCase("null")) {
                    return null;
                }
                return (T) source;
            } else {
                return (T) String.valueOf(source);
            }
        }
        if (source instanceof String){
            if(targetType == Arr.class || targetType == Obj.class){
                return Json.parse((String) source);
            }
            else if(targetType == ObjectId.class){
                return (T) new ObjectId((String) source);
            }
        }
        if (targetType == Boolean.class || targetType == boolean.class) {
            if (source instanceof Boolean) {
                return (T) source;
            } else {
                //尝试本身转换
                try {
                    String str = String.valueOf(source);
                    if (str.equalsIgnoreCase("true")) {
                        return (T) Boolean.TRUE;
                    } else if (str.equalsIgnoreCase("false")) {
                        return (T) Boolean.FALSE;
                    } else {
                        //尝试使用数字
                        BigDecimal decimal = new BigDecimal(String.valueOf(source));
                        return (T) (decimal.intValue() != 0 ? Boolean.TRUE : Boolean.FALSE);
                    }
                } catch (Exception e) {
                }
                //啥都没有，那就false吧
                return (T) Boolean.FALSE;
            }
        }
        if (targetType == Integer.class || targetType == int.class) {
            if (source instanceof Integer) {
                return (T) source;
            } else {
                try {
                    return (T) (Integer) Integer.parseInt(String.valueOf(source));
                } catch (Exception e) {
                    return (T) (Integer) 0;
                }
            }
        }
        if (targetType == Long.class || targetType == long.class) {
            if (source instanceof Long) {
                return (T) source;
            } else {
                try {
                    return (T) (Long) Long.parseLong(String.valueOf(source));
                } catch (Exception e) {
                    return (T) (Long) 0L;
                }
            }
        }
        //bson的情况
        if(targetType == BigDecimal.class && type == Decimal128.class){
            return (T) ((Decimal128) source).bigDecimalValue();
        }

        //可以返回自身
        Class clz = null;
        if (targetType instanceof ParameterizedType) {
            clz = (Class) ((ParameterizedType) targetType).getRawType();
        } else if (targetType instanceof Class) {
            clz = (Class) targetType;
        }
        if (clz == null) {
            return null;
        }
        if (clz == source.getClass()) {
            return (T) source;
        }
        //枚举
        Object[] enums = clz.getEnumConstants();
        if(enums != null && source instanceof CharSequence){
            for (Object anEnum : enums) {
                Enum em = (Enum) anEnum;
                if(em.name().equalsIgnoreCase(((CharSequence) source).toString())){
                    return (T) em;
                }
            }
            return null;
        }

        //目标为List
        if (List.class.isAssignableFrom(clz)) {
//            if(Arr.class.isAssignableFrom(clz)){
//                clz = Arr.class;
//            }
//            else
            if (List.class == clz) {
                clz = (Class<T>) ArrayList.class;
            }
            Type[] types = null;
            if (targetType instanceof ParameterizedType) {
                ParameterizedType p = (ParameterizedType) targetType;
                types = p.getActualTypeArguments();
            }
            List list = (List) newInstance(clz);
            if (type.isArray()) {
                for (int i = 0; i < Array.getLength(source); i++) {
                    list.add(Array.get(source, i));
                }
            } else if (source instanceof List) {
                if (null != types && types.length > 0) {
                    List collection = (List) source;
                    for (Object o : collection) {
                        list.add(cast(o, types[0]));
                    }
                } else {
                    ((List) list).addAll((Collection) source);
                }
            }
            return (T) list;
        }
        //目标为set类型
        if (Set.class.isAssignableFrom(clz)) {
            if (Set.class == clz) {
                clz = (Class<T>) HashSet.class;
            }
            Set list = (Set) newInstance(clz);
            if (type.isArray()) {
                for (int i = 0; i < Array.getLength(source); i++) {
                    list.add(Array.get(source, i));
                }
            } else {
                list.addAll((Collection) source);
            }
            return (T) list;
        }
        if (clz.isArray()) {
            Class<?> arrType = clz.getComponentType();
            Collection collection = (Collection) source;
            Object narr = Array.newInstance(arrType, (collection.size()));
            int i = 0;
            for (Object o : collection) {
                Array.set(narr, i, Json.cast(o, arrType));
                i++;
            }
            return (T) narr;
        }
        //目标为map类型
        if (Map.class.isAssignableFrom(clz)) {
            if (Map.class == clz) {
                clz = (Class<T>) HashMap.class;
            }
            Type[] types = null;
            if (targetType instanceof ParameterizedType) {
                ParameterizedType p = (ParameterizedType) targetType;
                types = p.getActualTypeArguments();
            }
            Map map;
            if (clz == Obj.class) {
                map = new Obj();
            } else {
                map = (Map) newInstance(clz);
            }
            if (source instanceof Map) {
                if (null != types && types.length > 1) {
                    for (Map.Entry<Object, Object> entry : ((Map<Object, Object>) source).entrySet()) {
                        map.put(entry.getKey(), cast(entry.getValue(), (Class<T>) types[1]));
                    }
                } else {
                    ((Map) map).putAll((Map) source);
                }

            } else if (source instanceof Collection) {
                //原数据可以变成map，key为数字，element为值
                int i = 0;
                for (Object o : ((Collection) source)) {
                    map.put(i + "", o);
                    i++;
                }
            } else {
                map.putAll(beanToMap(source));
            }
            return (T) map;
        }
        //把目标当bean转换
        if (source instanceof Map) {
            return (T) mapToBean((Map<Object, Object>) source, clz);
        } else {
            return (T) beanToBean(source, clz);
        }

    }

//    private static Map beanToMap(Object source, Class clz){
//        if(clz == Map.class){
//            clz = HashMap.class;
//        }
//        Object ins = newInstance(clz);
//        FieldAccess fa = FieldAccess.get(clz);
//        for (String fieldName : fa.getFieldNames()) {
//
//        }
//    }




    public static <T> T copy(T source) {
        if (source == null) {
            return null;
        }
        return (T) beanToBean(source, newInstance(source.getClass()));
    }

    private static Map beanToMap(Object source) {
        Map ret = new HashMap();
        ClassInfo classInfo = getClassInfo(source.getClass());
        classInfo.fields.forEach((k, v) -> {
            ret.put(k, getValue(source, v));
        });
        //getter
        classInfo.getters.forEach((k, v) -> {
            try {
                ret.put(k, v.invoke(source));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
//        for (Field declaredField : source.getClass().getDeclaredFields()) {
//            try {
//                ret.put(declaredField.getName(), declaredField.get(source));
//            } catch (IllegalAccessException e) {
//            }
//        }
        return ret;
    }

    private static <T> Object mapToBean(Map<Object, Object> source, Class<T> clz) {
        return mapToBean(source, clz, null);
    }

    private static <T> Object mapToBean(Map<Object, Object> source, Class<T> clz, T ins) {
        if (ins == null) {
            ins = newInstance(clz);
        }
        ClassInfo info = getClassInfo(clz);
        T finalIns = ins;

        info.fields.forEach((k, v) -> {
            Type type = v.field.getGenericType();
            setValue(finalIns, v, cast(source.get(v.name), type));
        });
        //setter
        info.setters.forEach((k, v) -> {
            Type type = v.getGenericParameterTypes()[0];
            Object value = cast(source.get(k), type);
            if (value == null) {
                return;
            }
            try {
                v.invoke(finalIns, value);
            } catch (Exception e) {
//                e.printStackTrace();
            }
        });
//        FieldAccess fa = FieldAccess.get(clz);
////        Class[] types = fa.getFieldTypes();
//        int i = 0;
//        for (String fieldName : fa.getFieldNames()) {
//            try {
//                Type type = clz.getDeclaredField(fieldName).getGenericType();
//                fa.set(ins, i, cast(source.get(fieldName), type));
//            } catch (Exception e) {
//            }
//            i++;
//        }
        return ins;
    }

    private static Object beanToBean(Object source, Class clz) {
        Map map = beanToMap(source);
        return mapToBean(map, clz);
    }

    private static <T> T beanToBean(Object source, T ins) {
        Map map = beanToMap(source);
        return (T) mapToBean(map, (Class<T>) ins.getClass(), ins);
    }
}
