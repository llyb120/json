package com.github.llyb120.json.mongo;

import com.github.llyb120.json.Json;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class MongoSelectorParserTest {

    @Test
    public void parse() {
        String query = "a=1";
        getList(query);

        query = "b.a[c*=2]";
        getList(query);

        getList("orgs[a=1 and (b=2 or c=3)]");
        MongoSelectorParser parser = new MongoSelectorParser("orgs[a=1 and (b=2 or c=3)]");
        List<MongoSelectorToken> list = parser.parse();
        parser.buildAst();
    }

    private List getList(String query){
        List<MongoSelectorToken> list = (new MongoSelectorParser(query)).parse();
        System.out.println(Json.stringify(list));
        return list;
    }
}