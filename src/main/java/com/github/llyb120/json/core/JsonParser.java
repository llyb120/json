package com.github.llyb120.json.core;


import com.github.llyb120.json.Arr;
import com.github.llyb120.json.Obj;
import com.github.llyb120.json.Util;

import java.math.BigDecimal;

import static com.github.llyb120.json.Json.*;


public class JsonParser {
    private String str;
    private int ptr = 0;
    private int len = 0;
    private StringBuilder sb = new StringBuilder();

    public JsonParser(String str) {
        this.str = str;
        this.len = str.length();
    }

    public Object parse() {
        JsonToken token = readToken();
        if (token == null) {
            return null;
        }
        if (token.type == JsonTokenType.LEFT_MIDDLE_BLOCK) {
            return parseArr();
        } else if (token.type == JsonTokenType.LEFT_LARGE_BLOCK) {
            return parseObj();
        } else {
            return token.value;
        }
    }

    private Obj parseObj() {
        Obj obj = o();
        JsonToken token;
        String key = null;
        loop:
        while ((token = readToken()) != null) {
            //skip colon
            switch (token.type) {
                case LEFT_LARGE_BLOCK:
                    obj.put(key, parseObj());
                    key = null;
                    break;

                case COLON:
                case COMMA:
                    break;

                case RIGHT_LARGE_BLOCK:
                    break loop;

                case LEFT_MIDDLE_BLOCK:
                    obj.put(key, parseArr());
                    key = null;
                    break;

                case RIGHT_MIDDLE_BLOCK:
                    throw new RuntimeException();

                case TOKEN:
                case STRING:
                    if (key == null) {
                        key = token.value;
                    } else {
                        obj.put(key, parseValue(token));
                        key = null;
                    }
                    break;

                default:
                    if (key == null) {
                        throw new RuntimeException();
                    }
                    obj.put(key, parseValue(token));
                    key = null;
                    break;
            }
        }
        return obj;
    }

    private Object parseValue(JsonToken token) {
        if(token.type == JsonTokenType.TOKEN){
            return new BigDecimal(token.value);
        }
        else if (token.type == JsonTokenType.NUMBER) {
            return new BigDecimal(token.value);
//            if(token.value.contains(".")){
//                return Double.parseDouble(token.value);
//            } else {
//                try{
//                    return Integer.parseInt(token.value);
//                } catch (Exception e){
//                }
//                try{
//                } catch (Exception e){
//                }
//                return null;
//            }
        } else if (token.type == JsonTokenType.BOOL) {
            return Boolean.parseBoolean(token.value);
        } else if (token.type == JsonTokenType.STRING) {
            return token.value;
        } else {
            return null;
        }
    }

    private Arr parseArr() {
        Arr arr = a();
        JsonToken token;
        loop:
        while ((token = readToken()) != null) {
            switch (token.type) {
                case LEFT_MIDDLE_BLOCK:
                    arr.add(parseArr());
                    break;

                case RIGHT_MIDDLE_BLOCK:
                    break loop;

                case LEFT_LARGE_BLOCK:
                    arr.add(parseObj());
                    break;

                case RIGHT_LARGE_BLOCK:
                    throw new RuntimeException();

                case COLON:
                case COMMA:
                    break;

                default:
                    arr.add(parseValue(token));
                    break;
            }

        }
        return arr;
    }

    private void assertToken(JsonToken token, JsonTokenType... types) {
        for (JsonTokenType type : types) {
            if (token.type == type) {
                return;
            }
        }
        throw new RuntimeException();
    }

    private boolean isLineComment(){
        return str.charAt(ptr) == '/' && (ptr < len - 1) && str.charAt(ptr + 1) == '/';
    }
    private boolean isMultipleCommentStart(){
        return str.charAt(ptr) == '/' && (ptr < len - 1) && str.charAt(ptr + 1) == '*';
    }
    private boolean isMultipleCommentEnd(){
        //到达末尾，结束
        if(ptr >= len){
            return true;
        }
        return str.charAt(ptr) == '*' && (ptr < len - 1) && str.charAt(ptr + 1) == '/';
    }

    private JsonToken readToken() {
        sb.setLength(0);
        boolean isBlank = false;
        boolean isStr = false;
        int start = -1;
        while (ptr < len) {
            char ch = str.charAt(ptr);
            boolean isStrStart = (ch == '"');
            //如果在str中，遇到\无条件步进
            if(isStr){
                if(ch == '\\'){
                    char nextChar = str.charAt(ptr + 1);
                    if(nextChar == '"' || nextChar == '\''){
                        ptr += 2;
                        sb.append(nextChar);
                        continue;
                    }
                }
                if(!isStrStart){
                    sb.append(ch);
                }
//                sb.append()
            }
//            if(isStr && ch == '\\'){
//                ptr += 2;
//                continue;
//            }
            if(isLineComment()){
                //读到末尾
                ptr += 2;
                while(ptr < len){
                    ch = str.charAt(ptr);
                    if(ch == '\n'){
                        break;
                    }
                    ptr++;
                }
                ch = str.charAt(ptr);
            }
            if(isMultipleCommentStart()){
                ptr += 2;
                while(ptr < len){
                    if(isMultipleCommentEnd()){
                        break;
                    }
                    ptr++;
                }
                ptr += 2;
                ch = str.charAt(ptr);
            }

            //如果已经结束了
            if(ptr >= len){
                break;
            }

            //
            isBlank = Util.isBlankChar(ch);
            if (start > -1) {
                if (isStrStart && isStr) {
                    ptr++;
                    return new JsonToken(JsonTokenType.STRING, sb.toString());
                } else if (!isStr && (ch == '{' || ch == '}' || ch == '[' || ch == ']' || ch == ':' || ch == ',' || isBlank)) {
                    //强行中断
                    String token = str.substring(start, ptr);
                    if (isBlank) {
                        ptr++;
                    }
                    if (token.equalsIgnoreCase("true") || token.equalsIgnoreCase("false")) {
                        return new JsonToken(JsonTokenType.BOOL, token);
                    }
                    if (token.equalsIgnoreCase("null")) {
                        return new JsonToken(JsonTokenType.NULL, token);
                    }
//                        if(isStr){
//                            return new JsonToken(JsonTokenType.STRING, token);
//                        }
                    return new JsonToken(JsonTokenType.TOKEN, token);
                } else {
                    //不是空格的情况下才往下走（空格立刻结束）
                    ptr++;
                    continue;
                }
            } else {
                switch (ch){
                    case ':':
                        ptr++;
                        return new JsonToken(JsonTokenType.COLON, ":");

                    case ',':
                        ptr++;
                        return new JsonToken(JsonTokenType.COMMA, ",");

                    case '[':
                        ptr++;
                        return new JsonToken(JsonTokenType.LEFT_MIDDLE_BLOCK, "[");

                    case ']':
                        ptr++;
                        return new JsonToken(JsonTokenType.RIGHT_MIDDLE_BLOCK, "]");

                    case '{':
                        ptr++;
                        return new JsonToken(JsonTokenType.LEFT_LARGE_BLOCK, "{");

                    case '}':
                        ptr++;
                        return new JsonToken(JsonTokenType.RIGHT_LARGE_BLOCK, "}");
                }
                if (isStrStart) {
                    isStr = true;
                    start = ptr + 1;
                } else if (!isBlank) {
                    start = ptr;
                }
            }
            ptr++;
        }
        return null;
    }
}