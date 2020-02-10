package com.github.llyb120.json.mongo;

import com.github.llyb120.json.Util;
import com.github.llyb120.json.selector.AbstractSelectorParser;
import jdk.nashorn.internal.parser.Token;

import java.util.ArrayList;
import java.util.List;

public final class MongoSelectorParser extends AbstractSelectorParser {
    private StringBuilder sb = new StringBuilder();

    public MongoSelectorParser(String selector){
        super(selector);
    }


    public List<MongoSelectorToken> parse(){
        List<MongoSelectorToken> result = new ArrayList<>();
        MongoSelectorToken token;// = new MongoSelectorToken();
        int left = -1;
        int right = -1;
        while(hasNext()){
            char ch = peek();
            //可以截取运算符
            if(isSingleSpecial(ch)){
                if(left > -1){
                    result.add(new MongoSelectorToken(MongoSelectorTokenType.KEYWORD, new String(chars, left, ptr - left)));
                    left = -1;
                }
                token = new MongoSelectorToken(ch + "");//readSpecial();
                result.add(token);
            } else if(isSpecial(ch)){
                if(left > -1){
                    result.add(new MongoSelectorToken(MongoSelectorTokenType.KEYWORD, new String(chars, left, ptr - left)));
                    left = -1;
                }
                token = readSpecial();
                result.add(token);
            } else {
                boolean end = (ptr == chars.length - 1);
                boolean blank = Util.isBlankChar(ch);
                //单词开始
                if(left == -1 && !blank){
                    left = ptr;
                }
                if(left > -1 && (blank||end)){
                    right = ptr;
                }

                if(left > -1 && right > -1){
                    result.add(new MongoSelectorToken(MongoSelectorTokenType.KEYWORD, new String(chars, left, right - left)));
                    left = -1;
                    right = -1;
                }
            }

            ptr++;


            //可以截取出单词
//            if(left > -1 && right > -1){
//                System.out.println(new String(chars, left, right - left));
//            }
//            if(isSpecial(ch)){
//                token.value = getTokenValue();
//                result.add(token);
//                token = readSpecial();
//                if (token != null) {
//                    result.add(token);
//                }
//                token = new MongoSelectorToken();
//                continue;
//            } else if(!blank && sb.length() == 0){
//                sb.append(ch);
//            }
//            //如果是末尾或者单词临界点
//            if(!hasNext() || blank){
//                if(sb.length() > 0){
//                    token.value = getTokenValue();
//                    result.add(token);
//                    token = new MongoSelectorToken();
//                }
//            }
        }
        return result;
    }


    private void buildAst(){

    }

    private MongoSelectorToken readSpecial(){
        int left = ptr;
        int right = -1;
        while(hasNext()){
            char ch = peek();
            if(!isSpecial(ch)) {
                right = ptr--;
                break;
            }
            ptr++;
        }
        if(right == -1){
            right = ptr;
        }
        MongoSelectorToken token = new MongoSelectorToken();
        token.value = new String(chars, left, right - left);
        return token;
    }

    private boolean isSpecial(char ch){
        return ch == '=' || ch == '>' || ch == '<' || ch == '!' ||  ch == '*' ;
    }

    private boolean isSingleSpecial(char ch){
        return ch == '[' || ch == ']' ||  ch == '(' || ch == ')';
    }


}
