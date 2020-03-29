
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
        String str = "{\"bpmId\":\"5e400124cb5c623b2983319d\",\"currentNodes\":[{\"nodeName\":\"开始\",\"msg\":null,\"minTimeout\":\"2020-03-02 10:56:09\",\"mainUsers\":{\"193\":\"晏*祥\"},\"maxTimeout\":\"2020-03-02 10:56:09\",\"supportUsers\":{},\"logId\":\"5e5c75c96c2452476138700a\",\"nodeId\":\"StartEvent_1thklpd\"}],\"comments\":null,\"parInsId\":null,\"pubUName\":\"晏*祥\",\"bpmName\":\"测试办公用品审批流程(2020-03-02 10:56:09)\",\"depName\":null,\"pubUid\":\"193\",\"attrs\":{\"aaa\":\"晏*祥\",\"列表控件\":\"[{\"物品名\":\"A2纸\",\"所属分类\":\"纸张\",\"库存\":42,\"入库总数\":112,\"可申请数量\":33,\"申请时间\":\"2020-03-02\",\"申请数量\":3}]\",\"隐藏控件\":\"[{\"wupinId\":\"5e5a56d26c24a9eaf925b054\",\"number\":\"3\",\"inStockNumber\":33,\"wupinName\":\"A2纸\",\"status\":\"claimFalse\",\"id\":\"5e5c75c86c24f6aa984413f5\",\"ture_name\":\"晏*祥\",\"uids\":\"193\",\"depId\":\"\",\"desc\":\"3\",\"date\":\"2020-03-02 00:00:00\",\"lastModify\":\"2020-03-02 10:56:08\",\"bpmId\":\"5e400124cb5c623b2983319d\"}]\",\"审批意见\":\"\",\"申请人\":null},\"signs\":[],\"bpmModel\":{\"ext\":{\"template\":\"<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;办公用品申请<br></p><table><tbody><tr class=\"firstRow\"><td width=\"361\" valign=\"top\" style=\"word-break: break-all;\">申请人：<input name=\"47bce5c74f589f4867dbd57e9ca9f808\" type=\"text\" value=\"宏控件：当前用户姓名\" title=\"aaa\" leipiplugins=\"macros\" orgtype=\"当前用户姓名\" expression=\"\" orghide=\"0\" orgwidth=\"150\" style=\"width: 150px;\"></td><td width=\"361\" valign=\"top\"><input name=\"8ac8da83627df7b14df32cdbc8d34c61\" type=\"text\" title=\"申请人\" value=\"\" leipiplugins=\"text\" orghide=\"0\" orgalign=\"left\" orgwidth=\"150\" orgtype=\"text\" style=\"text-align: left; width: 150px;\" orgfontsize=\"\" orgheight=\"\"></td></tr></tbody></table><p><img width=\"40\" height=\"40\" id=\"zz\" src=\"../img/list-view.png\" title=\"列表控件\" alt=\"\" leipiplugins=\"list\" orgwidth=\"40\" orgheight=\"40\" listdata=\"物品名,所属分类,库存,入库总数,可申请数量,申请时间,申请数量\" orgedit=\"n\" class=\"listclass\" name=\"dcdffac8b8d38df3a3f1cf42bdf37f36\"><br></p><p><br></p><hr><p><br></p><table><tbody><tr class=\"firstRow\"><td width=\"743\" valign=\"top\" style=\"word-break: break-all;\">是否同意申请<span leipiplugins=\"select\"><select name=\"98a52a9e323816e1403f63a1b83e3454\" title=\"审批意见\" leipiplugins=\"select\" size=\"1\" orgwidth=\"150\" style=\"width: 150px;\"><option value=\"同意\" selected=\"selected\">同意</option><option value=\"不同\">不同</option></select>&nbsp;&nbsp;</span></td></tr></tbody></table><p><input name=\"495ae7dd9913f888317c85e1ea89bbf7\" type=\"text\" title=\"隐藏控件\" value=\"\" leipiplugins=\"text\" orghide=\"1\" orgalign=\"left\" orgwidth=\"150\" orgtype=\"text\" style=\"text-align: left; width: 150px;\" orgfontsize=\"\" orgheight=\"\"></p><p>&nbsp;</p><p><br></p>\",\"xml\":\"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\\n<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" id=\"sid-38422fae-e03e-43a3-bef4-bd33b32041b2\" targetNamespace=\"http://bpmn.io/bpmn\" exporter=\"bpmn-js (https://demo.bpmn.io)\" exporterVersion=\"4.1.0-beta.0\">\\n  <process id=\"Process_1\" isExecutable=\"false\">\\n    <startEvent id=\"StartEvent_1thklpd\" name=\"开始\">\\n      <outgoing>SequenceFlow_0r9ge7g</outgoing>\\n    </startEvent>\\n    <task id=\"Task_0swpp46\" name=\"节点1\">\\n      <incoming>SequenceFlow_0r9ge7g</incoming>\\n      <outgoing>SequenceFlow_1hd1w5f</outgoing>\\n    </task>\\n    <sequenceFlow id=\"SequenceFlow_0r9ge7g\" sourceRef=\"StartEvent_1thklpd\" targetRef=\"Task_0swpp46\" />\\n    <endEvent id=\"EndEvent_0mq5q4v\" name=\"结束\">\\n      <incoming>SequenceFlow_1hd1w5f</incoming>\\n    </endEvent>\\n    <sequenceFlow id=\"SequenceFlow_1hd1w5f\" sourceRef=\"Task_0swpp46\" targetRef=\"EndEvent_0mq5q4v\" />\\n  </process>\\n  <bpmndi:BPMNDiagram id=\"BpmnDiagram_1\">\\n    <bpmndi:BPMNPlane id=\"BpmnPlane_1\" bpmnElement=\"Process_1\">\\n      <bpmndi:BPMNShape id=\"StartEvent_1thklpd_di\" bpmnElement=\"StartEvent_1thklpd\">\\n        <dc:Bounds x=\"232\" y=\"102\" width=\"36\" height=\"36\" />\\n        <bpmndi:BPMNLabel>\\n          <dc:Bounds x=\"239\" y=\"145\" width=\"22\" height=\"14\" />\\n        </bpmndi:BPMNLabel>\\n      </bpmndi:BPMNShape>\\n      <bpmndi:BPMNShape id=\"Task_0swpp46_di\" bpmnElement=\"Task_0swpp46\">\\n        <dc:Bounds x=\"320\" y=\"80\" width=\"100\" height=\"80\" />\\n      </bpmndi:BPMNShape>\\n      <bpmndi:BPMNEdge id=\"SequenceFlow_0r9ge7g_di\" bpmnElement=\"SequenceFlow_0r9ge7g\">\\n        <di:waypoint x=\"268\" y=\"120\" />\\n        <di:waypoint x=\"320\" y=\"120\" />\\n      </bpmndi:BPMNEdge>\\n      <bpmndi:BPMNShape id=\"EndEvent_0mq5q4v_di\" bpmnElement=\"EndEvent_0mq5q4v\">\\n        <dc:Bounds x=\"472\" y=\"102\" width=\"36\" height=\"36\" />\\n        <bpmndi:BPMNLabel>\\n          <dc:Bounds x=\"479\" y=\"145\" width=\"22\" height=\"14\" />\\n        </bpmndi:BPMNLabel>\\n      </bpmndi:BPMNShape>\\n      <bpmndi:BPMNEdge id=\"SequenceFlow_1hd1w5f_di\" bpmnElement=\"SequenceFlow_1hd1w5f\">\\n        <di:waypoint x=\"420\" y=\"120\" />\\n        <di:waypoint x=\"472\" y=\"120\" />\\n      </bpmndi:BPMNEdge>\\n    </bpmndi:BPMNPlane>\\n  </bpmndi:BPMNDiagram>\\n</definitions>\\n\",\"fields\":{\"aaa\":{\"name\":\"47bce5c74f589f4867dbd57e9ca9f808\",\"type\":\"text\",\"value\":\"宏控件：当前用户姓名\",\"title\":\"aaa\",\"leipiplugins\":\"macros\",\"orgtype\":\"当前用户姓名\",\"expression\":\"\",\"orghide\":\"0\",\"orgwidth\":\"150\",\"style\":\"width: 150px;\"},\"列表控件\":{\"width\":\"40\",\"height\":\"40\",\"id\":\"zz\",\"src\":\"../img/list-view.png\",\"title\":\"列表控件\",\"alt\":\"\",\"leipiplugins\":\"list\",\"orgwidth\":\"40\",\"orgheight\":\"40\",\"listdata\":\"物品名,所属分类,库存,入库总数,可申请数量,申请时间,申请数量\",\"orgedit\":\"n\",\"class\":\"listclass\",\"name\":\"dcdffac8b8d38df3a3f1cf42bdf37f36\"},\"隐藏控件\":{\"name\":\"495ae7dd9913f888317c85e1ea89bbf7\",\"type\":\"text\",\"title\":\"隐藏控件\",\"value\":\"\",\"leipiplugins\":\"text\",\"orghide\":\"1\",\"orgalign\":\"left\",\"orgwidth\":\"150\",\"orgtype\":\"text\",\"style\":\"text-align: left; width: 150px;\",\"orgfontsize\":\"\",\"orgheight\":\"\"},\"审批意见\":{\"name\":\"98a52a9e323816e1403f63a1b83e3454\",\"title\":\"审批意见\",\"leipiplugins\":\"select\",\"size\":\"1\",\"orgwidth\":\"150\",\"style\":\"width: 150px;\",\"values\":[\"同意\",\"不同\"],\"defaultValue\":\"同意\"},\"申请人\":{\"name\":\"8ac8da83627df7b14df32cdbc8d34c61\",\"type\":\"text\",\"title\":\"申请人\",\"value\":\"\",\"leipiplugins\":\"text\",\"orghide\":\"0\",\"orgalign\":\"left\",\"orgwidth\":\"150\",\"orgtype\":\"text\",\"style\":\"text-align: left; width: 150px;\",\"orgfontsize\":\"\",\"orgheight\":\"\"}}},\"nodes\":{\"EndEvent_0mq5q4v\":{\"dataSourceSql\":\"\",\"allowedChildNames\":[],\"nextNodes\":[],\"dealers\":{\"roles\":{},\"deps\":{},\"users\":{}},\"type\":\"end\",\"smart\":{\"filterRule\":\"all\",\"formFields\":[],\"targetRule\":\"\",\"chooseRule\":\"none\"},\"timeout\":{\"minValue\":0,\"stepValue\":0,\"maxValue\":0,\"stepUnit\":\"小时\",\"maxUnit\":\"小时\",\"minUnit\":\"小时\"},\"children\":{\"allows\":{},\"force\":\"0\",\"finished\":\"0\",\"manual\":\"0\"},\"datasource\":{\"enable\":\"none\",\"sql\":\"\"},\"name\":\"结束\",\"id\":\"EndEvent_0mq5q4v\",\"fields\":{\"allFields\":[],\"requiredFields\":[]},\"config\":{\"file\":\"disallow\",\"permission\":[]},\"flow\":{\"countersign\":\"disallow\",\"sponsor\":\"confirm\",\"msgTemp\":\"您有新的工作需要办理，流程id：[流水号]，流程：[流程名]\",\"changeAgent\":\"allow\",\"merge\":\"unforced\",\"concurrent\":\"disallow\",\"allowDynamic\":\"0\",\"canSeeSign\":\"always\"}},\"StartEvent_1thklpd\":{\"dataSourceSql\":\"\",\"allowedChildNames\":[],\"nextNodes\":[{\"node\":\"Task_0swpp46\",\"expression\":\"\"}],\"dealers\":{\"roles\":{\"2000007\":\"基础角色\",\"1059767604954005504\":\"系统管理员\"},\"deps\":{\"229\":\"授信管理部\"},\"users\":{\"237\":\"吴*琴\",\"238\":\"李*巧\",\"239\":\"廖*琼\"}},\"type\":\"start\",\"smart\":{\"filterRule\":\"all\",\"formFields\":[],\"targetRule\":\"StartEvent_1thklpd\",\"chooseRule\":\"none\"},\"timeout\":{\"minValue\":0,\"stepValue\":0,\"maxValue\":0,\"stepUnit\":\"小时\",\"maxUnit\":\"小时\",\"minUnit\":\"小时\"},\"children\":{\"allows\":{},\"force\":\"0\",\"finished\":\"0\",\"manual\":\"0\"},\"datasource\":{\"enable\":\"none\",\"sql\":\"\"},\"name\":\"开始\",\"id\":\"StartEvent_1thklpd\",\"fields\":{\"allFields\":[\"aaa\",\"申请人\",\"列表控件\",\"隐藏控件\"],\"requiredFields\":[]},\"config\":{\"file\":\"disallow\",\"permission\":[]},\"flow\":{\"countersign\":\"disallow\",\"sponsor\":\"confirm\",\"msgTemp\":\"您有新的工作需要办理，流程id：[流水号]，流程：[流程名]\",\"changeAgent\":\"allow\",\"merge\":\"unforced\",\"concurrent\":\"disallow\",\"allowDynamic\":\"0\",\"canSeeSign\":\"always\"}},\"Task_0swpp46\":{\"dataSourceSql\":\"\",\"allowedChildNames\":[],\"nextNodes\":[{\"node\":\"EndEvent_0mq5q4v\",\"expression\":\"\"}],\"dealers\":{\"roles\":{\"2000007\":\"基础角色\"},\"deps\":{},\"users\":{}},\"type\":\"task\",\"smart\":{\"filterRule\":\"all\",\"formFields\":[],\"targetRule\":\"Task_0swpp46\",\"chooseRule\":\"none\"},\"timeout\":{\"minValue\":0,\"stepValue\":0,\"maxValue\":0,\"stepUnit\":\"小时\",\"maxUnit\":\"小时\",\"minUnit\":\"小时\"},\"children\":{\"allows\":{},\"force\":\"0\",\"finished\":\"0\",\"manual\":\"0\"},\"datasource\":{\"enable\":\"none\",\"sql\":\"\"},\"name\":\"节点1\",\"id\":\"Task_0swpp46\",\"fields\":{\"allFields\":[\"列表控件\",\"审批意见\",\"隐藏控件\",\"申请人\"],\"requiredFields\":[]},\"config\":{\"file\":\"disallow\",\"permission\":[]},\"flow\":{\"countersign\":\"disallow\",\"sponsor\":\"confirm\",\"msgTemp\":\"您有新的工作需要办理，流程id：[流水号]，流程：[流程名]\",\"changeAgent\":\"allow\",\"merge\":\"unforced\",\"concurrent\":\"disallow\",\"allowDynamic\":\"0\",\"canSeeSign\":\"always\"}}},\"elseSetting\":{\"noticeOnComment\":\"n\"},\"listFields\":[\"申请人\",\"列表控件\",\"审批意见\",\"隐藏控件\"],\"workflowName\":\"测试办公用品审批流程\"},\"todoUser\":{\"193\":\"晏*祥\"},\"createTime\":\"2020-03-02 10:56:09\",\"parLogId\":null,\"overUser\":{},\"xml\":null,\"lastModifyTime\":\"2020-03-02 10:56:09\",\"depId\":null,\"_id\":\"5e5c75c9d153b06e09d2f342\",\"id\":\"4633\",\"state\":\"流转中\",\"logs\":[{\"nodeName\":\"开始\",\"msg\":\"开始\",\"backup\":{\"mainUsers\":{\"193\":\"晏*祥\"},\"supportUsers\":{}},\"uname\":\"晏*祥\",\"confirmTime\":null,\"type\":\"save\",\"uid\":\"193\",\"files\":[],\"startTime\":\"2020-03-02 10:56:09\",\"attributes\":[[\"aaa\",\"晏*祥\"],[\"申请人\",null],[\"列表控件\",\"[{\"物品名\":\"A2纸\",\"所属分类\":\"纸张\",\"库存\":42,\"入库总数\":112,\"可申请数量\":33,\"申请时间\":\"2020-03-02\",\"申请数量\":3}]\"],[\"隐藏控件\",\"[{\"wupinId\":\"5e5a56d26c24a9eaf925b054\",\"number\":\"3\",\"inStockNumber\":33,\"wupinName\":\"A2纸\",\"status\":\"claimFalse\",\"id\":\"5e5c75c86c24f6aa984413f5\",\"ture_name\":\"晏*祥\",\"uids\":\"193\",\"depId\":\"\",\"desc\":\"3\",\"date\":\"2020-03-02 00:00:00\",\"lastModify\":\"2020-03-02 10:56:08\",\"bpmId\":\"5e400124cb5c623b2983319d\"}]\"]],\"id\":\"5e5c75c96c2452476138700a\",\"time\":\"2020-03-02 10:56:09\",\"endTime\":\"2020-03-02 10:56:09\",\"nodeId\":\"StartEvent_1thklpd\"}]}";
        Obj obj = Json.parse(str);
        int d = 2;
    }

}
