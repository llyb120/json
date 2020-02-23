package com.github.llyb120.server.decoder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;

public class HttpHeadDecoder implements Decoder{

    @Override
    public boolean decode(SocketChannel sc, DecoderContext context) throws IOException {
        context.is = new BufferedInputStream(Channels.newInputStream(sc));
        int n = context.is.read(context.buffer);
        if (n < 0) {
            //socket error
            throw new IOException();
        }
        String str = new String(context.buffer, 0, n);
        int pos = str.indexOf("\r\n\r\n");
        if(pos == -1){
            //head error
            throw new IOException();
        }
        pos += 4;
        str = str.substring(0, pos);
        context.position = pos;
        String[] lines = str.split("\r\n");
        if(lines.length == 0){
            throw new IOException();
        }
        HttpContext httpContext = new HttpContext();
        context.data.put("httpContext", httpContext);
        //first must be path
        String[] arr = lines[0].split("\\s+");
        if(arr.length != 3){
            throw new IOException();
        }
        httpContext.method = HttpContext.HttpMethod.valueOf(arr[0].toUpperCase());
        httpContext.path = arr[1];
        for (int i = 1; i < lines.length; i++) {
            int _i = lines[i].indexOf(":");
            if(_i > -1){
                httpContext.headers.put(lines[i].substring(0, _i), lines[i].substring(_i + 1).trim());
            }
        }
        return true;
    }


}
