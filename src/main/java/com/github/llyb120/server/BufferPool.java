package com.github.llyb120.server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class BufferPool {
    private static ReentrantLock lock = new ReentrantLock();
    private static Map<Integer, LinkedList<byte[]>> pool = new HashMap<>();

    public static byte[] get(int size){
        lock.lock();
        try{
            LinkedList<byte[]> list = getList(size);//pool.get(size);
            if(list.isEmpty()){
                byte[] bs = new byte[size];
                list.add(bs);
                return bs;
            } else {
                return list.removeFirst();
            }
        } finally {
            lock.unlock();
        }
    }

    public static void release(byte[] bs){
        lock.lock();
        try{
            LinkedList<byte[]> list = getList(bs.length);
            list.addLast(bs);
        } finally {
            lock.unlock();
        }
    }

    private static LinkedList<byte[]> getList(int size){
        LinkedList<byte[]> list = pool.get(size);
        if (list == null) {
            list = new LinkedList<>();
            pool.put(size, list);
        }
        return list;
    }
}
