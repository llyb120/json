package com.github.llyb120.json.selector;

import com.github.llyb120.json.Arr;
import com.github.llyb120.json.Json;
import com.github.llyb120.json.core.StringifyOption;
import com.github.llyb120.json.reflect.ReflectUtil;

import java.util.*;

import static com.github.llyb120.json.Json.a;

public final class JsonPicker {

    private static boolean checkKey(SelectorKey key, Object key2) {
        return (Objects.equals("*", key.key) || Objects.equals(key.key, key2));
    }

    private static boolean checkValue(SelectorKey key, Object value) {
        if(key.props.isEmpty()){
            return true;
        }
        return value instanceof Map && key.props
                .entrySet()
                .stream()
                .allMatch(e -> {
                    PropertyOperatorType op = e.getValue().op;
                    if(op == PropertyOperatorType.EQUAL){
                        return Objects.equals(e.getValue().value, String.valueOf(((Map) value).get(e.getKey())));
                    }
                    if(op == PropertyOperatorType.NOT_EQUAL){
                        return !Objects.equals(e.getValue().value, String.valueOf(((Map) value).get(e.getKey())));
                    }
                    if(op == PropertyOperatorType.JUST_KEY){
                        return ((Map) value).containsKey(e.getKey());
                    }
                    if(op == PropertyOperatorType.CONTAINS){
                        return String.valueOf(((Map) value).get(e.getKey())).contains(e.getValue().value);
                    }
                    return false;
                });
    }

    public static <T> Arr<T> pick(String selector, Object source, Class<T> resultClz) {
        List<SelectorNode> parsed = new SelectorParser(selector).parse();
        List<List<Map.Entry>> values = new ObjectWalker().walk(source).result;
        Arr<T> result = a();
        Map cache = new HashMap();
        for (List<Map.Entry> entries : values) {
            check:
            for (SelectorNode selectorNode : parsed) {
                SelectorNode node = selectorNode;
                //命中任意
                //倒序检查
                int i = entries.size();
                Map.Entry matched = null;
                while(i-- > 0){
                    Map.Entry next = entries.get(i);//it.next();
                    //key满足
                    int finalI = i;
                    if (node.keys.stream().anyMatch(e -> {
                        boolean validKey = checkKey(e, next.getKey());
                        //如果前面的是一个数组，那么也满足
                        if(!validKey){
                            if(finalI - 1 >= 0){
                                Map.Entry prev = entries.get(finalI - 1);
                                validKey = (prev.getValue() instanceof Collection && checkKey(e, prev.getKey()));
                            }
                        }
                        return validKey && checkValue(e, next.getValue());
                    })) {
                        if (matched == null) {
                            matched = next;
                        }
                        node = node.prev;
                        if (node == null) {
                            if(cache.containsKey(matched)){
                                break check;
                            }
                            result.add(Json.cast(matched.getValue(), resultClz));
                            cache.put(matched, true);
                            break check;
                        }
                    }
//                    System.out.println(Json.stringify(next, new StringifyOption().ignoreKeys("next")));
                }
//                System.out.println("***********");
            }
        }
//        System.out.println(result);
        return result;
    }
//    void _pick(){
//
//    }
//
//    private void walk(Object object){
//        Map<String, Object> props = ReflectUtil.getValues(object);
//        props.forEach((k,v) -> walk(v));
//        System.out.println(object);
//    }


}
