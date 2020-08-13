package com.github.llyb120.json;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
//    public void ...(){
//
//    }

    @Test
    public void ttt(){
        Obj item = o(1,2, new Foo());
        int d = 2;
    }

    static class Foo{
        public String bar = "a";
    }

    public void tt() {
        aaa(
            a(1,2,3)
        );
        List list = new ArrayList();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        Map map = new HashMap();
        map.put("a", 1);
        map.put("b", 2);
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

        a(
            yield(a(1,2,3), e -> {
                return e > 2 ? "up" : "down";
            })
        );
//        mo(
//                "@for", a(1, 2, 3),
//                "${item}", "@if item > 0", o("${item}", "fk"), "@else", 2, "@end",
//                "@end"
//        ).render(o(
//                "data", a(1,2,3)
//        ));
    }

    @Test
    public void pick() {
        Obj item = o(
                "a", 1,
                "b", o(
                        "c", a(1, o(
                                "d", "fff"
                        ))
                ),
                "array", a(
                        o(
                                "foo", o(
                                        "key1",1,
                                        "key2", 2,
                                        "count", 10,
                                        "test","aaa"
                                )
                        ),
                        o(
                                "foo", o(
                                        "key3",4,
                                        "key4", 5,
                                        "count", 100,
                                        "test", "bbb"
                                )
                        ),
                        o(
                                "foo", o("count", 1000)
                        ),
                        o(
                                "foo", o("count", 10000)
                        )
                )
        );
        int total = 0;
        for (Integer integer : item.pick("foo[key1]+foo[key3] count", int.class)) {
            total += integer;
        }
        assertEquals(total, 110);
        total = 0;
        for (Integer integer : item.pick("foo[test*=a]+foo[test*=b] count", int.class)) {
           total += integer;
        }
        assertEquals(total, 110);
        total = 0;
        for (Integer integer : item.pick("foo[test!=aaa]+foo[test*=b] count", int.class)) {
            total += integer;
        }
        assertEquals(total, 11100);

    }

}