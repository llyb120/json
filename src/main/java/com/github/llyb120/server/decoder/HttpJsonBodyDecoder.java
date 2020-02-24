package com.github.llyb120.server.decoder;

import com.github.llyb120.json.Arr;
import com.github.llyb120.json.Json;
import com.github.llyb120.json.Obj;
import com.github.llyb120.server.BufferPool;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class HttpJsonBodyDecoder implements Decoder{

    @Override
    public boolean decode(SocketChannel sc, DecoderContext data) throws IOException {
        if(!(data.data instanceof HttpContext)){
            return true;
        }
        //获取contentType
        HttpContext httpContext = (HttpContext) data.data;
        if(!httpContext.getRequestContentType().contains("application/json")){
            return true;
        }

        int len = httpContext.getRequestContentLength();
        if(len < 1){
            return true;
        }
        if(data.position != -1 && data.position <= data.limit){
            //获取剩余的内容
            byte[] buffer = BufferPool.get((len / 4096 + 1) * 4096);
            int half = data.limit - data.position;
            System.arraycopy(data.buffer, data.position, buffer, 0, half);
            int n = data.is.read(buffer, half, buffer.length - half);
            if(n < 1){
                return true;
            }
            String body = new String(buffer, 0, half + n);
            Object post = Json.parse(body);
            if(post instanceof Arr){
                httpContext.arrBody = (Arr) post;
            } else if(post instanceof Obj)
                httpContext.mapBody = (Obj) post;
        }
        return true;
    }

}
