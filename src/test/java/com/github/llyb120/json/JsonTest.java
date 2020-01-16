package com.github.llyb120.json;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import static com.github.llyb120.json.Json.*;
import static org.junit.Assert.*;

public class JsonTest {

    @Test
    public void testO() {
        Obj obj = o(
                "const", 1,
                o("innerMapKey1", 1, "innerMapKey2", 2),
                "vIsObj", o(),
                4, undefined,
                a(1,2,3).yield(e -> o(e,true)),
                o("aa",1, "bb", 2).yield((k,v) -> {
                    int vv = (int) v;
                    return o(k, ++vv);
                })
        );
        assertEquals(obj.get("aa"), 2);
        assertEquals(obj.get("bb"), 3);
        assertFalse(obj.containsKey("4"));
        assertEquals(obj.get("innerMapKey1"), 1);
        assertEquals(obj.get("innerMapKey2"), 2);
        assertEquals(obj.get("1"), true);
        assertEquals(obj.get("2"), true);
        assertEquals(obj.get("3"), true);
        assertTrue(obj.o("vIsObj").isEmpty());
        assertEquals(obj.get("const"), 1);

        System.out.println(obj.toString());
    }

    @Test
    public void testCopy() {
    }

    @Test
    public void parse() throws IOException {
        RandomAccessFile raf = new RandomAccessFile("E:\\work\\json\\test\\1.json5", "r");
        byte[] bs = new byte[(int) raf.length()];
        raf.read(bs);
        Obj item = Json.parse(bs);
        System.out.println(item);
    }

    @Test
    public void testYield() {
        Arr arr = a(
               yield(a(1, 2, 3), e -> e > 2 ? "up" : "down"),
                "ri","jian"
        );
//
//        Arr arr2 = a();
//        for (Integer integer : a(1, 2, 3)) {
//            arr2.add(integer > 2 ? "up" : "down");
//        }
//        arr2.add("ri");
//        arr2.add("jian");

        System.out.println(arr.toString());
        assertEquals(arr.get(0), "down");
        assertEquals(arr.get(1), "down");
        assertEquals(arr.get(2), "up");
        arr = a(
                yield(a(1,2,3), e -> a(e,"fuck")),
                "ri","jian"

        );
        System.out.println(arr.toString());
        assertEquals(arr.size(), 3 + 2);
        arr = aaa(
                yield(a(1,2,3), e -> a(e,"fuck")),
                "ri","jian"
        );
        System.out.println(arr.toString());
        assertEquals(arr.size(), 6 + 2);
    }

    @Test
    public void testA() {
        Arr arr = a(1, 2, 3);
        assertEquals(arr.size(), 3);

        arr = a(1,2, "...", a(1,2,3));
        assertEquals(arr.size(), 5);
    }

    @Test
    public void test_bo(){
        MongoClient client = new MongoClient("47.94.97.138");
        MongoCollection<Document> col = client.getDatabase("test")
                .getCollection("test");
//                .insertOne((bo("fuck", o("fff","ff"))));
        Document doc = bo("fuck", "1");
        doc.put("ohohoh", o("fuck", "123321"));


        col.insertOne(doc);
    }

    static class Ro{
        public int a = 1;
        public Map<String, Object> map;
        private String b;

        public void setB(String b) {
            this.b = b;
        }
    }

    @Test
    public void testRo() {
        Map map  = new HashMap<>();
        Obj obj = ro(map);
        map.put("a", 1);
        assertEquals(obj.ii("a"), 1);
        obj.put("b", 2);
        assertEquals(map.get("b"), 2);

        Arr<Integer> arr = a(1, 2, 3);
        System.out.println(ro(arr).toString());


//        Ro r = new Ro();
//        Obj obj2 = ro(r);
//
//        obj2.put("a", 123);
//        assertEquals(r.a, 123);
//        obj2.put("b", "jian");
//        assertEquals(r.b, "jian");
//
//        obj2.oo("map").put("ajian", "1");
//        assertEquals(r.map.get("ajian"), "1");
    }


    @Test
    public void testPick(){
        Arr<Integer> list = a(
                o("c", 1),
                o("c", 2),
                o("c", 3),
                o("c", 4),
                o("c", 5)
        ).pick("*.c", Integer.class);
        for (Integer integer : list) {
            System.out.println(integer);
        }
    }
}