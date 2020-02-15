
import com.github.llyb120.json.*;
import com.github.llyb120.json.reflect.ReflectUtil;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
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

        b ins2 = copy(ins);
        int e = 2;


        Arr list = a(
                o("a", 1),
                o("a", 2),
                o("a", 3),
                o("a", 4)
                );
        for (Object o : list) {
            
        }
//        List<Obj> list2 = la(
//                o("a", 1),
//                o("a", 2),
//                o("a", 3),
//                o("a", 4)
//        );
//        for (Obj obj1 : list2) {
//
//        }

//        List<Object> sd = la(1, null, "f", new Date(), new Object());
    }

    public Class getClz() throws Exception{
        ClassLoader loader;
        RandomAccessFile raf = new RandomAccessFile("E:\\work\\json\\target\\classes\\com\\github\\llyb120\\json\\T.class", "rw");
        byte[] bs = new byte[(int) raf.length()];
        int len = raf.read(bs);
        Class<?> clz = ReflectUtil.unsafe.defineAnonymousClass(Class.forName("com.github.llyb120.json.T"), bs, new int[][]{new int[]{1, 2, 3}});
        return clz;
    }

    @Test
    public void t(){
        String str = "{\"bpmId\":\"5e400124cb5c623b2983319d\",\"currentNodes\":[{\"nodeName\":\"开始\",\"msg\":null,\"minTimeout\":\"2020-02-14 20:20:00\",\"mainUsers\":{\"1\":\"系统管理员\"},\"maxTimeout\":\"2020-02-14 20:20:00\",\"supportUsers\":{},\"logId\":\"5e4690706c24e3f4cba75a93\",\"nodeId\":\"StartEvent_1thklpd\"}],\"comments\":null,\"parInsId\":null,\"pubUName\":\"系统管理员\",\"bpmName\":\"啛啛喳喳(2020-02-14 20:20:00)\",\"depName\":null,\"pubUid\":\"1\",\"attrs\":{\"头像\":null,\"姓名\":\"\",\"说明啊啊\":\"\",\"而非\":\"\",\"列表\":\"[{\\\"姓名\\\":\\\"A4纸\\\",\\\"库存\\\":35},{\\\"姓名\\\":\\\"笔记本电脑\\\",\\\"库存\\\":43}]\",\"第二个图片\":null},\"signs\":[],\"bpmModel\":{\"ext\":{\"template\":\"<p>姓名<input name=\\\"60d0458ac6ebef0fff1331af196b3c82\\\" type=\\\"text\\\" title=\\\"姓名\\\" value=\\\"\\\" leipiplugins=\\\"text\\\" orghide=\\\"0\\\" orgalign=\\\"left\\\" orgwidth=\\\"150\\\" orgtype=\\\"text\\\" style=\\\"text-align: left; width: 150px;\\\"></p><p><img width=\\\"120\\\" height=\\\"120\\\" src=\\\"../../img/empty.png\\\" title=\\\"头像\\\" alt=\\\"\\\" leipiplugins=\\\"chooseimage\\\" orgwidth=\\\"120\\\" orgheight=\\\"120\\\" name=\\\"4c50eef3bdaf0b4164ce179e576f2b2d\\\"></p><p><br></p><p>说明：<input name=\\\"dee371f2778d29f2d9e3d7b1873ef1c1\\\" type=\\\"text\\\" title=\\\"说明啊啊\\\" value=\\\"\\\" leipiplugins=\\\"text\\\" orghide=\\\"0\\\" orgalign=\\\"left\\\" orgwidth=\\\"150\\\" orgtype=\\\"text\\\" style=\\\"text-align: left; width: 150px;\\\"></p><p><img width=\\\"40\\\" height=\\\"40\\\" id=\\\"zz\\\" src=\\\"../img/list-view.png\\\" title=\\\"列表\\\" alt=\\\"\\\" leipiplugins=\\\"list\\\" orgwidth=\\\"40\\\" orgheight=\\\"40\\\" listdata=\\\"物品名,库存,个数,还有,什么,asd,ww,er,ew,rwe\\\" class=\\\"listclass\\\" name=\\\"3712972d84adf48acbd6ad24b4d75ad0\\\"></p><p><img width=\\\"120\\\" height=\\\"120\\\" src=\\\"../../img/empty.png\\\" title=\\\"第二个图片\\\" alt=\\\"\\\" leipiplugins=\\\"chooseimage\\\" orgwidth=\\\"120\\\" orgheight=\\\"120\\\" name=\\\"7a37e2701235e832fc7d190be3186dfc\\\"></p><p><img width=\\\"24\\\" height=\\\"24\\\" src=\\\"../img/user.png\\\" title=\\\"而非\\\" alt=\\\"\\\" leipiplugins=\\\"chooseuser\\\" orgwidth=\\\"128\\\" orgheight=\\\"64\\\" name=\\\"b38f98b781f4a7a9ab4d686f0eed5f3f\\\"></p>\",\"xml\":\"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>\\n<definitions xmlns=\\\"http://www.omg.org/spec/BPMN/20100524/MODEL\\\" xmlns:bpmndi=\\\"http://www.omg.org/spec/BPMN/20100524/DI\\\" xmlns:xsi=\\\"http://www.w3.org/2001/XMLSchema-instance\\\" xmlns:dc=\\\"http://www.omg.org/spec/DD/20100524/DC\\\" xmlns:di=\\\"http://www.omg.org/spec/DD/20100524/DI\\\" id=\\\"sid-38422fae-e03e-43a3-bef4-bd33b32041b2\\\" targetNamespace=\\\"http://bpmn.io/bpmn\\\" exporter=\\\"bpmn-js (https://demo.bpmn.io)\\\" exporterVersion=\\\"4.1.0-beta.0\\\">\\n  <process id=\\\"Process_1\\\" isExecutable=\\\"false\\\">\\n    <startEvent id=\\\"StartEvent_1thklpd\\\" name=\\\"开始\\\">\\n      <outgoing>SequenceFlow_0r9ge7g</outgoing>\\n    </startEvent>\\n    <task id=\\\"Task_0swpp46\\\" name=\\\"节点1\\\">\\n      <incoming>SequenceFlow_0r9ge7g</incoming>\\n      <outgoing>SequenceFlow_1hd1w5f</outgoing>\\n    </task>\\n    <sequenceFlow id=\\\"SequenceFlow_0r9ge7g\\\" sourceRef=\\\"StartEvent_1thklpd\\\" targetRef=\\\"Task_0swpp46\\\" />\\n    <endEvent id=\\\"EndEvent_0mq5q4v\\\" name=\\\"结束\\\">\\n      <incoming>SequenceFlow_1hd1w5f</incoming>\\n    </endEvent>\\n    <sequenceFlow id=\\\"SequenceFlow_1hd1w5f\\\" sourceRef=\\\"Task_0swpp46\\\" targetRef=\\\"EndEvent_0mq5q4v\\\" />\\n  </process>\\n  <bpmndi:BPMNDiagram id=\\\"BpmnDiagram_1\\\">\\n    <bpmndi:BPMNPlane id=\\\"BpmnPlane_1\\\" bpmnElement=\\\"Process_1\\\">\\n      <bpmndi:BPMNShape id=\\\"StartEvent_1thklpd_di\\\" bpmnElement=\\\"StartEvent_1thklpd\\\">\\n        <dc:Bounds x=\\\"232\\\" y=\\\"102\\\" width=\\\"36\\\" height=\\\"36\\\" />\\n        <bpmndi:BPMNLabel>\\n          <dc:Bounds x=\\\"239\\\" y=\\\"145\\\" width=\\\"22\\\" height=\\\"14\\\" />\\n        </bpmndi:BPMNLabel>\\n      </bpmndi:BPMNShape>\\n      <bpmndi:BPMNShape id=\\\"Task_0swpp46_di\\\" bpmnElement=\\\"Task_0swpp46\\\">\\n        <dc:Bounds x=\\\"320\\\" y=\\\"80\\\" width=\\\"100\\\" height=\\\"80\\\" />\\n      </bpmndi:BPMNShape>\\n      <bpmndi:BPMNEdge id=\\\"SequenceFlow_0r9ge7g_di\\\" bpmnElement=\\\"SequenceFlow_0r9ge7g\\\">\\n        <di:waypoint x=\\\"268\\\" y=\\\"120\\\" />\\n        <di:waypoint x=\\\"320\\\" y=\\\"120\\\" />\\n      </bpmndi:BPMNEdge>\\n      <bpmndi:BPMNShape id=\\\"EndEvent_0mq5q4v_di\\\" bpmnElement=\\\"EndEvent_0mq5q4v\\\">\\n        <dc:Bounds x=\\\"472\\\" y=\\\"102\\\" width=\\\"36\\\" height=\\\"36\\\" />\\n        <bpmndi:BPMNLabel>\\n          <dc:Bounds x=\\\"479\\\" y=\\\"145\\\" width=\\\"22\\\" height=\\\"14\\\" />\\n        </bpmndi:BPMNLabel>\\n      </bpmndi:BPMNShape>\\n      <bpmndi:BPMNEdge id=\\\"SequenceFlow_1hd1w5f_di\\\" bpmnElement=\\\"SequenceFlow_1hd1w5f\\\">\\n        <di:waypoint x=\\\"420\\\" y=\\\"120\\\" />\\n        <di:waypoint x=\\\"472\\\" y=\\\"120\\\" />\\n      </bpmndi:BPMNEdge>\\n    </bpmndi:BPMNPlane>\\n  </bpmndi:BPMNDiagram>\\n</definitions>\\n\",\"fields\":{\"头像\":{\"width\":\"120\",\"height\":\"120\",\"src\":\"../../img/empty.png\",\"title\":\"头像\",\"alt\":\"\",\"leipiplugins\":\"chooseimage\",\"orgwidth\":\"120\",\"orgheight\":\"120\",\"name\":\"4c50eef3bdaf0b4164ce179e576f2b2d\"},\"姓名\":{\"name\":\"60d0458ac6ebef0fff1331af196b3c82\",\"type\":\"text\",\"title\":\"姓名\",\"value\":\"\",\"leipiplugins\":\"text\",\"orghide\":\"0\",\"orgalign\":\"left\",\"orgwidth\":\"150\",\"orgtype\":\"text\",\"style\":\"text-align: left; width: 150px;\"},\"说明啊啊\":{\"name\":\"dee371f2778d29f2d9e3d7b1873ef1c1\",\"type\":\"text\",\"title\":\"说明啊啊\",\"value\":\"\",\"leipiplugins\":\"text\",\"orghide\":\"0\",\"orgalign\":\"left\",\"orgwidth\":\"150\",\"orgtype\":\"text\",\"style\":\"text-align: left; width: 150px;\"},\"而非\":{\"width\":\"24\",\"height\":\"24\",\"src\":\"../img/user.png\",\"title\":\"而非\",\"alt\":\"\",\"leipiplugins\":\"chooseuser\",\"orgwidth\":\"128\",\"orgheight\":\"64\",\"name\":\"b38f98b781f4a7a9ab4d686f0eed5f3f\"},\"列表\":{\"width\":\"40\",\"height\":\"40\",\"id\":\"zz\",\"src\":\"../img/list-view.png\",\"title\":\"列表\",\"alt\":\"\",\"leipiplugins\":\"list\",\"orgwidth\":\"40\",\"orgheight\":\"40\",\"listdata\":\"物品名,库存,个数,还有,什么,asd,ww,er,ew,rwe\",\"class\":\"listclass\",\"name\":\"3712972d84adf48acbd6ad24b4d75ad0\"},\"第二个图片\":{\"width\":\"120\",\"height\":\"120\",\"src\":\"../../img/empty.png\",\"title\":\"第二个图片\",\"alt\":\"\",\"leipiplugins\":\"chooseimage\",\"orgwidth\":\"120\",\"orgheight\":\"120\",\"name\":\"7a37e2701235e832fc7d190be3186dfc\"}}},\"nodes\":{\"EndEvent_0mq5q4v\":{\"dataSourceSql\":\"\",\"allowedChildNames\":[],\"nextNodes\":[],\"dealers\":{\"roles\":{},\"deps\":{},\"users\":{}},\"type\":\"end\",\"smart\":{\"filterRule\":\"all\",\"formFields\":[],\"targetRule\":\"\",\"chooseRule\":\"none\"},\"timeout\":{\"minValue\":0,\"stepValue\":0,\"maxValue\":0,\"stepUnit\":\"小时\",\"maxUnit\":\"小时\",\"minUnit\":\"小时\"},\"children\":{\"allows\":{},\"force\":\"0\",\"finished\":\"0\",\"manual\":\"0\"},\"datasource\":{\"enable\":\"none\",\"sql\":\"\"},\"name\":\"结束\",\"id\":\"EndEvent_0mq5q4v\",\"fields\":{\"allFields\":[],\"requiredFields\":[]},\"config\":{\"file\":\"disallow\",\"permission\":[]},\"flow\":{\"countersign\":\"disallow\",\"sponsor\":\"confirm\",\"msgTemp\":\"您有新的工作需要办理，流程id：[流水号]，流程：[流程名]\",\"changeAgent\":\"allow\",\"merge\":\"unforced\",\"concurrent\":\"disallow\",\"allowDynamic\":\"0\",\"canSeeSign\":\"always\"}},\"StartEvent_1thklpd\":{\"dataSourceSql\":\"\",\"allowedChildNames\":[],\"nextNodes\":[{\"node\":\"Task_0swpp46\",\"expression\":\"\"}],\"dealers\":{\"roles\":{\"2000007\":\"基础角色\"},\"deps\":{},\"users\":{}},\"type\":\"start\",\"smart\":{\"filterRule\":\"all\",\"formFields\":[],\"targetRule\":\"StartEvent_1thklpd\",\"chooseRule\":\"none\"},\"timeout\":{\"minValue\":0,\"stepValue\":0,\"maxValue\":0,\"stepUnit\":\"小时\",\"maxUnit\":\"小时\",\"minUnit\":\"小时\"},\"children\":{\"allows\":{},\"force\":\"0\",\"finished\":\"0\",\"manual\":\"0\"},\"datasource\":{\"enable\":\"none\",\"sql\":\"\"},\"name\":\"开始\",\"id\":\"StartEvent_1thklpd\",\"fields\":{\"allFields\":[\"头像\",\"第二个图片\",\"列表\"],\"requiredFields\":[]},\"config\":{\"file\":\"disallow\",\"permission\":[]},\"flow\":{\"countersign\":\"disallow\",\"sponsor\":\"confirm\",\"msgTemp\":\"您有新的工作需要办理，流程id：[流水号]，流程：[流程名]\",\"changeAgent\":\"allow\",\"merge\":\"unforced\",\"concurrent\":\"disallow\",\"allowDynamic\":\"0\",\"canSeeSign\":\"always\"}},\"Task_0swpp46\":{\"dataSourceSql\":\"\",\"allowedChildNames\":[],\"nextNodes\":[{\"node\":\"EndEvent_0mq5q4v\",\"expression\":\"\"}],\"dealers\":{\"roles\":{\"2000007\":\"基础角色\"},\"deps\":{},\"users\":{}},\"type\":\"task\",\"smart\":{\"filterRule\":\"all\",\"formFields\":[],\"targetRule\":\"Task_0swpp46\",\"chooseRule\":\"none\"},\"timeout\":{\"minValue\":0,\"stepValue\":0,\"maxValue\":0,\"stepUnit\":\"小时\",\"maxUnit\":\"小时\",\"minUnit\":\"小时\"},\"children\":{\"allows\":{},\"force\":\"0\",\"finished\":\"0\",\"manual\":\"0\"},\"datasource\":{\"enable\":\"none\",\"sql\":\"\"},\"name\":\"节点1\",\"id\":\"Task_0swpp46\",\"fields\":{\"allFields\":[],\"requiredFields\":[]},\"config\":{\"file\":\"disallow\",\"permission\":[]},\"flow\":{\"countersign\":\"disallow\",\"sponsor\":\"confirm\",\"msgTemp\":\"您有新的工作需要办理，流程id：[流水号]，流程：[流程名]\",\"changeAgent\":\"allow\",\"merge\":\"unforced\",\"concurrent\":\"disallow\",\"allowDynamic\":\"0\",\"canSeeSign\":\"always\"}}},\"elseSetting\":{\"noticeOnComment\":\"n\"},\"listFields\":[],\"workflowName\":\"啛啛喳喳\"},\"todoUser\":{\"1\":\"系统管理员\"},\"createTime\":\"2020-02-14 20:20:00\",\"parLogId\":null,\"overUser\":{},\"xml\":null,\"lastModifyTime\":\"2020-02-14 20:20:00\",\"depId\":null,\"_id\":\"5e469070a6d5f017e219b0cf\",\"id\":\"4403\",\"state\":\"流转中\",\"logs\":[{\"nodeName\":\"开始\",\"msg\":\"开始\",\"backup\":{\"mainUsers\":{\"1\":\"系统管理员\"},\"supportUsers\":{}},\"uname\":\"系统管理员\",\"confirmTime\":null,\"type\":\"save\",\"uid\":\"1\",\"files\":[],\"startTime\":\"2020-02-14 20:20:00\",\"attributes\":[[\"头像\",null],[\"第二个图片\",null],[\"列表\",\"[{\\\"姓名\\\":\\\"A4纸\\\",\\\"库存\\\":35},{\\\"姓名\\\":\\\"笔记本电脑\\\",\\\"库存\\\":43}]\"]],\"id\":\"5e4690706c24e3f4cba75a93\",\"time\":\"2020-02-14 20:20:00\",\"endTime\":\"2020-02-14 20:20:00\",\"nodeId\":\"StartEvent_1thklpd\"}]}";
        Obj obj = Json.parse(str);
        int d = 2;
    }

}
