package com.github.llyb120.json.mongo;


public class MongoSelectorToken extends MongoSelectorItem{
    public String value;

    public MongoSelectorToken(String value) {
        this.value = value;
        switch (value){
            case "[":
                type = MongoSelectorItemType.TOKEN_BLOCK_LEFT;
                break;

            case "]":
                type = MongoSelectorItemType.TOKEN_BLOCK_RIGHT;
                break;

            case "(":
                type = MongoSelectorItemType.TOKEN_BRACKET_LEFT;
                break;

            case ")":
                type = MongoSelectorItemType.TOKEN_BRACKET_RIGHT;
                break;

            case ">":
                type = MongoSelectorItemType.TOKEN_OP_GT;
                break;

            case "<":
                type = MongoSelectorItemType.TOKEN_OP_LT;
                break;

            case ">=":
                type = MongoSelectorItemType.TOKEN_OP_GTE;
                break;

            case "<=":
                type = MongoSelectorItemType.TOKEN_OP_LTE;
                break;

            case "==":
                type = MongoSelectorItemType.TOKEN_OP_EQ;
                break;

            case "!=":
                type = MongoSelectorItemType.TOKEN_OP_NEQ;
                break;

            case "&&":
                type = MongoSelectorItemType.TOKEN_AND;
                break;

            case "||":
                type = MongoSelectorItemType.TOKEN_OR;
                break;

            default:
                type = MongoSelectorItemType.TOKEN_KEYWORD;
                break;

        }
    }
    //    public MongoSelectorToken(MongoSelectorTokenType type){}
}
