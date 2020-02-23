package com.github.llyb120.server.decoder;

import com.github.llyb120.json.Obj;

import static com.github.llyb120.json.Json.o;

public class HttpContext {
    public Obj headers = o();
    public HttpMethod method;
    public String path;

    public enum HttpMethod{
        GET,POST,OPTION,DELETE,HEAD, UNKNOWN;
    }
}
