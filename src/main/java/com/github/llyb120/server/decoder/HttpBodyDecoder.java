package com.github.llyb120.server.decoder;

import com.github.llyb120.server.BufferPool;

import java.io.IOException;

public interface HttpBodyDecoder extends Decoder{

    default byte[] readBody(HandlerContext data) throws IOException {
        HttpContext context = (HttpContext) data.data;
        int len = context.getRequestContentLength();
        if(len < 1){
            return null;
        }
        if(data.position == -1 || data.position > data.limit) {
            return null;
        }
        BufferPool.release(data.buffer);
        byte[] buffer = data.buffer = BufferPool.get((len / 4096 + 1) * 4096);
        int half = data.limit - data.position;
        System.arraycopy(data.buffer, data.position, buffer, 0, half);
        int n = 0;
        //还有可读的内容
        if(len > data.limit){
            int left = len - data.limit;
            n = half;
            while(left > 0){
                int read = data.is.read(buffer, n, 4096);
                if(n < 1){
                    return null;
                }
                n += read;
                left -= read;
            }
        }
        data.position = data.limit = len;
        return buffer;
    }
}
