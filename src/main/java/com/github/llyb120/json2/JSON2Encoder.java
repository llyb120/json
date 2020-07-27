package com.github.llyb120.json2;


import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.github.llyb120.json.Util.isNumeric;

/**
 * @Author: Administrator
 * @Date: 2020/7/27 9:46
 */
public class JSON2Encoder {

    Obj2 obj;
    Node root = new Node(null);
    StringBuilder sb = new StringBuilder();

    static class Node{
        String key;
        Object value;
        Map<String, Node> children = new HashMap<>();
        int numKeys;

        Node(String key){
            this.key = key;
        }
    }

    public JSON2Encoder(Obj2 obj){
        this.obj = obj;
    }

    public String encode(){
        for (Map.Entry<String, Object> entry : obj.map.entrySet()) {
            String[] keys = entry.getKey().split("\\.");
            Node node = root;
            for (String key : keys) {
                Node nNode = node.children.get(key);
                if (nNode == null) {
                    nNode = new Node(key);
                    node.children.put(key, nNode);
                    if(isNumeric(key)){
                        node.numKeys++;
                    }
                }
                node = nNode;
            }
            node.value = entry.getValue();
        }

        //walk map
        walk(root);
        return sb.toString();
    }

    void walk(Node node){
        if(node.value == null){
            if(node.children.size() == node.numKeys){
                sb.append("[");
                for (Map.Entry<String, Node> entry : node.children.entrySet()) {
                    walk(entry.getValue());
                    sb.append(",");
                }
                if(sb.charAt(sb.length() - 1) == ',') {
                    sb.deleteCharAt(sb.length() - 1);
                }
                sb.append("]");
            } else {
                sb.append("{");
                for (Map.Entry<String, Node> entry : node.children.entrySet()) {
                    sb.append("\"");
                    sb.append(entry.getKey());
                    sb.append("\"");
                    sb.append(": ");
                    walk(entry.getValue());
                    sb.append(",");
                }
                if(sb.charAt(sb.length() - 1) == ',') {
                    sb.deleteCharAt(sb.length() - 1);
                }
                sb.append("}");
            }
//            if(node.children.size() == node.numKeys){
//                sb.append("]");
//            } else {
//            }
        } else {
            sb.append("\"");
            sb.append(node.value);
            sb.append("\"");
        }
    }
}
