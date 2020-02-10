package com.github.llyb120.json.mongo;

public class MongoSelectorToken {
    public MongoSelectorTokenType type;
    public String value;

    public MongoSelectorToken(MongoSelectorTokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public MongoSelectorToken() {
    }

    public MongoSelectorToken(String value) {
        this.value = value;
        switch (value){
            case "[":
                type = MongoSelectorTokenType.OP_BLOCK_LEFT;
                break;

            case "]":
                type = MongoSelectorTokenType.OP_BLOCK_RIGHT;
                break;

            case "(":
                type = MongoSelectorTokenType.OP_BRACKET_LEFT;
                break;

            case ")":
                type = MongoSelectorTokenType.OP_BRACKET_RIGHT;
                break;

        }
    }
    //    public MongoSelectorToken(MongoSelectorTokenType type){}
}
