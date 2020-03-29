package com.github.llyb120.server.http;

import com.github.llyb120.server.handler.Handler;
import com.github.llyb120.server.handler.HandlerContext;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * http 反向代理
 */
public class HttpProxyHandler implements Handler {

    private String path;
    private String targetPath;
    private String host;
    private int port;

    public HttpProxyHandler(String path, String target) {
        if(!target.endsWith("/")){
           target += "/";
        }
        Pattern pattern = Pattern.compile("^https?://([^/:]+)(\\:\\d+)?(.+)$");
        Matcher matcher = pattern.matcher(target);
        if(!matcher.find()){
            throw new RuntimeException();
        }
        host = matcher.group(1);
        String port = matcher.group(2);
        if (port == null) {
            port = "80";
        }
        this.port = Integer.parseInt(port);
        targetPath = matcher.group(3);
        this.path = path;
//        this.target = target;
    }

    @Override
    public void handle(SocketChannel sc, HandlerContext data) throws Exception {
        if(!(data.data instanceof HttpContext)){
            return;
        }
        HttpContext context = (HttpContext) data.data;
        String path = context.path;
        if(!path.endsWith("/")){
            path = path + "/";
        }
        if(!path.startsWith(this.path)){
           return;
        }
        try {
            SocketChannel clntChan = SocketChannel.open();
            clntChan.configureBlocking(true);
            clntChan.connect(new InetSocketAddress(host, port));
//            clntChan.register(selector, SelectionKey.OP_READ);
            while (!clntChan.finishConnect()){
            }
            //写入
            StringBuilder sb = new StringBuilder();
            sb.append(context.method.name());
            sb.append(" ");
            String newPath = context.path.substring(this.path.length());
            if(newPath.startsWith("/")){
                newPath = newPath.substring(1);
            }
            sb.append(targetPath + newPath);
            sb.append(" ");
            sb.append("HTTP/1.1");
            sb.append("\r\n");
            for (Map.Entry<String, Object> entry : context.requestHeaders.entrySet()) {
                sb.append(entry.getKey());
                sb.append(": ");
                if(entry.getKey().equals("Host")){
                    sb.append(host);
                    sb.append(":");
                    sb.append(port);
                } else {
                    sb.append(entry.getValue());
                }
                sb.append("\r\n");
            }
            sb.append("\r\n");
            clntChan.write(ByteBuffer.wrap(sb.toString().getBytes()));
            int left = context.getRequestContentLength() - data.buffer.remaining();
            clntChan.write(data.buffer);
            while(left > 0){
                data.buffer.flip();
                sc.read(data.buffer);
                data.buffer.flip();
                left -= data.buffer.remaining();
                clntChan.write(data.buffer);
            }
            data.buffer.flip();
            //读取一次
            clntChan.read(data.buffer);
            data.buffer.flip();
            String str = StandardCharsets.ISO_8859_1.decode(data.buffer).toString();
            //content-length;
            int i = str.indexOf("Content-Length");
            int len = 0;
            if(i > -1){
                i += "Content-Length".length();
                i = str.indexOf(":", i);
                if(i > -1){
                    int end = str.indexOf("\r\n", i);
                    if(end > -1){
                        len = Integer.parseInt(str.substring(i + 1, end).trim());
                    }
                }
            }
            int limit = str.indexOf("\r\n\r\n");
            if(limit > -1){
                limit += 4;
            }
            left = len - (str.length() - limit);
            sc.write(ByteBuffer.wrap(str.getBytes()));
            while(left > 0){
                data.buffer.flip();
                clntChan.read(data.buffer);
                data.buffer.flip();
                left -= data.buffer.remaining();
                sc.write(data.buffer);
            }
            System.out.println(left);
            data.finished = true;
////            n = clntChan.read(buffer);
////            n = clntChan.read(buffer);
////            n = clntChan.read(buffer);
////            n = clntChan.read(buffer);
////            n = clntChan.read(buffer);
////            buffer.flip();
//            System.out.println(StandardCharsets.UTF_8.decode(buffer).toString());
//            System.out.println("已连接！");
        }catch (Exception e){
            e.printStackTrace();
        }
        //如果命中了
//        if()
    }


}
