package com.github.llyb120.json2;

import com.github.llyb120.json.Util;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.github.llyb120.json2.JSONContext.Type.*;

/**
 * @Author: Administrator
 * @Date: 2020/7/23 16:38
 */
public class JSON2Parser {

   private byte[] bytes;
   private List<JSONContext> stack = new ArrayList<>();
   private Map<String, Object> map = new LinkedHashMap<>();
   private StringBuilder sb = new StringBuilder();

    public JSON2Parser(byte[] bytes) {
        this.bytes = bytes;
    }

    public JSON2Parser(String str) {
        this.bytes = str.getBytes(StandardCharsets.UTF_8);
    }

    public Obj2 parse(){
        if (bytes == null) {
            throw new RuntimeException() ;
        }
        int len = bytes.length;
        int tokenStart = -1;
        //上下文stack
        JSONContext last;
        String token;
        for(int ptr = 0; ptr < len; ptr++){
            byte b = bytes[ptr];
            if(b == '\\'){
                continue;
            } else if(b == '"') {
                if(tokenStart == -1){
                    tokenStart = ptr + 1;
                } else {
                    token = new String(bytes, tokenStart, ptr - tokenStart, StandardCharsets.UTF_8);
                    tokenStart = -1;
                    putValue(token);
                }
            } else if(tokenStart == -1){
                switch (b){
                    case ':':
                    case ',':
                        break;

                    case '{':
                        stack.add(new JSONContext(TYPE_OBJECT, ".", -1));
                        break;

                    case '}':
                        //找到上一个object
                        for (int i = stack.size() - 1; i >= 0; i--) {
                            if(stack.get(i).type == TYPE_OBJECT) {
                                splice(i);
                                break;
                            }
                        }
                        last = getLast();
                        if (last == null) {
                            break;
                        }
                        if(last.type == TYPE_KEY){
                            splice(stack.size() -1);
                        } else if(last.type == TYPE_ARRAY){
                            last.index++;
                        }
                        break;

                    case '[':
                        //数组开始
                        stack.add(new JSONContext(TYPE_ARRAY, null, 0));
                        break;

                    case ']':
                        //找到上一个array开始
                        for (int i = stack.size() - 1; i >= 0; i--) {
                            if(stack.get(i).type == TYPE_ARRAY) {
                                splice(i);
                                break;
                            }
                        }
                        last = getLast();
                        if (last == null) {
                            break;
                        }
                        if(last.type == TYPE_KEY){
                            splice(stack.size() - 1);
                        } else if(last.type == TYPE_ARRAY){
                            last.index++;
                        }
                        break;

                    default:
                        if(!Util.isBlankChar(b)){
                            tokenStart = ptr;
                        }
                        break;

                }
            } else if(Util.isBlankChar(b) || b == ',' || b == ':' || b == '}' || b == ']') {
                //遇到需要中断的情况
                token = new String(bytes, tokenStart, ptr - tokenStart, StandardCharsets.UTF_8);
                tokenStart = -1;
                putValue(token);
                ptr--;
            }
        }

        return new Obj2(map);
    }


    /**
     * 得到最后一个Context
     *
     * @return 返回最后一个
     */
    JSONContext getLast(){
        if(stack.isEmpty()){
            return null;
        }
        return stack.get(stack.size() - 1);
    }

    /**
     * 删除LIST中的指定位置的元素
     *
     * @param pos 从当前位置删到末尾
     *
     */
    void splice(int pos){
        int len = stack.size();
        for (int i = pos; i < len; i++) {
            stack.remove(stack.size() - 1);
        }
    }

    void putValue(String val){
        sb.setLength(0);
        JSONContext last = getLast();
        if(last.type == TYPE_KEY || last.type == TYPE_ARRAY){
            //必定为value
            for (int i = 1; i < stack.size(); i++) {
                JSONContext jsonContext = stack.get(i);
                if(jsonContext.type == TYPE_ARRAY){
                    sb.append(".");
                    sb.append(jsonContext.index);
                } else {
                    sb.append(jsonContext.token);
                }
            }
//            for (JSONContext jsonContext : stack) {
//            }

            //如果为array则记数+1
            if(last.type == TYPE_ARRAY) {
                last.index++;
            } else {
                splice(stack.size() - 1);
            }
            map.put(sb.toString(), val);
        } else {
            //必定为KEY
            stack.add(new JSONContext(TYPE_KEY, val, -1));
        }
    }

}
