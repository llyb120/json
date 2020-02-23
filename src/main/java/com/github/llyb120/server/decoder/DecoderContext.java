package com.github.llyb120.server.decoder;

import com.github.llyb120.json.Obj;
import com.github.llyb120.server.BufferPool;

import java.io.InputStream;
import java.io.OutputStream;

import static com.github.llyb120.json.Json.o;

public class DecoderContext {
    public InputStream is;
    public OutputStream os;
    public byte[] buffer;
    public int position = -1;
    public Obj data = o();

    public DecoderContext(){
        buffer = BufferPool.get(4096);
    }
}
