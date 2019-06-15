package com.up72.hn.util;

import org.apache.commons.lang.ObjectUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CollectionUtils {

    /**
     * 是否包含key值
     * @param map map对象
     * @return 比较结果
     */
    public static boolean isContainKey(Map map, Object obj) {
        boolean flag = false;
        Iterator keys = map.keySet().iterator();
        while (keys.hasNext()) {
            Object key = keys.next();
            if (ObjectUtils.equals(obj, key)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
//        map.put("1", 1);
        map.put("2", 2);

        String str = "1";
        boolean flag = isContainKey(map, str);
        System.out.println(flag);
    }
}
