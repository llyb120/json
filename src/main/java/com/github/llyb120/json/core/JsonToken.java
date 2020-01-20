package com.github.llyb120.json.core;

public class JsonToken {
    public JsonTokenType type;
    public String value;

    public JsonToken(JsonTokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return type.name() + " : " + value;
    }
}
