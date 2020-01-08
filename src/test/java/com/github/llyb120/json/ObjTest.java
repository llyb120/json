package com.github.llyb120.json;

import org.testng.annotations.Test;

import static com.github.llyb120.json.Json.a;
import static com.github.llyb120.json.Json.o;
import static org.testng.Assert.*;

public class ObjTest {

    @Test
    public void testGet() {
        Obj obj = o(
                "a", o(
                        "b", 1
                ),
                "c", a(
                     o(
                             "d", 1
                     )   ,
                        o(
                                "d",2
                        )
                )
        );
        int i = obj.get("a.b", Integer.class);
        assertEquals(i, 1);
        i = obj.get("c.0.d", int.class);
        assertEquals(i, 1);
        i = obj.get("c.1.d", int.class);
        assertEquals(i, 2);
    }


    @Test
    public void testA() {
        Obj obj = o(
                "c", o(
                        "a", a(
                                1,2,3
                        )
                )
        );
        Arr arr = obj.a("c.a");
        assertNotNull(arr);
        assertEquals(arr.get(0), 1);
        assertEquals(arr.get(1), 2);
        assertEquals(arr.get(2), 3);
    }
}