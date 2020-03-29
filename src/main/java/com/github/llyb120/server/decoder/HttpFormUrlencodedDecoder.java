package com.github.llyb120.server.decoder;

import com.github.llyb120.server.handler.HandlerContext;
import com.github.llyb120.server.http.HttpContext;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import static com.github.llyb120.json.Json.o;

public class HttpFormUrlencodedDecoder implements HttpBodyDecoder{

    @Override
    public void decode(SocketChannel sc, HandlerContext data) throws Exception {
        if(!(data.data instanceof HttpContext)){
            return;
        }
        HttpContext context = (HttpContext) data.data;
        if(context.responseStatus != -1){
            return;
        }
        String contentType = context.getRequestContentType();//"multipart/form-data";
        if(!contentType.contains("application/x-www-form-urlencoded")){
            return;
        }
        ByteBuffer buffer = readBody(sc, data);
        if (buffer == null) {
            return;
        }
        String query = StandardCharsets.UTF_8.decode(buffer).toString();//new String(buffer, 0, data.position);
        try{
            query = URLDecoder.decode(query, StandardCharsets.UTF_8.name());
            query = URLDecoder.decode(query, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
        }
        context.mapBody = o();
        int i = -1;
        for (String s : query.split("&")) {
            i = s.indexOf("=");
            if(i == -1){
                context.mapBody.put(s, "");
            } else {
                context.mapBody.put(s.substring(0,i), s.substring(i + 1));
            }
        }
    }
}
