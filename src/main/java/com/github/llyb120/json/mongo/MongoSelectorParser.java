package com.github.llyb120.json.mongo;

import com.github.llyb120.json.Util;
import com.github.llyb120.json.error.ErrorMongoSelectorException;
import com.github.llyb120.json.selector.AbstractSelectorParser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class MongoSelectorParser extends AbstractSelectorParser {
    private StringBuilder sb = new StringBuilder();
    private List<MongoSelectorToken> tokens = new ArrayList<>();
    private List<MongoSelectorItem> stack = new ArrayList<>();
    private int tokenPtr = 0;

    public MongoSelectorParser(String selector){
        super(selector);
    }


    public List<MongoSelectorToken> parse(){
        MongoSelectorToken token;// = new MongoSelectorToken();
        int left = -1;
        int right = -1;
        while(hasNext()){
            char ch = peek();
            //可以截取运算符
            if(isSingleSpecial(ch)){
                if(left > -1){
                    tokens.add(new MongoSelectorToken(new String(chars, left, ptr - left)));
                    left = -1;
                }
                token = new MongoSelectorToken(ch + "");//readSpecial();
                tokens.add(token);
            } else if(isSpecial(ch)){
                if(left > -1){
                    tokens.add(new MongoSelectorToken(new String(chars, left, ptr - left)));
                    left = -1;
                }
                token = readSpecial();
                tokens.add(token);
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
                    tokens.add(new MongoSelectorToken(new String(chars, left, right - left)));
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
        return tokens;
    }


    public void buildAst(){
        int len = tokens.size();
        LinkedList<Integer> lastBlock = new LinkedList<>();
        while(tokenPtr < len){
            MongoSelectorToken token = peekToken();
            if (token == null) {
               break;
            }
            if(token.type == MongoSelectorItemType.TOKEN_OP_BLOCK_LEFT){
                stack.add(token);
                lastBlock.add(tokenPtr);// = tokenPtr;
            }
            //规约描述表达式
            else if(token.type == MongoSelectorItemType.TOKEN_OP_BLOCK_RIGHT){
                if(!lastBlock.isEmpty()){
                    mergeBlock(lastBlock.removeLast(), stack.size());
                }
                //如果是描述
                MongoSelectorItem node = getStackLast();
                if(node instanceof MongoSelectorNode){
                    ass(stack.size() > 1);
                    MongoSelectorToken keyword = (MongoSelectorToken) stack.get(stack.size() - 2);
                    ass(keyword.type == MongoSelectorItemType.TOKEN_KEYWORD);
                    stack.remove(stack.size() - 2);
                    ((MongoSelectorNode) node).left = keyword;
                }
            }
            else if(token.type == MongoSelectorItemType.TOKEN_KEYWORD){
                stack.add(token);
            }
            else if(isOP(token)){
//                assertToken(stack.getLast(), MongoSelectorTokenType.KEYWORD);
//                assertToken(nextToken(), MongoSelectorTokenType.KEYWORD);
            }
            tokenPtr++;
        }
        int d = 2;
    }

    private MongoSelectorItem getStackLast(){
        ass(!stack.isEmpty());
        return stack.get(stack.size() - 1);
    }

    private void ass(boolean flag){
        if(!flag){
            throw new ErrorMongoSelectorException();
        }
    }

    private MongoSelectorNode mergeBlock(int from, int to){
        MongoSelectorNode node = new MongoSelectorNode();
        node.type = MongoSelectorItemType.NODE_DESCRIBE;

        List<MongoSelectorItem> list = stack.subList(from, to);
        if(list.isEmpty()){
            throw new ErrorMongoSelectorException();
        }
        Object target = list.get(0);
        //弹出元素
        int i = stack.size();
        while(--i >= 0){
            Object removed = stack.remove(i);
            if(removed == target){
                break;
            }
        }

        stack.add(node);
        return node;
    }

    private boolean isOP(MongoSelectorToken token){
        return token.type.name().startsWith("OP_");
    }

    private MongoSelectorToken nextToken(){
        if(tokenPtr + 1 >= tokens.size() ){
            return null;
        }
        return tokens.get(tokenPtr + 1);
    }

    private MongoSelectorToken peekToken(){
        if(tokenPtr >= tokens.size()){
            return null;
//            throw new ErrorMongoSelectorException();
        }
        return tokens.get(tokenPtr);
    }

    private void assertToken(MongoSelectorToken token, MongoSelectorItemType targetType){
        if(token == null || token.type != targetType){
            throw new ErrorMongoSelectorException();
        }
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
        String value = new String(chars, left, right - left);
        MongoSelectorToken token = new MongoSelectorToken(value);
        return token;
    }

    private boolean isSpecial(char ch){
        switch (ch){
            case '=':
            case '>':
            case '<':
            case '!':
            case '*':
                return true;
        }
        return false;
    }

    private boolean isSingleSpecial(char ch){
        switch (ch){
            case '[':
            case ']':
            case '(':
            case ')':
                return true;
        }
        return false;
    }


}
