package com.github.llyb120.server.handler;

import java.nio.channels.SocketChannel;

public interface Handler extends BaseHandler{
    void handle(SocketChannel sc, HandlerContext data) throws Exception;
}
