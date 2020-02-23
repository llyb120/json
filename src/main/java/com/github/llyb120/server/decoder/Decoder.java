package com.github.llyb120.server.decoder;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface Decoder {
    boolean decode(SocketChannel sc, DecoderContext data) throws IOException;
}
