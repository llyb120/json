package com.github.llyb120.json.selector;

import com.github.llyb120.json.reflect.ReflectUtil;

import java.util.*;

public class ObjectWalker {
    public LinkedList<Map.Entry> path;
    public List<LinkedList<Map.Entry>> result;

    public ObjectWalker walk(Object object) {
        path = new LinkedList<>();
        result = new ArrayList();
        _walk(object);
        return this;
    }

    private void _walk(Object object) {
        Map<String, Object> props = ReflectUtil.getValues(object);
        //如果没有下一个了
//        if (props.isEmpty()) {
            result.add(new LinkedList<>(path));
//            return;
//        }
        for (Map.Entry<String, Object> entry : props.entrySet()) {
            //禁止回溯
            if(path.stream().anyMatch(e -> e.getValue() == entry.getValue())){
                continue;
            }
            path.addLast(entry);
            _walk(entry.getValue());
            path.removeLast();
        }
    }
}
