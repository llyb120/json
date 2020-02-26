package com.github.llyb120.server.writer;

import com.github.llyb120.json.Json;
import com.github.llyb120.server.decoder.Decoder;
import com.github.llyb120.server.decoder.HandlerContext;
import com.github.llyb120.server.decoder.HttpContext;
import com.github.llyb120.server.request.FormDataFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonObjectWriter implements HttpWriter {

    @Override
    public void handle(SocketChannel sc, HandlerContext data) throws Exception {
        if(!(data.data instanceof HttpContext)){
            return;
        }
        HttpContext httpContext = (HttpContext) data.data;
        if(httpContext.responseStatus != -1){
            return;
        }
        if(httpContext.retValue instanceof File || httpContext.retValue instanceof FormDataFile){
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bs = null;//new byte[0];
        if(httpContext.retValue instanceof String){
            bs = ((String) httpContext.retValue).getBytes(StandardCharsets.UTF_8);
        } else if(httpContext.retValue instanceof byte[]){
            bs = (byte[]) httpContext.retValue;
        } else {
            bs = Json.stringify(httpContext.retValue).getBytes(StandardCharsets.UTF_8);
        }
        httpContext.responseStatus = 200;
        httpContext.responseHeaders.put("Content-Length", bs.length);
        httpContext.responseHeaders.put("Content-Type", "application/json; charset=utf-8");
        write(sc, getHeaders(httpContext), bs);
    }

}
