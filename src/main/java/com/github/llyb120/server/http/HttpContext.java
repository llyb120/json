package com.github.llyb120.server.http;

import com.github.llyb120.json.Arr;
import com.github.llyb120.json.Obj;

import static com.github.llyb120.json.Json.o;

public class HttpContext {
    //request
    public Obj requestHeaders = o();
    public HttpMethod method;
    public String path;
    public Obj mapBody;
    public Arr arrBody;
    public Obj query = o();

    //response
    public int responseStatus = -1;
    public Obj responseHeaders = o();

    //返回给前端的值
    public Object retValue;

    public enum HttpMethod{
        GET,POST,OPTION,DELETE,HEAD, UNKNOWN;
    }

    public String getRequestContentType(){
        String type = requestHeaders.s("Content-Type", "");
        if(type.isEmpty()){
            type = requestHeaders.s("content-type", "");
        }
        return type;
    }

    public int getRequestContentLength(){
        int i = requestHeaders.i("Content-Length", 0);
        if(i == 0){
            i = requestHeaders.i("content-length", 0);
        }
        return i;
    }
}
