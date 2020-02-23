package com.github.llyb120.server;

import cn.hutool.http.HttpUtil;
import com.github.llyb120.server.decoder.HttpHeadDecoder;
import org.junit.Test;

import static org.junit.Assert.*;

public class NamiServerTest {
    @Test
    public void test() throws Exception {
        NamiServer server = new NamiServer(8099, 1024);
        server
                .addDecoder(new HttpHeadDecoder())
                .start();

        Thread.sleep(1000);
        HttpUtil.get("http://127.0.0.1:8099/fuck");
    }

}