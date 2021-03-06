package com.github.llyb120.server;

import com.github.llyb120.json.Util;
import com.github.llyb120.log.Log;
import com.github.llyb120.server.decoder.*;
import com.github.llyb120.server.handler.BaseHandler;
import com.github.llyb120.server.handler.ErrorHandler;
import com.github.llyb120.server.handler.Handler;
import com.github.llyb120.server.handler.HandlerContext;
import com.github.llyb120.server.nio.NioReader;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.*;

import static java.net.StandardSocketOptions.TCP_NODELAY;

public class NamiServer {

    private Selector selector;
    private ServerSocketChannel servChannel;
    private volatile boolean running = true;
    ExecutorService threadPool;// = Executors.newFixedThreadPool()
    int port;
    private Queue<BaseHandler> decoders = new ConcurrentLinkedQueue<>();

    NamiServer() {
    }

    public static NamiServerBuilder builder(){
        return new NamiServerBuilder();
    }

    public NamiServer addHandler(BaseHandler decoder) {
        decoders.add(decoder);
        return this;
    }

//    public NamiServer add

    public void start() throws Exception {
        //打开多路复用器
        selector = Selector.open();
        //打开服务器通道
        servChannel = ServerSocketChannel.open();
        //设置服务器通道为非阻塞模式
        servChannel.configureBlocking(false);
        //绑定端口,backlog指队列的容量，提供了容量限制的功能，避免太多客户端占用太多服务器资源
        //serverSocketChannel有一个队列，存放没有来得及处理的客户端,服务器每次accept，就会从队列中去一个元素。
        servChannel.socket().bind(new InetSocketAddress(port), 1024);
        //把服务器通道注册到多路复用器上，并监听阻塞事件
        servChannel.register(selector, SelectionKey.OP_ACCEPT);

        Log.info(String.format("boot server on port %d", port));

        threadPool.submit(() -> {
            loop();
        });
    }

    public void shutdown() {
        running = false;
        Util.close(selector);
    }

    private void loop() {
        while (running) {
            try {
                //多路复用器开始工作（轮询），选择已就绪的通道
                //等待某个通道准备就绪时最多阻塞1秒，若超时则返回。
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
//                    Async.execute(() -> {
                    try {
                        handleInput(key);
                    } catch (IOException e) {
                        key.cancel();
                        Util.close(key.channel());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (!key.isValid()) {
            return;
        }
        if (key.isAcceptable()) {
            //获取服务器通道
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            //执行阻塞方法(等待客户端资源)
            SocketChannel sc = ssc.accept();
            //设置为非阻塞模式
            sc.configureBlocking(false);
            //注册到多路复用器上，并设置为可读状态
            NioReader.runTask(sc);
//            sc.register(selector, SelectionKey.OP_READ);
        } else if (key.isReadable()) {
            //读取数据
//            key.cancel();
            SocketChannel sc = (SocketChannel) key.channel();
//            threadPool.submit(() -> {
                ByteBuffer buf = ByteBuffer.allocate(10);
                try {
                    int n;
                        n = sc.read(buf);
                        System.out.println(n);
                    if(n < 0){
                        key.cancel();
                        System.out.println("cancel");
                    }
//                    key.cancel();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(new String(buf.array()));
                System.out.println("read ！");
//                handle(sc);
//            });
        }
    }

    private void handle(SocketChannel sc) {
        HandlerContext context = null;
        try {
//            sc.configureBlocking(true);
            sc.setOption(TCP_NODELAY, true);
            context = new HandlerContext();
            Exception lastException = null;
            for (BaseHandler decoder : decoders) {
                if (decoder instanceof Decoder) {
                    if(context.finished){
                        continue;
                    }
                    try {
                        ((Decoder) decoder).decode(sc, context);
                    } catch (Exception e) {
                        lastException = e;
                    }
                } else if (decoder instanceof Handler) {
                    if(context.finished){
                        continue;
                    }
                    try {
                        ((Handler) decoder).handle(sc, context);
                    } catch (Exception e) {
                        lastException = e;
                    }
                } else if (decoder instanceof ErrorHandler && lastException != null) {
                    try {
                        ((ErrorHandler) decoder).handle(sc, context, lastException);
                    } catch (Exception e) {
                        lastException = e;
                    }
                }
            }
//            context.os.flush();
            if (lastException != null) {
                throw lastException;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Util.close(context);
            Util.close(sc);
        }
    }

}
