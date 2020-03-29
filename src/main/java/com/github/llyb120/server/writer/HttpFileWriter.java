package com.github.llyb120.server.writer;

import com.github.llyb120.server.handler.HandlerContext;
import com.github.llyb120.server.http.HttpContext;
import com.github.llyb120.server.request.FormDataFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class HttpFileWriter implements HttpWriter {

    @Override
    public void handle(SocketChannel sc, HandlerContext data) throws Exception {
        if(!(data.data instanceof HttpContext)){
            return;
        }
        HttpContext httpContext = (HttpContext) data.data;
        if(httpContext.responseStatus != -1){
            return;
        }
        byte[] bytes = null;
        String mime = null;
        String name = null;
        if(httpContext.retValue instanceof File){
            try(
                    RandomAccessFile raf = new RandomAccessFile((File) httpContext.retValue, "r");
                    ){
                bytes = new byte[(int) raf.length()];
                raf.readFully(bytes);
                httpContext.responseStatus = 200;
                httpContext.responseHeaders.put("Content-Length", bytes.length);
                mime = Mime.get((File) httpContext.retValue);
                name = ((File) httpContext.retValue).getName();
            } catch (IOException e){
                httpContext.responseStatus = 404;
            }
        } else if(httpContext.retValue instanceof FormDataFile){
            bytes = ((FormDataFile) httpContext.retValue).bytes;
            httpContext.responseStatus = 200;
            httpContext.responseHeaders.put("Content-Length", bytes.length);
            mime = "application/octet-stream";
            name = ((FormDataFile) httpContext.retValue).fileName;
        }
        if (bytes == null || mime == null || name == null) {
            return;
        }
        if(mime.equals(Mime.defaultMime)){
            //补充下载文件名
            name = URLEncoder.encode(name, StandardCharsets.UTF_8.name());
            httpContext.responseHeaders.put("Content-Disposition","attachment;filename=\"" + name + "\"");
        }
        httpContext.responseHeaders.put("Content-Type", mime);//getContentType(path));
        write(sc, getHeaders(httpContext), bytes);
    }

}
