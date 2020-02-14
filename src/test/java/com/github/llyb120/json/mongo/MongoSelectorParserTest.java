package com.github.llyb120.json.mongo;

import com.github.llyb120.json.Json;
import org.junit.Test;

import java.util.List;

import static com.github.llyb120.json.Json.o;
import static org.junit.Assert.*;

public class MongoSelectorParserTest {

    @Test
    public void parse() {
//        String query = "a=1";
//        getList(query);
//
//        query = "b.a[c*=2]";
//        getList(query);
//
//        getList("orgs[][a=1 and (b=2 or c=3)]");
        MongoSelectorParser parser = new MongoSelectorParser("orgs..(a==1 && (b==2 || c==3)) || orgs.(a==1) && a==2");
//        MongoSelectorParser parser2 = new MongoSelectorParser("orgs.(a == 1 && b == 2)");
        List<MongoSelectorToken> list = parser.parse();
        System.out.println(Json.stringify(list));
        parser.buildAst();
        Object ret = parser.calculate(null, o("a", 2, "orgs", o("a", 1, "b", 2, "c", 3)));
        assertTrue((Boolean) ret);
        ret = parser.calculate(null, o("orgs", o("a", 2, "b", 2, "c", 3)));
        assertFalse((Boolean) ret);
        ret = parser.calculate(null, o("orgs", o("a", 1, "b", 100, "c", 3)));
        assertTrue((Boolean) ret);

        System.out.println(ret);
    }

    private List getList(String query){
        List<MongoSelectorToken> list = (new MongoSelectorParser(query)).parse();
        System.out.println(Json.stringify(list));
        return list;
    }
}