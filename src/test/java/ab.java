
import com.github.llyb120.json.*;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.llyb120.json.Json.*;

public class ab {

//    public void ttt(List<Document> arg){
//
//    }
//
//    public void fff(String a){
//
//    }
//
//    public void b(){
//        ttt(a().toBson());
//        fff(a().toBson());
//    }

    public static class b{
        public String bcc = "1";
        public String fff = "2";
        private Map<String,Integer> ffd;

        public String getFff() {
            return fff;
        }

        public void setFff(String fff) {
            bcc = fff;
        }

        public void setFfd(Map<String, Integer> ffd) {
            this.ffd = ffd;
        }

        public Map<String, Integer> getFfd() {
            return ffd;
        }
    }
    public static class c{
        public String fff = "ddd";
        public Map<String, Long> ffd;
    }

    @Test
    public void test() throws Exception {
//        Class clz1 = getClz();
//        clz1.newInstance();
//        Class clz2 = getClz();
//        clz2.newInstance();
        Obj oo = o(
                "bcc", 222,
                "fff", 333,
                "ffd",o(
                        "a","12345"
                )
        );
        b ins = Json.cast(oo, b.class);
        c cc = Json.cast(ins, c.class);
        int d = 2;

        Map map = new HashMap();
        Obj obj = ro(map);
        Obj cp = ooo(map);
        map.put("a", 1);
        obj.put("b", 2);
        cp.put("c", 3);
        cp.ss("fuck2");
        obj.ss("fuck");
        System.out.println("map is " + Json.stringify(map));
        System.out.println("obj is " + Json.stringify(obj));
        System.out.println("cp is " + Json.stringify(cp));

        obj = o(
                "a", a(
                        o(
                                "b", 1
                        )
                )
        );
        obj.s("a.0.b");

        Arr arr = Json.parse("[1.0,2,3]");
        arr.add(1L);
        System.out.println(arr.toString());
        int e = 2;
    }

    public Class getClz() throws Exception{
        ClassLoader loader;
        RandomAccessFile raf = new RandomAccessFile("E:\\work\\json\\target\\classes\\com\\github\\llyb120\\json\\T.class", "rw");
        byte[] bs = new byte[(int) raf.length()];
        int len = raf.read(bs);
        Class<?> clz = ReflectUtil.unsafe.defineAnonymousClass(Class.forName("com.github.llyb120.json.T"), bs, new int[][]{new int[]{1, 2, 3}});
        return clz;
    }
}
