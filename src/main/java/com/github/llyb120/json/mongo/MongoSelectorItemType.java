package com.github.llyb120.json.mongo;

public enum MongoSelectorItemType {
    TOKEN_KEYWORD,
    TOKEN_AND,
    TOKEN_OR,
    TOKEN_OP_EQ,
    TOKEN_OP_GT,
    TOKEN_OP_GTE,
    TOKEN_OP_LT,
    TOKEN_OP_LTE,
    TOKEN_OP_NEQ,
    TOKEN_BLOCK_LEFT,
    TOKEN_BLOCK_RIGHT,
    TOKEN_BRACKET_LEFT,
    TOKEN_BRACKET_RIGHT,


    NODE_MATCH,
    NODE_ELEM_MATCH,
    NODE_EXPRESSION,

    ;
}
