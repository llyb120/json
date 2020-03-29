package com.github.llyb120.server.writer;

import com.github.llyb120.server.handler.Handler;
import com.github.llyb120.server.http.HttpContext;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;

public interface HttpWriter extends Handler {

    static String OK = "HTTP/1.1 200 OK";//.getBytes();
    static String LINE = "\r\n";//.getBytes();

    default String getHeaders(HttpContext context) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("HTTP/1.1 %d OK", context.responseStatus));
        sb.append(LINE);
        context.responseHeaders.put("Connection","close");
        for (Map.Entry<String, Object> entry : context.responseHeaders.entrySet()) {
            sb.append(entry.getKey());
            sb.append(": ");
            sb.append(entry.getValue());
            sb.append(LINE);
        }
        sb.append(LINE);
        return sb.toString();
    }

    default void write(SocketChannel sc, String header, byte[] body) throws IOException {
        byte[] bs = header.getBytes();
        ByteBuffer buffer = ByteBuffer.allocateDirect(bs.length + body.length);
        buffer.put(bs);
        buffer.put(body);
        buffer.flip();
        sc.write(buffer);
    }
}
