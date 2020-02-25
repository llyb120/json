package com.github.llyb120.server.decoder;

import com.github.llyb120.json.Arr;
import com.github.llyb120.json.Json;
import com.github.llyb120.json.Obj;
import com.github.llyb120.server.BufferPool;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class HttpJsonBodyDecoder implements HttpBodyDecoder {

    @Override
    public void decode(SocketChannel sc, HandlerContext data) throws IOException {
        if (!(data.data instanceof HttpContext)) {
            return;
        }
        //获取contentType
        HttpContext httpContext = (HttpContext) data.data;
        if (httpContext.responseStatus != -1) {
            return;
        }
        if (!httpContext.getRequestContentType().contains("application/json")) {
            return;
        }
        byte[] buffer = readBody(data);
        if (buffer == null) {
            return;
        }
        String body = new String(buffer, 0, data.position);
        Object post = Json.parse(body);
        if (post instanceof Arr) {
            httpContext.arrBody = (Arr) post;
        } else if (post instanceof Obj) {
            httpContext.mapBody = (Obj) post;
        }
    }

}
