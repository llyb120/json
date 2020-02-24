package com.github.llyb120.server.decoder;

import java.nio.channels.SocketChannel;

public interface Decoder extends BaseHandler {
    void decode(SocketChannel sc, HandlerContext data) throws Exception;
}
