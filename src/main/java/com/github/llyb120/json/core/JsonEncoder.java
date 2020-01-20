package com.github.llyb120.json.core;

import com.github.llyb120.json.lambda.JsonStr;
import com.github.llyb120.json.reflect.ReflectUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class JsonEncoder {
    StringBuilder sb = new StringBuilder();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    StringifyOption option;

    public JsonEncoder(StringifyOption option){
        this.option = option;
        if (option == null) {
            this.option = new StringifyOption();
        }
    }

    public String stringify(Object obj) {
        sb.setLength(0);
        encode(obj);
        return sb.toString();
    }

    private void encode(Object obj) {
        if (obj == null) {
            sb.append("null");
            return;
        }
        if(obj instanceof JsonStr){
            sb.append(((JsonStr) obj).toJsonString());
            return;
        }
        if (obj instanceof Map) {
            encodeMap((Map) obj);
        } else if (obj instanceof Collection) {
            encodeCollection((Iterable) obj);
        } else if (obj.getClass().isArray()) {
            encodeArray((Object[]) obj);
        }
        //基本类型
        else if (obj instanceof Boolean) {
            if ((Boolean) obj) {
                sb.append("true");
            } else {
                sb.append("false");
            }
        } else if (obj instanceof Integer || obj instanceof Double || obj instanceof Float || obj instanceof BigDecimal) {
//            sb.append("\"");
            sb.append(obj);
//            sb.append("\"");
        } else if (obj instanceof String) {
            sb.append("\"");
            String str = (String) obj;
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (c == '\\') {
                    sb.append(c);
                    i++;
                    if (i < str.length()) {
                        sb.append(str.charAt(i));
                    }
                    continue;
                }
                if (c == '"') {
                    sb.append("\\");
                }
                sb.append(c);
            }
            sb.append("\"");

        } else if (obj instanceof Long) {
            if((long)obj > Integer.MAX_VALUE){
                sb.append("\"");
                sb.append(obj);
                sb.append("\"");
            } else {
                sb.append(obj);
            }
        } else if (obj instanceof Date) {
            sb.append("\"");
            sb.append(sdf.format(obj));
            sb.append("\"");
        } else if (obj.getClass().getName().equals("org.bson.types.ObjectId")) {
            sb.append("\"");
            sb.append(obj.toString());
            sb.append("\"");
        } else if (obj.getClass().getName().equals("org.bson.types.Decimal128")) {
            sb.append(new BigDecimal(obj.toString()));
        } else if(obj.getClass().getEnumConstants() != null){
            try{
//                Object[] consts = obj.getClass().getEnumConstants();
                sb.append("\"");
                sb.append(obj.toString());
                sb.append("\"");
            } catch (Exception e){
                e.printStackTrace();
            }
//            obj.getClass().getEnumConstants()
        } else {
            encodeEntity(obj);
        }
    }

    private void encodeEntity(Object obj) {
//        FieldAccess fa = FieldAccess.get(obj.getClass());
//        MethodAccess ma = MethodAccess.get(obj.getClass());
//        ClassInfo info = ReflectUtil.getClassInfo(obj.getClass());
//        int i = 0;
        sb.append("{");
        Map<String, Object> values = ReflectUtil.getValues(obj);
        values.forEach((k,v) -> {
            if(option.ignoreKeys().containsKey(k)){
                return;
            }
            sb.append("\"");
            sb.append(k);
            sb.append("\"");
            sb.append(":");
            encode(v);
            sb.append(",");
        });
//        info.fields.forEach((k,v) -> {
//            sb.append("\"");
//            sb.append(k);
//            sb.append("\"");
//            sb.append(":");
//            Object val = ReflectUtil.getValue(obj, v);//entry.getValue().get(obj);
//            encode(val);
//            sb.append(",");
//        });

//        String[] names = ma.getMethodNames();
//        for (Map.Entry<String, Method> entry : info.getters.entrySet()) {
//            String methodName = ennames[i];
//            if(methodName.length() <= 3){
//                continue;
//            }
//            int idex = methodName.indexOf("get");
//            if (idex == -1) {
//                continue;
//            }
//            String key = methodName.substring(idex + 3, idex + 4).toLowerCase() + methodName.substring(idex + 4);
//            Class[] types = ma.getParameterTypes()[i];
//            Class retType = ma.getReturnTypes()[i];
//            if (types.length == 0 && !"void".equals(retType.getName())) {
//            sb.append("\"");
//            sb.append(entry.getKey());
//            sb.append("\"");
//            sb.append(":");
//            Object val = null;
//            try {
//                val = entry.getValue().invoke(obj);
//            } catch (Exception e) {
//            }
//            encode(val);
//            sb.append(",");
//            }
//        }
        deleteLastComma();
        sb.append("}");
    }


    private void encodeCollection(Iterable iterable) {
        sb.append("[");
        for (Object o : iterable) {
            encode(o);
            sb.append(",");
        }
        deleteLastComma();
        sb.append("]");
    }

    private void encodeArray(Object[] objects) {
        sb.append("[");
        for (Object o : objects) {
            encode(o);
            sb.append(",");
        }
        deleteLastComma();
        sb.append("]");
    }

    private void encodeMap(Map map) {
        sb.append("{");
        Set eset = map.entrySet();
        for (Object o : eset) {
            Map.Entry entry = (Map.Entry) o;
            Object key = entry.getKey();
            if (key == null) {
                continue;
            }
            sb.append("\"");
            if (key instanceof String) {
                sb.append(key);
            } else {
                sb.append(String.valueOf(key));
            }
            sb.append("\"");
            sb.append(":");
            Object val = entry.getValue();
            encode(val);
            sb.append(",");
        }
        deleteLastComma();
        sb.append("}");
    }

    private void deleteLastComma() {
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.deleteCharAt(sb.length() - 1);
        }
    }

//    private static String addSlashes(String s) {
////        s = s.replaceAll("\\\\", "\\\\\\\\");
////        s = s.replaceAll("\\n", "\\\\n");
////        s = s.replaceAll("\\r", "\\\\r");
//////        s = s.replaceAll("\\00", "\\\\0");
////        s = s.replaceAll("'", "\\\\'");
////        s = s.replaceAll("\"", "\\\\\"");
//        return s;
//    }
}
