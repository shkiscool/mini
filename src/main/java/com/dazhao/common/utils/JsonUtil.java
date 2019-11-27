package com.dazhao.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil {

    /**
     * json 转 pojo
     */
    public static <T> T getClassObject(String json, Class<T> clazz) {
        try {
            return JSONObject.parseObject(json, clazz);
        } catch (Exception e) {
            log.error(clazz + "转JSON失败");
        }
        return null;
    }

    public static <T> List<T> jsonToList(String json, Class<T> clazz) {
        List<T> ts = JSONArray.parseArray(json, clazz);
        return ts;
    }

    /**
     * json转Object
     */
    public static JSONObject getObject(String json) {
        return JSONObject.parseObject(json);
    }
}
