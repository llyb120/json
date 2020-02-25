package com.github.llyb120;

import com.github.llyb120.server.NamiServer;
import com.github.llyb120.server.decoder.*;
import com.github.llyb120.server.request.FormDataFile;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class TestServerApp {
    public static void main(String[] args) throws Exception {
        NamiServer.builder()
                .port(8099)
                .maxThreads(1024)
                .build()
                .addHandler(new HttpHeadDecoder())
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
                        ctx.retValue = "fuck u 123";
                    }
                })
                .addHandler(new JsonObjectWriter())
                .start();
    }
}
