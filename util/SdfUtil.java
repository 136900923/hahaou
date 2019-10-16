/*
 */
package com.up72.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * SimpleDateFormat 日期格式化工具类
 *
 * @author HuKaiXuan
 */
public class SdfUtil {

    /** 将日期格式化为121212121212的格式，带时分秒，后缀“_NUM”代表Number类型，表示是数字 */
    public static final String TIME_NUM = "yyyyMMddHHmmssSSS";
    /** 将日期格式化为2012-12-12 12:12:12的格式，带时分秒 */
    public static final String TIME = "yyyy-MM-dd HH:mm:ss";
    /** 将日期格式化为2012-12-12的格式，不带时分秒 */
    public static final String DATE = "yyyy-MM-dd";
    /** 将日期格式化为20121212的格式，不带时分秒，后缀“_NUM”代表Number类型，表示是数字 */
    public static final String DATE_NUM = "yyyyMMdd";
    /** 将日期格式化为0112的格式，即年的最后两位、加上两位的月份 */
    public static final String YYMM = "yyMM";
    /** 将日期格式化为毫秒 */
    public static final String SSS = "SSS";

    /** 存放不同的日期模板格式的sdf的Map */
    private static ThreadLocal<Map<String, SimpleDateFormat>> sdfMap = ThreadLocal.withInitial(() -> new HashMap<>());

    /**
     * 返回一个SimpleDateFormat，每个线程只会new一次pattern对应的sdf
     *
     * @param pattern
     * @return
     */
    private static SimpleDateFormat getSdf(final String pattern) {
        Map<String, SimpleDateFormat> simpleDateFormatMap = sdfMap.get();
        SimpleDateFormat sdf = simpleDateFormatMap.get(pattern);
        if (sdf == null) {
            sdf = new SimpleDateFormat(pattern);
            simpleDateFormatMap.put(pattern, sdf);
        }
        return sdf;
    }

    /**
     * 格式化，把Date对象格式化成字符串类型的日期
     *
     * @param date    Date对象
     * @param pattern 格式，如：{@link com.up72.framework.util.SdfUtil#TIME}
     * @return
     */
    public static String format(Date date, String pattern) {
        return getSdf(pattern).format(date);
    }

    /**
     * 解析，把字符串类型的日期解析成Date对象
     *
     * @param dateStr 把字符串类型的日期
     * @param pattern 格式，如：{@link com.up72.framework.util.SdfUtil#TIME}
     * @return
     */
    public static Date parse(String dateStr, String pattern) {
        try {
            return getSdf(pattern).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}