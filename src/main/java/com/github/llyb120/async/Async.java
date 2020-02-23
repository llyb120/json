package com.github.llyb120.async;

import com.github.llyb120.json.error.AsyncException;
import com.github.llyb120.json.lambda.RetFunc;
import com.github.llyb120.json.lambda.VoidFunc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Async {

    private static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    public static Future execute(VoidFunc task){
        return cachedThreadPool.submit(() -> {
            try {
                task.call();
            } catch (Exception e) {
                throw new AsyncException(e);
            }
        });
    }

    public static Future execute(RetFunc task){
        return cachedThreadPool.submit(() -> {
            try {
                return task.call();
            } catch (Exception e) {
                throw new AsyncException(e);
            }
        });
    }
}
