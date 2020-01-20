package com.github.llyb120.json.reflect;

import com.github.llyb120.json.Arr;
import com.github.llyb120.json.Obj;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import static com.github.llyb120.json.Json.a;
import static com.github.llyb120.json.Json.o;
import static com.github.llyb120.json.reflect.ReflectUtil.getType;
import static com.github.llyb120.json.reflect.ReflectUtil.getValues;
import static org.junit.Assert.*;

public class ReflectUtilTest {

    @Test
    public void test_getType() {
        int i = 1;
        Integer i2 = 2;
        assertEquals(getType(i), ClassType.INTEGER);
        assertEquals(getType(i2), ClassType.INTEGER);
        long l = 2;
        Long l2 = 2L;
        assertEquals(getType(l), ClassType.LONG);
        assertEquals(getType(l2), ClassType.LONG);
        Date date = new Date();
        assertEquals(getType(date), ClassType.DATE);
        BigDecimal bigDecimal = new BigDecimal(1);
        assertEquals(getType(bigDecimal), ClassType.BIG_DECIMAL);
    }

    class bean{
        public int a = 1;
        public String b = "2";
    }

    @Test
    public void test_getValues() {
        Obj obj = o(
           "a",1,
           "b",2
        );
        Map<String, Object> result = getValues(obj);
        assertEquals(result.get("a"), 1);
        assertEquals(result.get("b"), 2);

        result = getValues(new bean());
        assertEquals(result.get("a"), 1);
        assertEquals(result.get("b"), "2");

        int[] arr = new int[]{1,2,3,4,5};
        result = getValues(arr);
        assertEquals(result.get("0"), 1);
        assertEquals(result.get("4"), 5);

        Arr ar = a(1,2,3,4,5);
        result = getValues(ar);
        assertEquals(result.get("0"), 1);
        assertEquals(result.get("4"), 5);
    }
}