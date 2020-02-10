package com.github.llyb120.json.selector;

import com.github.llyb120.json.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class SelectorParser {
    private char[] chars;
    private int ptr = 0;
    private StringBuilder sb = new StringBuilder();

    public SelectorParser(String selector){
        this.chars = selector.toCharArray();
    }

    public char next(){
        return chars[ptr++];
    }

    public void go(){
        ptr++;
    }

    public char peek(){
        if(ptr >= chars.length){
            return (char) -1;
        }
        return chars[ptr];
    }

    public boolean hasNext(){
        return ptr < chars.length;
    }


    //parser
    public List<SelectorNode> parse(){
        SelectorNode node = null;//new SelectorNode();
        List<SelectorNode> result = new ArrayList<>();
        while(hasNext()){
            char ch = next();
            //忽略空白字段
            boolean blank = Util.isBlankChar(ch);
            //如果下一个还是空白，直接忽略
            if(blank && Util.isBlankChar(peek())){
                continue;
            }
            //如果出现了有意义的字段
            if (node == null) {
                node = new SelectorNode();
                result.add(node);
            }
            if (ch == ',') {
                //并列的选择器
                addKey(node);
                node = new SelectorNode();
                result.add(node);
                continue;
            } else if((blank)){
                //下属选择器
                addKey(node);
                SelectorNode temp = node;
                node = new SelectorNode();
                temp.setNext(node);
                continue;
            } else if(ptr == chars.length){
                if(!blank){
                    sb.append(ch);
                }
                addKey(node);
                continue;
            } else if(ch == '['){
                addKey(node);
                readProperty(node);
                continue;
            } else if(ch == '+'){
                //并列的区间
                addKey(node);
                continue;
            }
            if(!blank){
                sb.append(ch);
            }
        }
        //如果还有剩余
//        addKey(node);
        //逆序
        return result.stream()
                .map(e -> {
                    SelectorNode item = e;
                    while(item.next != null){
                        item = item.next;
                    }
                    return item;
                })
                .collect(Collectors.toList());
    }

    private void readProperty(SelectorNode node){
        //属性开始
//        boolean readKey = false;
        PropertyOperatorType op = PropertyOperatorType.JUST_KEY;
        String key = null;
        while(hasNext()){
            char ch = next();
            boolean blank = Util.isBlankChar(ch);
            if (key == null && blank) {
                continue;
            }
            if(ch == ']'){
                if(key == null){
                    key = getToken();
                    node.keys.getLast().props.put(key, new PropertyOperator(op, null));
                } else {
                    String value = (getToken());
                    node.keys.getLast().props.put(key, new PropertyOperator(op, value));
                }
                break;
            }
            if(key == null){
                if(ch == '='){
                   op = PropertyOperatorType.EQUAL;
                key = getToken();
                    continue;
                } else if(ch == '!'){
                    op = PropertyOperatorType.NOT_EQUAL;
                    go();
                    key = getToken();
                    continue;
                } else if(ch == '*'){
                   op = PropertyOperatorType.CONTAINS;
                   go();
                    key = getToken();
                    continue;
                }
            }
            sb.append(ch);
        }
    }

    private String getToken(){
        if(sb.length() > 0){
            String str = sb.toString();
            sb.setLength(0);
            return str;
        }
        return "";
    }

    private void addKey(SelectorNode node){
        if(sb.length() > 0){
            SelectorKey key = new SelectorKey(getToken());
            node.keys.add(key);
        }
    }

}