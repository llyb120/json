package com.github.llyb120.json.mongo;

public class MongoSelectorNode extends MongoSelectorItem{
    public MongoSelectorItem left;
    public MongoSelectorItem right;

    public MongoSelectorItemType value;

    public MongoSelectorNode(MongoSelectorItemType type){
        this.type = type;
    }

}
