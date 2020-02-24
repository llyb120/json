package com.github.llyb120.server;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import com.github.llyb120.server.decoder.HttpHeadDecoder;
import com.github.llyb120.server.decoder.HttpJsonBodyDecoder;
import org.junit.Test;

import static com.github.llyb120.json.Json.o;
import static org.junit.Assert.*;

public class NamiServerTest {
    @Test
    public void test() throws Exception {
        NamiServer server = new NamiServer(8099, 1024);
        server
                .addDecoder(new HttpHeadDecoder())
                .addDecoder(new HttpJsonBodyDecoder())
                .start();

        Thread.sleep(1000);
//        HttpUtil.get("http://127.0.0.1:8099/fuck");
        HttpUtil.createPost("http://127.0.0.1:8099/fuck")
                .header("content-type", "application/json")
                .body(o(
                        "fuck", RandomUtil.randomString(4096)
                ).toString())
                .execute();
    }

}