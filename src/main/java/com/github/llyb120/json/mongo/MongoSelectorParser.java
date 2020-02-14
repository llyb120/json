package com.github.llyb120.json.mongo;

import com.github.llyb120.json.Json;
import com.github.llyb120.json.Obj;
import com.github.llyb120.json.Util;
import com.github.llyb120.json.error.ErrorMongoSelectorException;
import com.github.llyb120.json.selector.AbstractSelectorParser;
import org.jcp.xml.dsig.internal.dom.ApacheNodeSetData;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.github.llyb120.json.Json.o;
import static com.github.llyb120.json.mongo.MongoSelectorItemType.*;

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
                if(left > -1 && end){
                    right = ptr + 1;
                }
                else if(left > -1 && blank){
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
        LinkedList<Integer> lastBracket = new LinkedList<>();
        while(tokenPtr < len){
            MongoSelectorToken token = peekToken();
            if (token == null) {
               break;
            }
            if(token.type == TOKEN_BRACKET_LEFT){
                stack.add(token);
                lastBracket.add(tokenPtr);// = tokenPtr;
            } else if (token.type == TOKEN_BRACKET_RIGHT) {
                stack.add(token);
                ass(!lastBracket.isEmpty());
                ass(stack.size() > 2);
                mergeBracket(stack.size() - 3);
                //如果前面可以规约成描述
                if(stack.size() > 1 && stack.get(stack.size() - 2).type == TOKEN_KEYWORD){
                    mergeMatch(stack.size() - 2);
                }
            }
//            else if (token.type == MongoSelectorItemType.TOKEN_BLOCK_LEFT) {
//                stack.add(token);
//                lastBlock.add(tokenPtr);// = tokenPtr;
//            }
//            //规约描述表达式
//            else if (token.type == MongoSelectorItemType.TOKEN_BLOCK_RIGHT) {
//                stack.add(token);
//                ass(!lastBlock.isEmpty());
//                if(lastBlock.getLast() == tokenPtr - 1){
//                    lastBlock.removeLast();
//                } else {
//                    if(stack.size() > 5){
//                        //检查是否数组匹配
//                        if(stack.get(stack.size() - 5).type == TOKEN_BLOCK_LEFT && stack.get(stack.size() - 4).type == TOKEN_BLOCK_RIGHT){
//                            lastBlock.removeLast();
//                            mergeBlock(stack.size() - 6);
//                        } else if(tokens.get(lastBlock.getLast() - 1).type == TOKEN_KEYWORD){
//                            lastBlock.removeLast();
//                            mergeBlock(stack.size() - 4);
//                        }
//                    } else if(stack.size() > 3){
//                        if(tokens.get(lastBlock.getLast() - 1).type == TOKEN_KEYWORD){
//                            lastBlock.removeLast();
//                            mergeBlock(stack.size() - 4);
//                        }
//                    } else {
//                        ass(false);
//                    }
////                    if(lastBlock.size() == 6){
////                        mergeBlock(stack.size() - 6);
////                    } else if(lastBlock.size() == 4){
////                        mergeBlock(stack.size() - 4);
////                    } else {
////                        ass(false);
////                    }
////                    int d = 2;
//                }
////                if (!lastBlock.isEmpty()) {
////                    mergeBlock(lastBlock.removeLast());
////                }
////                //如果是描述
////                MongoSelectorItem node = getStackLast();
////                if (node instanceof MongoSelectorNode) {
////                    ass(stack.size() > 1);
////                    MongoSelectorToken keyword = (MongoSelectorToken) stack.get(stack.size() - 2);
////                    ass(keyword.type == MongoSelectorItemType.TOKEN_KEYWORD);
////                    stack.remove(stack.size() - 2);
////                    ((MongoSelectorNode) node).left = keyword;
////                }
//            }
            else if (token.type == MongoSelectorItemType.TOKEN_KEYWORD) {
                stack.add(token);
                if (stack.size() > 2 && isOP(stack.get(stack.size() - 2))) {
                    mergeExpression(stack.size() - 3);
                }
            } else {
                stack.add(token);
            }

            //处理and和or
            if(token.type != TOKEN_BLOCK_LEFT  && token.type != TOKEN_BRACKET_LEFT ){
                if(stack.size() > 2){
                    MongoSelectorItemType type = stack.get(stack.size() - 2).type;
                    if(stack.get(stack.size() -3).type == NODE_EXPRESSION && stack.get(stack.size() -1).type == NODE_EXPRESSION){
                        if(type == TOKEN_AND){
                            mergeAnd(stack.size() - 3);
                        } else if(type == TOKEN_OR){
                            mergeOr(stack.size() - 3);
                        }
                    }
                }
            }
            tokenPtr++;
        }
        ass(stack.size() == 1);
        System.out.println(Json.stringify(stack));
    }

    public Object calculate(MongoSelectorNode node, Obj context){
        if (node == null) {
            node = (MongoSelectorNode) stack.get(0);
        }
        Object left = null;
        Object right = null;
        if(node.left instanceof MongoSelectorNode){
            left = calculate((MongoSelectorNode) node.left, context);
        } else if(node.left instanceof MongoSelectorToken){
            left = context.s(((MongoSelectorToken) node.left).value);
        }
        if(node.right instanceof MongoSelectorNode){
            right = calculate((MongoSelectorNode) node.right, context);
        } else if(node.right instanceof MongoSelectorToken){
            right = String.valueOf(((MongoSelectorToken) node.right).value);
        }
        switch (node.value){
            case TOKEN_OR:
                return ((boolean) left) || ((boolean) right);
            case TOKEN_AND:
                return ((boolean) left) && ((boolean) right);
            case TOKEN_OP_EQ:
                return Objects.equals(left, right);

            case NODE_MATCH:
                Obj nc = context.get(((MongoSelectorToken) node.left).value, Obj.class);
                if (nc == null) {
                    nc = o();
                }
                return calculate((MongoSelectorNode) node.right, nc);
            case NODE_ELEM_MATCH:
                return false;


        }
        return null;
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

    private MongoSelectorNode mergeMatch(int from){
        List<MongoSelectorItem> list = stack.subList(from, stack.size());
        ass(list.size() == 2);
        ass(list.get(1).type == NODE_EXPRESSION);
        MongoSelectorItem key = list.get(0);
        ass(key.type == TOKEN_KEYWORD);
        MongoSelectorNode node = new MongoSelectorNode(NODE_EXPRESSION);
        if(((MongoSelectorToken)key).value.endsWith("..")){
            node.value = NODE_ELEM_MATCH;
            ((MongoSelectorToken) key).value = ((MongoSelectorToken) key).value.substring(0, ((MongoSelectorToken) key).value.length() - 2);
        } else if(((MongoSelectorToken)key).value.endsWith(".")){
            node.value = NODE_MATCH;
            ((MongoSelectorToken) key).value = ((MongoSelectorToken) key).value.substring(0, ((MongoSelectorToken) key).value.length() - 1);
        } else {
            ass(false);
        }
        node.left = key;
        node.right = list.get(1);
        stackDelTo(list.get(0));
        stack.add(node);
        return node;
    }

    private MongoSelectorNode mergeAnd(int from){
        List<MongoSelectorItem> list = stack.subList(from, stack.size());
        ass(list.size() == 3);
        MongoSelectorNode node = new MongoSelectorNode(NODE_EXPRESSION);
        node.left = list.get(0);
        node.right = list.get(2);
        node.value = TOKEN_AND;
        stackDelTo(list.get(0));
        stack.add(node);
        return node;
    }

    private MongoSelectorNode mergeOr(int from){
        List<MongoSelectorItem> list = stack.subList(from, stack.size());
        ass(list.size() == 3);
        MongoSelectorNode node = new MongoSelectorNode(NODE_EXPRESSION);
        node.left = list.get(0);
        node.right = list.get(2);
        node.value = TOKEN_OR;
        stackDelTo(list.get(0));
        stack.add(node);
        return node;
    }

    private MongoSelectorNode mergeExpression(int from){
        List<MongoSelectorItem> list = stack.subList(from, stack.size());
        ass(list.size() == 3);
        MongoSelectorNode node = new MongoSelectorNode(NODE_EXPRESSION);
        node.value = list.get(1).type;
        node.left = list.get(0);
        node.right = list.get(2);
        stackDelTo(list.get(0));
        stack.add(node);
        return node;
    }

    private void stackDelTo(MongoSelectorItem item){
        //弹出元素
        int i = stack.size();
        while(--i >= 0){
            Object removed = stack.remove(i);
            if(removed == item){
                break;
            }
        }
    }

    private MongoSelectorNode mergeBlock(int from){
        List<MongoSelectorItem> list = stack.subList(from, stack.size());
        MongoSelectorNode node = null;
        if(list.size() == 4){
            node = new MongoSelectorNode(NODE_EXPRESSION);
            node.value = NODE_MATCH;
            node.left = list.get(0);
            node.right = list.get(2);
        } else if(list.size() == 6){
            node = new MongoSelectorNode(NODE_EXPRESSION);
            node.value = NODE_ELEM_MATCH;
            node.left = list.get(0);
            node.right = list.get(4);
        } else {
            ass(false);
        }
        stackDelTo(list.get(0));
        stack.add(node);
        return node;
    }

    private MongoSelectorNode mergeBracket(int from){
        List<MongoSelectorItem> list = stack.subList(from, stack.size());
        ass(list.size() == 3);
        MongoSelectorItem item = list.get(1);
        ass(item instanceof MongoSelectorNode);
        stackDelTo(list.get(0));
        stack.add(item);
        return (MongoSelectorNode) item;
    }

    private boolean isOP(MongoSelectorItem token){
        return token.type.name().startsWith("TOKEN_OP_");
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
            case '&':
            case '|':
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
