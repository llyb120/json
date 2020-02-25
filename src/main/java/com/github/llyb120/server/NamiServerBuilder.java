package com.github.llyb120.server;

import java.util.concurrent.Executors;

public class NamiServerBuilder {
    int port = 8080;
    int maxThreads = 1024;

    public NamiServerBuilder port(int port){
        this.port = port;
        return this;
    }

    public NamiServerBuilder maxThreads(int num){
        this.maxThreads = num;
        return this;
    }

    public NamiServer build(){
        NamiServer server = new NamiServer();
        server.port = port;
        server.threadPool = Executors.newFixedThreadPool(maxThreads + 1);
        return server;
    }
}
