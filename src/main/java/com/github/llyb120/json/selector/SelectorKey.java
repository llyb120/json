package com.github.llyb120.json.selector;

import com.github.llyb120.json.lambda.JsonStr;

import java.util.HashMap;
import java.util.Map;

public class SelectorKey {
    public String key;
    public Map<String, PropertyOperator> props = new HashMap<>();

    public SelectorKey(String key) {
        this.key = key;
    }


}
