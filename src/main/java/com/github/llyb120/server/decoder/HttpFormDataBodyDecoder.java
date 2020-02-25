package com.github.llyb120.server.decoder;

import com.github.llyb120.server.request.FormDataFile;

import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import static com.github.llyb120.json.Json.o;

public class HttpFormDataBodyDecoder implements HttpBodyDecoder {
    @Override
    public void decode(SocketChannel sc, HandlerContext data) throws Exception {
        if(!(data.data instanceof HttpContext)){
            return;
        }
        HttpContext context = (HttpContext) data.data;
        if(context.responseStatus != -1){
            return;
        }
        String contentType = context.getRequestContentType();//"multipart/form-data";
        if(!contentType.contains("multipart/form-data")){
            return;
        }
        int i = contentType.indexOf("boundary=");
        if(i < 0){
            return;
        }
        String token = contentType.substring(i + "boundary=".length());
        byte[] buffer = readBody(data);
        if (buffer == null) {
            return;
        }
            String body = new String(buffer, 0, data.position, StandardCharsets.ISO_8859_1);//"ISO-8859-1");
            String[] arr = body.split("\r\n--" + token);
            boolean first = true;
            if(arr.length == 0){
                return;
            }
            context.mapBody = o();
            FormDataFile tempFile = null;
            String key = null;
            for (int i1 = 0; i1 < arr.length - 1; i1++) {
                String s = arr[i1];
                int start = 0;
                if(first){
                    start = token.length() + 4;
                    first = false;
                }
                while(true){
                    int next = s.indexOf("\r\n", start);
                    //可以读出一行来
                    if(next > -1){
                        String line =  s.substring(start,next);
                        System.out.println(s);
                        start = next + 2;
                        if(line.isEmpty()){
                            //开始读取直
                            if (tempFile != null) {
                                byte[] value = s.substring(start).getBytes(StandardCharsets.ISO_8859_1);
                                tempFile.bytes = value;
                                if(key != null){
                                    context.mapBody.put(key ,tempFile);
                                }
                            } else {
                                String value = s.substring(start);
                                if (key != null) {
                                    context.mapBody.put(key, value);
                                }
                            }
                            key = null;
                            tempFile = null;
                        } else {
                            //读取属性
                            i = line.indexOf("filename=");
                            //是文件
                            if(i > -1){
                                tempFile = new FormDataFile();
                                tempFile.fileName = getValue(line, i + 9);
                            }
                            i = line.indexOf("name=");
                            if(i > -1){
                                key = getValue(line, i+5);
                            }
                        }
                    } else {
                        break;
                    }
                }
            }

//            Object post = Json.parse(body);
//            if(post instanceof Arr){
//                httpContext.arrBody = (Arr) post;
//            } else if(post instanceof Obj)
//                httpContext.mapBody = (Obj) post;
//            int d = 2;

    }

    private String getValue(String line, int pos){
        //检查下一个
        char c = line.charAt(pos);
        if(c == '"' || c == '\''){
            int last = line.indexOf(c, pos+1);
            if(last > -1){
                return line.substring(pos+1, last);
            }
        } else {
            int last = line.indexOf(" ", pos);
            if(last > -1){
                return line.substring(pos, last);
            } else {
                //直接取到末尾
                return line.substring(pos);
            }
        }
        return "";
    }
}
