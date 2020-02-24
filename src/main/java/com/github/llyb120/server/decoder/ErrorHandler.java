package com.github.llyb120.server.decoder;

import java.nio.channels.SocketChannel;

public interface ErrorHandler extends BaseHandler {

    void handle(SocketChannel sc, HandlerContext context, Exception cause) throws Exception;
}
