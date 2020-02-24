package com.github.llyb120.server.decoder;

import com.github.llyb120.json.Arr;
import com.github.llyb120.json.Obj;
import com.github.llyb120.server.BufferPool;

import java.io.InputStream;
import java.io.OutputStream;

import static com.github.llyb120.json.Json.o;

public class HandlerContext {
    public InputStream is;
    public OutputStream os;
    public byte[] buffer;
    public int position = -1;
    public int limit = -1;
    public Object data;

    public HandlerContext(){
        buffer = BufferPool.get(4096);
    }
}
