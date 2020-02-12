package com.github.llyb120.json;


import com.github.llyb120.json.lambda.FlexAction;
import com.github.llyb120.json.lambda.MapGeneratorFunc;
import com.github.llyb120.json.selector.JsonPicker;
import org.bson.Document;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.github.llyb120.json.Json.bo;
import static com.github.llyb120.json.Json.cast;

public final class Obj implements Map<String, Object>, Serializable {

    //bson util
    private Map<String, Object> map;

    Obj() {
        map = new LinkedHashMap<>();
    }

    public Map<String, Object> map() {
        return map;
    }

    public Obj map(Map map) {
        this.map = map;
        return this;
    }

    @Override
    public int size() {
        return map().size();
    }

    @Override
    public boolean isEmpty() {
        return map().isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map().containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map().containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return map().get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return map().put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return map().remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        map().putAll(m);
    }

    @Override
    public void clear() {
        map().clear();
    }

    @Override
    public Set<String> keySet() {
        return map().keySet();
    }

    @Override
    public Collection<Object> values() {
        return map().values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return map().entrySet();
    }


    public Obj flex(Object... objects) {
        Obj nobj = Json.o();
        boolean flexed = false;
        for (int i = 0; i < objects.length; i++) {
            Object obj = objects[i];
            boolean readNext = false;
            if (obj instanceof FlexAction) {
                for (Entry<String, Object> entry : this.entrySet()) {
                    if (((FlexAction) obj).canFlex(entry.getKey(), entry.getValue())) {
                        Object fval = ((FlexAction) obj).call(entry.getKey(), entry.getValue());
                        if (fval instanceof Map.Entry) {
                            nobj.put((String) ((Entry) fval).getKey(), ((Entry) fval).getValue());
                        } else {
                            nobj.put(entry.getKey(), fval);
                        }
                    }
                }
                flexed = true;
                continue;
            }
        }
        if (flexed) {
            clear();
            map(nobj);
        }
        return this;
    }


    private  <T> T get(String key, Class<T> clz, boolean multi) {
        if(multi){
                String[] arr = key.split("\\.");
                Obj item = this;
                for (int i = 0; i < arr.length; i++) {
                    if (i == arr.length - 1) {
                        return item.get((arr[i]), clz, false);
                    }
                    item = item.get(arr[i], Obj.class, false);
                    if (item == null) {
                        item = Json.o();
                    }
                }
                return null;
            } else {
                return cast(get(key), clz);
            }
    }

    public <T> T get(String key, Class<T> clz) {
        if(containsKey(key)){
            return get(key, clz, false);
        }
        return get(key, clz, key.contains("."));
    }

    public String s(String key) {
        return get(key, String.class);
    }

//    public String ss(String key){
//        try {
//            return get(key, String.class);
//        } catch (Exception e){
//            return "";
//        }
//    }

    public String s(String key, String defaultValue) {
        String str = s(key);
        if (Util.isEmpty(str)) {
            return defaultValue;
        }
        return str;
    }

    public String ss(String key) {
        String val = s(key);
        if (val == null) {
            val = "";
        }
        put(key, val);
        return val;
    }

    public String ss(String key, String defaultValue) {
        String val = s(key, defaultValue);
        put(key, val);
        return val;
    }

    public Integer i(String k, int defaultValue) {
        try {
            Integer val = i(k);
            if (val == null) {
                val = defaultValue;
            }
            return val;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public Integer i(String k) {
        return get(k, Integer.class);
    }

    public int ii(String k) {
        int ret = 0;
        try {
            ret = get(k, Integer.class);
        } catch (Exception e) {
            ret = 0;
        } finally {
            put(k, ret);
            return ret;
        }
    }

    public boolean b(Object k) {
        Object val = get(k);
        if (val == null) {
            return false;
        }
        return cast(val, Boolean.class);
    }

    public Long l(Object k) {
        Object val = get(k);
        if (val == null) {
            return null;
        }
        return cast(val, Long.class);
    }

    public Long l(Object k, long defaultValue) {
        Long val = l(k);
        if (val == null) {
            return defaultValue;
        }
        return val;
    }

    public Date date(String key, String format) {
        try {
            return new SimpleDateFormat(format).parse(s(key));
        } catch (Exception e) {
        }
        return null;
    }

    public Obj o(String key) {
        return get(key, Obj.class);
    }

    public Obj oo(String key) {
        Obj ret = o(key);
        if (ret == null) {
            ret = Json.o();
            put(key, ret);
        }
        return ret;
    }

    public Arr<? super Object> a(String key) {
        return get(key, Arr.class);
    }

    public Arr<? super Object> aa(String key) {
        Arr ret = a(key);
        if (ret == null) {
            ret = Json.a();
        }
        put(key, ret);
        return ret;
    }


    public Generator yield(MapGeneratorFunc<String,Object> func){
        return Json.yield(this, func);
    }

    public Obj v(String key, Validate validate, String msg, Object... args) {
        Object obj = get(key);
        switch (validate) {
            case NotNull:
                if (obj == null) {
                    Validate.error(msg, args);
                }
                break;

            case NotEmpty:
                obj = s(key, "");
                if (Util.isEmptyIfStr(obj)) {
                    Validate.error(msg, args);
                }
                break;

            case NotBlank:
                obj = s(key, "");
                if (Util.isBlankIfStr(obj)) {
                    Validate.error(msg, args);
                }
                break;
        }

        return this;
    }

    public <T> Arr<T> pick(String path, Class<T> clz){
        return JsonPicker.pick(path, this, clz);
    }

    public Arr pick(String path){
        return JsonPicker.pick(path, this, Object.class);
    }


//    public <T> T toBson() {
//        return castBson(this);
//    }
//
//    public static Obj fromBson(Object object) {
//        return (Obj) Json.fromBson(object);
//    }
//
    //    public Document toBsonDoc(){
//        Document docuemnt = new Document();
//        for (Entry<String, Object> entry : entrySet()) {
//            if(entry.getValue() instanceof Obj) {
//                docuemnt.put(entry.getKey(), ((Obj) entry.getValue()).toBsonDoc());
//            } else if(entry.getValue() instanceof Arr){
//                docuemnt.put(entry.getKey(), ((Arr) entry.getValue()).toBsonArray());
//            } else {
//                docuemnt.put(entry.getKey(), entry.getValue());
//            }
//        }
//        return docuemnt;
//    }

//    public void forEach(KVIterator<Object> iterator){
//        for (Map.Entry<String, Object> e : entrySet()) {
//            try {
//                iterator.call(e.getKey(), e.getValue());
//            } catch (Exception ex) {
//                throw new RuntimeException();
//            }
//        }
//    }

    public Object or(Object obj){
        if(isEmpty()){
            return obj;
        }
        return this;
    }


    @Override
    public String toString() {
        return Json.stringify(this);
    }

    @Deprecated
    public Document toBson(){
        return bo(this);
    }

    @Deprecated
    public <T> T to(Class<T> clz){
        return Json.cast(this, clz);
    }
}
