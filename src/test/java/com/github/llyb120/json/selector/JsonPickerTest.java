package com.github.llyb120.json.selector;

import com.github.llyb120.json.Arr;
import com.github.llyb120.json.Json;
import com.github.llyb120.json.Obj;
import org.junit.Test;

import java.util.List;

import static com.github.llyb120.json.Json.a;
import static com.github.llyb120.json.Json.o;
import static org.junit.Assert.*;

public class JsonPickerTest {

    @Test
    public void pick() {
        Obj par;
        Obj item = o(
                "a", 1,
                "b", o(
                        "c", a(1, par = o(
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
//        par.put("par", par);
//
//        List<String> items = JsonPicker.pick("b c 0,a", item, String.class);
//        assertEquals(items.size(), 2);

        List<String> items = JsonPicker.pick("array foo[key1]+foo[key3]", item, String.class);
        int d = 2;
    }

    @Test
    public void pick2(){
        String str = "{\"_id\":\"5e5cbd00a2ce611b47c081de\",\"id\":\"1\",\"username\":\"admin\",\"password\":\"2\",\"su\":1,\"true_name\":\"系统管理员\",\"phone\":\"15094011010\",\"orgs\":[{\"id\":\"1126767593877667840\",\"type\":\"ROLE\"},{\"id\":\"2000007\",\"type\":\"ROLE\"},{\"id\":\"1185103421875818496\",\"type\":\"ROLE\"},{\"id\":\"1059767604954005504\",\"type\":\"ROLE\"},{\"id\":\"222\",\"type\":\"DEP\"}]}";
        Obj obj = Json.parse(str);
        Arr<String> item = obj.pick("orgs [type=DEP] id", String.class);
        System.out.println("fff");
    }
}