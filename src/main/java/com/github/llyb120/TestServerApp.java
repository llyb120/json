package com.github.llyb120;

import com.github.llyb120.server.NamiServer;
import com.github.llyb120.server.decoder.*;
import com.github.llyb120.server.handler.Handler;
import com.github.llyb120.server.handler.HandlerContext;
import com.github.llyb120.server.http.HttpContext;
import com.github.llyb120.server.http.HttpProxyHandler;
import com.github.llyb120.server.request.FormDataFile;
import com.github.llyb120.server.writer.HttpFileWriter;
import com.github.llyb120.server.writer.JsonObjectWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.SocketChannel;

public class TestServerApp {
    public static void main(String[] args) throws Exception {
        NamiServer.builder()
                .port(8099)
                .maxThreads(1024)
                .build()
                .addHandler(new HttpHeadDecoder())
                .addHandler(new HttpProxyHandler("/hz", "http://47.94.97.138"))
                .addHandler(new HttpJsonBodyDecoder())
                .addHandler(new HttpFormDataBodyDecoder())
                .addHandler(new HttpFormUrlencodedDecoder())
                .addHandler(new Handler() {
                    @Override
                    public void handle(SocketChannel sc, HandlerContext data) throws Exception {
                        HttpContext ctx = (HttpContext) data.data;
                        if (ctx.mapBody != null) {
                            FormDataFile formDataFile = (FormDataFile) ctx.mapBody.get("file");
                            if (formDataFile != null) {
                                FileOutputStream fos = new FileOutputStream("d:/1.zip");;
                                fos.write(formDataFile.bytes);
                                fos.flush();
                                fos.close();
                            }
                        }
                        ctx.retValue = new File("d:/日狗.fuck");//"fuck u 123";
                    }
                })
                .addHandler(new JsonObjectWriter())
                .addHandler(new HttpFileWriter())
                .start();
    }
}
