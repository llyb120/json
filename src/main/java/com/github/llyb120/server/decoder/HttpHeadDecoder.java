package com.github.llyb120.server.decoder;

import com.github.llyb120.server.handler.HandlerContext;
import com.github.llyb120.server.http.HttpContext;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class HttpHeadDecoder implements Decoder{

    @Override
    public void decode(SocketChannel sc, HandlerContext context) throws IOException {
        ByteBuffer buffer = context.buffer = ByteBuffer.allocateDirect(4096);
        int n = sc.read(buffer);
        if(n < 0){
            return;
        }
        buffer.flip();
        String str = StandardCharsets.ISO_8859_1.decode(buffer).toString();//buffer.asCharBuffer().toString();
        int pos = str.indexOf("\r\n\r\n");
        if(pos == -1){
            return;
        }
        pos += 4;
        buffer.position(pos);
        str = str.substring(0, pos);
//        context.position = pos;
//        context.limit = n;
        String[] lines = str.split("\r\n");
        if(lines.length == 0){
            return;
        }
        HttpContext httpContext = new HttpContext();
        context.data = httpContext;
//        context.data.put("httpContext", httpContext);
        //first must be path
        String[] arr = lines[0].split("\\s+");
        if(arr.length != 3){
            return;
        }
        httpContext.method = HttpContext.HttpMethod.valueOf(arr[0].toUpperCase());
        //decode path
        httpContext.path = arr[1];
        handlePath(httpContext);
        for (int i = 1; i < lines.length; i++) {
            int _i = lines[i].indexOf(":");
            if(_i > -1){
                httpContext.requestHeaders.put(lines[i].substring(0, _i), lines[i].substring(_i + 1).trim());
            }
        }
    }

    public void handlePath(HttpContext context){
        int i = context.path.indexOf("?");
        if (i == -1) {
            return;
        }
        String query = context.path.substring(i+1);
        try{
            query = URLDecoder.decode(query, StandardCharsets.UTF_8.name());
            query = URLDecoder.decode(query, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
        }
        context.path = context.path.substring(0, i);

        for (String s : query.split("&")) {
            i = s.indexOf("=");
            if(i == -1){
                context.query.put(s, "");
            } else {
                context.query.put(s.substring(0,i), s.substring(i + 1));
            }
        }
    }


}
