package com.up72.util;

import com.up72.common.util.JsonUtil;

import java.io.IOException;
import java.util.*;

/**
 * 移除Map中空的键值对
 *
 * @ClassName RemoveNullKeyValue
 * @Description
 * @Author 周录鹏
 * @Date 2019/1/30 17:48
 * @Version 1.0
 **/
public class RemoveNullKeyValue {

    /*移除Map中值为空的键值对*/
    public static Map removeNullEntry(Map map) {
        removeNullKey(map);
        removeNullValue(map);
        return map;
    }

    /*移除键为空的键值对*/
    public static Map removeNullKey(Map map) {
        Set set = map.keySet();
        for (Iterator iterator = set.iterator(); iterator.hasNext(); ) {
            Object obj = (Object) iterator.next();
            remove(obj, iterator);
        }
        return map;
    }

    /*移除值为空的键值对*/
    public static Map removeNullValue(Map map) {
        Set set = map.keySet();
        for (Iterator iterator = set.iterator(); iterator.hasNext(); ) {
            Object obj = (Object) iterator.next();
            Object value = (Object) map.get(obj);
            remove(value, iterator);
        }
        return map;
    }

    private static void remove(Object obj, Iterator iterator) {

        if (obj instanceof String) {
            String str = (String) obj;
            if (str == null || str.trim().isEmpty()) {
                iterator.remove();
            }

        } else if (obj instanceof Collection) {

            Collection col = (Collection) obj;
            if (col == null || col.isEmpty()) {
                iterator.remove();
            }

        } else if (obj instanceof Map) {

            Map temp = (Map) obj;
            if (temp == null || temp.isEmpty()) {
                iterator.remove();
            }

        } else if (obj instanceof Object[]) {

            Object[] array = (Object[]) obj;
            if (array == null || array.length <= 0) {
                iterator.remove();
            }

        } else {

            if (obj == null) {
                iterator.remove();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Map map = new HashMap();
        map.put("123", 123);
        map.put("哈哈", null);
        map.put("", 3456);
        map.put("", null);
        map.put("", "");
        removeNullEntry(map);
        System.out.println(JsonUtil.object2Json(map));
    }
}
