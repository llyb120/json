package com.github.llyb120.server.writer;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

public class Mime {
    private static ConcurrentHashMap<String,String> map = new ConcurrentHashMap();

    static {
        String lines = ".ai        application/postscript\n" +
                ".eps        application/postscript\n" +
                ".exe        application/octet-stream\n" +
                ".doc        application/vnd.ms-word\n" +
                ".xls        application/vnd.ms-excel\n" +
                ".ppt        application/vnd.ms-powerpoint\n" +
                ".pps        application/vnd.ms-powerpoint\n" +
                ".pdf        application/pdf\n" +
                ".xml        application/xml\n" +
                ".odt        application/vnd.oasis.opendocument.text\n" +
                ".swf        application/x-shockwave-flash\n" +
                "\n" +
                "//压缩文件类型的 \n" +
                ".gz        application/x-gzip\n" +
                ".tgz        application/x-gzip\n" +
                ".bz        application/x-bzip2\n" +
                ".bz2        application/x-bzip2\n" +
                ".tbz        application/x-bzip2\n" +
                ".zip        application/zip\n" +
                ".rar        application/x-rar\n" +
                ".tar        application/x-tar\n" +
                ".7z        application/x-7z-compressed\n" +
                "\n" +
                "//文字类型 \n" +
                ".txt        text/plain\n" +
                ".php        text/x-php\n" +
                ".html        text/html\n" +
                ".htm        text/html\n" +
                ".js        text/javascript\n" +
                ".css        text/css\n" +
                ".rtf        text/rtf\n" +
                ".rtfd        text/rtfd\n" +
                ".py        text/x-python\n" +
                ".java        text/x-java-source\n" +
                ".rb        text/x-ruby\n" +
                ".sh        text/x-shellscript\n" +
                ".pl        text/x-perl\n" +
                ".sql        text/x-sql\n" +
                "\n" +
                "//图片类型的 \n" +
                ".bmp        image/x-ms-bmp\n" +
                ".jpg        image/jpeg\n" +
                ".jpeg        image/jpeg\n" +
                ".gif        image/gif\n" +
                ".png        image/png\n" +
                ".tif        image/tiff\n" +
                ".tiff        image/tiff\n" +
                ".tga        image/x-targa\n" +
                ".psd        image/vnd.adobe.photoshop\n" +
                "\n" +
                "//音频文件类型的 \n" +
                ".mp3        audio/mpeg\n" +
                ".mid        audio/midi\n" +
                ".ogg        audio/ogg\n" +
                ".mp4a        audio/mp4\n" +
                ".wav        audio/wav\n" +
                ".wma        audio/x-ms-wma\n" +
                "\n" +
                "//视频文件类型的 \n" +
                ".avi        video/x-msvideo\n" +
                ".dv        video/x-dv\n" +
                ".mp4        video/mp4\n" +
                ".mpeg        video/mpeg\n" +
                ".mpg        video/mpeg\n" +
                ".mov        video/quicktime\n" +
                ".wm        video/x-ms-wmv\n" +
                ".flv        video/x-flv\n" +
                ".mkv        video/x-matroska\n" +
                ".json application/json"
                ;
        String[] arr = lines.split("\n");
        for (String s : arr) {
            if (s.isEmpty()) {
                continue;
            }
            String[] ar = s.split("\\s+");
            if (ar.length != 2) {
                continue;
            }
            map.put(ar[0].substring(1), ar[1]);
        }
    }

    public static String defaultMime = "application/octet-stream";

    public static String get(File file){
        int point = file.getName().lastIndexOf(".");
        if (point == -1) {
            return defaultMime;
        }
        String str =  map.get(file.getName().substring(point + 1).toLowerCase());
        if (str == null) {
            return defaultMime;
        }
        return str;
    }
}
