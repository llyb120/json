package com.github.llyb120.server.decoder;

import com.github.llyb120.server.BufferPool;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public interface HttpBodyDecoder extends Decoder{

    default ByteBuffer readBody(SocketChannel sc, HandlerContext data) throws IOException {
        HttpContext context = (HttpContext) data.data;
        int len = context.getRequestContentLength();
        if(len < 1){
            return null;
        }
//        if(data.position == -1 || data.position > data.limit) {
//            return null;
//        }
        ByteBuffer buffer; //= ByteBuffer.allocateDirect(len);
//        buffer.put(data.buffer);
        if(data.buffer.remaining() < len){
            //空余的不够
            buffer = ByteBuffer.allocateDirect(len);
            buffer.put(data.buffer);
            while(buffer.hasRemaining()){
                int n = sc.read(buffer);//, len - data.buffer.position());
                if(n <= 0){
                    break;
                }
            }
            buffer.flip();
            data.buffer = buffer;
        }
//        data.buffer = buffer;
//        BufferPool.release(data.buffer);
//        byte[] buffer = data.buffer = BufferPool.get((len / 4096 + 1) * 4096);
//        int half = data.limit - data.position;
//        System.arraycopy(data.buffer, data.position, buffer, 0, half);
//        int n = 0;
//        //还有可读的内容
//        if(len > data.limit){
//            int left = len - data.limit;
//            n = half;
//            while(left > 0){
//                int read = data.is.read(buffer, n, 4096);
//                if(n < 1){
//                    return null;
//                }
//                n += read;
//                left -= read;
//            }
//        }
//        data.position = data.limit = len;
//        buffer.flip();
        return data.buffer;
    }
}
