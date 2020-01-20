package com.github.llyb120.json.selector;

import com.github.llyb120.json.Obj;
import org.junit.Test;

import static com.github.llyb120.json.Json.a;
import static com.github.llyb120.json.Json.o;
import static org.junit.Assert.*;

public class ObjectWalkerTest {

    @Test
    public void walk() {
        //测试回溯是否会出错
        Obj par;
        Obj item = o(
                "a", 1,
                "b", o(
                        "c", a(1, par = o(
                                "d", "fff"
                        ))
                )
        );
        par.put("par", par);
        ObjectWalker walker = new ObjectWalker();
        walker.walk(item);
        assertEquals(walker.result.size(), 3);

        int d = 2;

    }
}