package com.github.llyb120;

import com.github.llyb120.server.NamiServer;
import com.github.llyb120.server.decoder.*;

import java.nio.channels.SocketChannel;

public class TestServerApp {
    public static void main(String[] args) throws Exception {
        NamiServer.builder()
                .port(8080)
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
                        ctx.retValue = "fuck u 123";
                    }
                })
                .addHandler(new JsonObjectWriter())
                .start();
    }
}
