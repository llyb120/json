package com.github.llyb120.json;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.llyb120.json.Json.*;
import static org.junit.Assert.*;

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
                        ),
                        o(
                                "d", 2
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
                                1, 2, 3
                        )
                )
        );
        Arr arr = obj.a("c.a");
        assertNotNull(arr);
        assertEquals(arr.get(0), 1);
        assertEquals(arr.get(1), 2);
        assertEquals(arr.get(2), 3);
    }


//    public Arr<Obj> ora(){
//    }
    
    public void tt() {
//        Arr arr;
//        for (int i = 0; i < arr.size(); i++) {
//        }
//        for (Obj obj : arr.oa()) {
//
//        }
//        for (Object o : arr.oa()) {
//
//        }
//
//        for (Object o : arr.oa()) {
//
//        }

//        mo(
//                "@for", a(1, 2, 3),
//                "${item}", "@if item > 0", o("${item}", "fk"), "@else", 2, "@end",
//                "@end"
//        ).render(o(
//                "data", a(1,2,3)
//        ));
    }

}