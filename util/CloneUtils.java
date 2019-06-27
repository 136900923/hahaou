package com.up72.hn.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 克隆工具类
 */
public class CloneUtils {

    public static Map cloneMap(Map map) {
        Map target = new HashMap();
        for (Iterator<Map.Entry> iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry entry = iterator.next();
            target.put(entry.getKey(), entry.getValue());
        }
        return target;
    }
}
