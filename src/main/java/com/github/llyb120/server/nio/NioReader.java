package com.github.llyb120.server.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class NioReader {
    private Selector selector;
    private ByteBuffer buffer;
    private SocketChannel sc;

    private static ExecutorService executor = Executors.newCachedThreadPool();
    private static Queue<NioReader> readers = new ConcurrentLinkedQueue<>();
    private static ReentrantLock lock;

    public NioReader() throws IOException {
        this.selector = Selector.open();
        buffer = ByteBuffer.allocate(4096);
    }

    public void start(){
        executor.submit(() -> {
            while(true){
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                while(it.hasNext()){
                    SelectionKey key = it.next();
                    it.remove();
                    if(!key.isValid()){
                        continue;
                    }
                    int n = sc.read(buffer);
                    if(n > 0){
                        //空间不够
                        if(!buffer.hasRemaining()){
                            ByteBuffer temp = buffer;
                            buffer = ByteBuffer.allocate(buffer.capacity() * 2);
                            temp.flip();
                            buffer.put(temp);
                        }
                    } else if(n < 0){
//                        key.cancel();
                        System.out.println("end");
                    }
                }
            }
        });
    }

    public static void runTask(SocketChannel sc) throws IOException {
        //得到一个可用的reader
        NioReader reader = readers.poll();
        if (reader == null) {
            reader = new NioReader();
        }
        reader.sc = sc;
        sc.register(reader.selector,SelectionKey.OP_READ );
        reader.start();
    }

}
