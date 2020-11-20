package org.sang.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 */
public abstract class DateUtils {

    // 年-月-日 日期格式
    public static String YYYY_MM_DD_FORMAT = "yyyy-MM-dd";

    /**
     * 获取当前的日期
     * @return 当前日期
     */
    public static String getNowDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_FORMAT);
        return sdf.format(new Date());
    }
}
