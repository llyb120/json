package com.github.llyb120.server;

import com.github.llyb120.server.decoder.*;
import org.junit.Test;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class NamiServerTest {
    @Test
    public void test() throws Exception {
        NamiServer server = new NamiServer(8099, 1024);
        server
                .addHandler(new HttpHeadDecoder())
                .addHandler(new HttpJsonBodyDecoder())
                .addHandler(new Handler() {
                    @Override
                    public void handle(SocketChannel sc, HandlerContext data) throws Exception {
                        HttpContext ctx = (HttpContext) data.data;
                        ctx.retValue = "fuck u 123";
                    }
                })
                .addHandler(new JsonObjectWriter())
                .start();

//        Thread.sleep(1000);
////        HttpUtil.get("http://127.0.0.1:8099/fuck");
//        HttpUtil.createPost("http://127.0.0.1:8099/fuck")
//                .header("content-type", "application/json")
//                .body(o(
//                        "fuck", RandomUtil.randomString(4096)
//                ).toString())
//                .execute();

        Thread.sleep(100000);
    }

}