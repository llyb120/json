package com.github.llyb120.json;

import org.testng.annotations.Test;

import static com.github.llyb120.json.Json.o;
import static org.testng.Assert.*;

public class JsonTest {

    @Test(priority = 0)
    public void testO() throws InterruptedException {
        Obj obj = o(
                "a", 1,
                "b", 2
        );
    }

    @Test(priority = 1)
    public void testCopy() {
    }
}