package com.github.llyb120.json.selector;

import com.github.llyb120.json.Json;
import com.github.llyb120.json.core.StringifyOption;
import org.junit.Test;

import java.util.List;

public class SelectorParserTest {

    @Test
    public void parse() {
        String selector = "  a  b+c+* d,b[jian] c[jian=1][ri][fff!=2]";
        List<SelectorNode> item = new SelectorParser(selector).parse();
        System.out.println(selector);
        System.out.println(Json.stringify(item, new StringifyOption().ignoreKeys("next")));
    }

    @Test
    public void test_02(){
       String selector = "array foo[key1] key1";
        List<SelectorNode> item = new SelectorParser(selector).parse();
        System.out.println(selector);
        System.out.println(Json.stringify(item, new StringifyOption().ignoreKeys("next")));
    }
}