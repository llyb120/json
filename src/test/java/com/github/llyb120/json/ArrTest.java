package com.github.llyb120.json;

import org.junit.Test;

import static com.github.llyb120.json.Json.a;
import static com.github.llyb120.json.Json.o;
import static org.junit.Assert.*;

public class ArrTest {

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
                                        "key2", 2
                                )
                        ),
                        o(
                                "foo", o(
                                        "key3",1,
                                        "key4", 2
                                )
                        )
                )
        );
        Arr<Obj> items = item.pick("array foo[key1]+foo[key3]", Obj.class);
        System.out.println(items.toString());
    }
}