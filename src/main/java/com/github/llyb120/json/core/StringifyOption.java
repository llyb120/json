package com.github.llyb120.json.core;

import java.util.HashMap;
import java.util.Map;

public class StringifyOption {
    Map<String, Boolean> ignoreKeys = new HashMap<>();

    public Map ignoreKeys() {
        return ignoreKeys;
    }

    public StringifyOption ignoreKeys(String ...keys){
        for (String key : keys) {
            ignoreKeys.put(key, true);
        }
        return this;
    }
}
