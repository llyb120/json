export default `
String jsonStr = "{\\"b\\":\\"value2\\",\\"c\\":\\"value3\\",\\"a\\":\\"value1\\"}";
//方法一：使用工具类转换
JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
//方法二：new的方式转换
JSONObject jsonObject2 = new JSONObject(jsonStr);

//JSON对象转字符串
jsonObject.toString();
`