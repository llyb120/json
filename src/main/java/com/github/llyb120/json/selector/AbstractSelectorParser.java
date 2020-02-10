package com.github.llyb120.json.selector;

public abstract class AbstractSelectorParser {
    protected char[] chars;
    protected int ptr = 0;

    public AbstractSelectorParser(String selector){
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

    public char peekNext(){
        if(ptr + 1 >= chars.length){
            return (char) -1;
        }
        return chars[ptr + 1];
    }

    public boolean hasNext(){
        return ptr < chars.length;
    }

}
