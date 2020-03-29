package com.github.llyb120.server.handler;

import com.github.llyb120.json.Arr;
import com.github.llyb120.json.Obj;
import com.github.llyb120.server.BufferPool;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import static com.github.llyb120.json.Json.o;

public class HandlerContext implements AutoCloseable{
//    public InputStream is;
//    public OutputStream os;
//    public byte[] buffer;
    public ByteBuffer buffer;
//    public int position = -1;
//    public int limit = -1;
    public Object data;
    public boolean finished = false;

    public HandlerContext(){
//        buffer = BufferPool.get(4096);
    }

    @Override
    public void close() throws Exception {
//        if (buffer != null) {
//            BufferPool.release(buffer);
//        }
    }
}
