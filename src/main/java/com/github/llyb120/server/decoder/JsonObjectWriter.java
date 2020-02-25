package com.github.llyb120.server.decoder;

import com.github.llyb120.json.Json;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonObjectWriter implements Decoder{

    static byte[] OK = "HTTP/1.1 200 OK".getBytes();
    static byte[] LINE = "\r\n".getBytes();
    @Override
    public void decode(SocketChannel sc, HandlerContext data) throws Exception {
        if(!(data.data instanceof HttpContext)){
            return;
        }
        HttpContext httpContext = (HttpContext) data.data;
        if(httpContext.retValue == null){
            return;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(OK);
        baos.write(LINE);
        byte[] bs = null;//new byte[0];
        if(httpContext.retValue instanceof String){
            bs = ((String) httpContext.retValue).getBytes(StandardCharsets.UTF_8);
        } else if(httpContext.retValue instanceof byte[]){
            bs = (byte[]) httpContext.retValue;
        } else {
            bs = Json.stringify(httpContext.retValue).getBytes(StandardCharsets.UTF_8);
        }
        httpContext.responseHeaders.put("Content-Length", bs.length);
        httpContext.responseHeaders.put("Content-Type", "application/json; charset=utf-8");
        for (Map.Entry<String, Object> entry : httpContext.responseHeaders.entrySet()) {
            baos.write((entry.getKey() + ": " + entry.getValue()).getBytes());
            baos.write(LINE);
        }
        baos.write(LINE);
        baos.write(bs);

        ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());
        sc.write(buffer);
    }

}
