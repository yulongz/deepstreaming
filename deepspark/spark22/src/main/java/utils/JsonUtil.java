package utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hbase.MappingTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.io.IOException;
import java.util.*;

/**
 * Project Name:chapter3
 * Package Name:org.smart4j.framework.util
 * Date:2017/9/17 19:46
 * AUTHOR by zhangyulong
 * Email:sky.zyl@hotmail.com
 */
public final class JsonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

//    static {
//        OBJECT_MAPPER.setPropertyNamingStrategy(new PropertyNamingStrategy() {
//            private static final long serialVersionUID = 1L;
//
//            // 反序列化时调用
//            @Override
//            public String nameForSetterMethod(MapperConfig<?> config,
//                                              AnnotatedMethod method, String defaultName) {
//                return method.getName().substring(3);
//            }
//
//            // 序列化时调用
//            @Override
//            public String nameForGetterMethod(MapperConfig<?> config,
//                                              AnnotatedMethod method, String defaultName) {
//                return method.getName().substring(3);
//            }
//        });
//    }

    /**
     * 将POJO转为JSON
     *
     * @param <T> the type parameter
     * @param obj the obj
     * @return the string
     */
    public static <T> String toJson(T obj) {

        String json;
        try {
            json = OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.error("convert POJO to JSON failure", e);
            throw new RuntimeException(e);
        }
        return json;
    }

    /**
     * 将JSON转为POJO
     *
     * @param <T>  the type parameter
     * @param json the json
     * @param type the type
     * @return the t
     */
    public static <T> T fromJson(String json, Class<T> type) {
        T pojo;
        try {
            pojo = OBJECT_MAPPER.readValue(json, type);
        } catch (IOException e) {
            LOGGER.error("convert JSON to POJO failure", e);
            throw new RuntimeException(e);
        }
        return pojo;
    }

    /**
     * 将JSON转成JSONObject
     *
     * @param json the json
     * @return the json object
     */
    public static JSONObject fromJson(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject;
    }

    /**
     * 将JSONObject转成JSON
     *
     * @param jsonObject the json object
     * @return the string
     */
    public static String toJson(JSONObject jsonObject) {
        String jsonString = JSON.toJSONString(jsonObject);
        return jsonString;
    }

    /**
     * 转换编码
     *
     * @param jsonObject the json object
     * @param tableName  the table name
     * @return the json object
     */
    public static JSONObject transformKeys(JSONObject jsonObject,String tableName) {
        JSONObject jsonObjectNew = new JSONObject();
        jsonObjectNew.put("UTCTime", jsonObject.get("UTCTime"));
        jsonObjectNew.put("UnixTime", jsonObject.get("UnixTime"));
        Iterator<String> iterator = jsonObject.keySet().iterator();
        HashSet<String> strings = new HashSet<>();
        strings.add("newCode");
        String newKey;
        while (iterator.hasNext()) {
            String oldKey = iterator.next();
            try {
                newKey = MappingTable.getData(strings, oldKey, tableName).get("newCode");
                if (newKey == null) {
                } else {
                    jsonObjectNew.put(newKey, jsonObject.get(oldKey));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonObjectNew;
    }

    /**
     * 拆分JsonObject
     *
     * @param jsonObject the json object
     * @return the tuple 2
     */
    public static Tuple2<JSONObject, JSONObject> splitJSONObject(JSONObject jsonObject) {
        JSONObject jsonObjectKey = new JSONObject();
        jsonObjectKey.put("UTCTime", jsonObject.get("UTCTime"));
        jsonObjectKey.put("UnixTime", jsonObject.get("UnixTime"));
        jsonObject.remove("UTCTime");
        jsonObject.remove("UnixTime");
        Tuple2<JSONObject, JSONObject> tuple2 = new Tuple2<>(jsonObjectKey, jsonObject);
        return tuple2;
    }

    /**
     * flatMapValues:value<JsonObject>->list
     *
     * @param v1 the v 1
     * @return the iterable
     */
    public static Iterable<JSONObject> valuesToList(JSONObject v1) {
        List<JSONObject> list = new ArrayList<>();
        for (Map.Entry<String, Object> m : v1.entrySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(m.getKey(), m.getValue());
            list.add(jsonObject);
        }
        return list;
    }
}
