package com.up72.framework.util;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 用于检查的工具类
 *
 * @author HuKaiXuan
 */
public class CheckUtil {
    /** 逗号分隔的IDS的判断 */
    public static final Pattern IDS = Pattern.compile("(\\d+,)*\\d+$");
    /** 手机，以1开头，后面10位数字 */
    public static final Pattern MOBILE = Pattern.compile("^(1\\d{10})$");
    /** 邮箱，来源于jQuery Validation Plugin v1.17.0 */
    public static final Pattern EMAIL = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$");
    /** URL链接 */
    public static final Pattern URL = Pattern.compile("^(?:(?:(?:https?|ftp):)?\\/\\/)(?:\\S+(?::\\S*)?@)?(?:(?!(?:10|127)(?:\\.\\d{1,3}){3})(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,})).?)(?::\\d{2,5})?(?:[/?#]\\S*)?$");

    /**
     * 是否为ids（多个数字中间以逗号分隔，首尾不能有逗号，如：1,2,3）
     *
     * @param checkStr 待检查的字符串
     * @return
     */
    public static boolean isIds(String checkStr) {
        return checkStr != null && IDS.matcher(checkStr).matches();
    }

    /**
     * 是否为手机号
     * 以1开头，后面接10位数字，就认为是手机号。
     * 这里的校验比较松散，因为不知道三大运营商会忽然开放哪个号码段。
     *
     * @param checkStr 待检查的字符串
     * @return
     */
    public static boolean isMobile(String checkStr) {
        return checkStr != null && MOBILE.matcher(checkStr).matches();
    }

    /**
     * 是否为邮箱
     * 来源于jQuery Validation Plugin v1.17.0
     * jQuery Validation又来源于：https://html.spec.whatwg.org/multipage/input.html#valid-e-mail-address
     *
     * @param checkStr 待检查的字符串
     * @return
     */
    public static boolean isEmail(String checkStr) {
        return checkStr != null && EMAIL.matcher(checkStr).matches();
    }

    /**
     * 是否为URL链接
     * 来源于jQuery Validation Plugin v1.17.0
     *
     * @param checkStr 待检查的字符串
     * @return
     */
    public static boolean isUrl(String checkStr) {
        return checkStr != null && URL.matcher(checkStr).matches();
    }

    /**
     * 是否为空白的字符串
     *
     * @param checkStr 待检查的字符串
     * @return
     */
    public static boolean isBlank(String checkStr) {
        return StringUtils.isBlank(checkStr);
    }

    /**
     * 是否不是空白的字符串
     *
     * @param checkStr 待检查的字符串
     * @return
     */
    public static boolean isNotBlank(String checkStr) {
        return !isBlank(checkStr);
    }

    /**
     * 对象是否为空，可以用于判断 Map、Collection、String、Array是否为空
     * 【强调】判断String字符串是否为空时，推荐使用上面的{@link #isBlank(String)}，因为empty和blank是有区别的，如"  "是blank的，但是不是empty的
     *
     * @param obj Map、Collection、String、Array
     * @return
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            return StringUtils.isEmpty((String) obj);
        } else if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        } else if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        return false;
    }

    /**
     * 对象是否不为空，是 {@link #isEmpty(Object)} 的取反操作
     *
     * @param obj
     * @return
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 第一个值是否在后面的几个值中
     *
     * @param first         第一个值
     * @param compareValues 用于比较的可变参数
     * @return
     */
    public static boolean isIn(int first, int... compareValues) {
        if (compareValues == null || compareValues.length == 0) {
            return false;
        }
        for (int compareValue : compareValues) {
            if (first == compareValue) {
                return true;
            }
        }
        return false;
    }

    /**
     * 第一个值是否在后面的几个值中
     *
     * @param first         第一个值
     * @param compareValues 用于比较的可变参数
     * @return
     */
    public static boolean isIn(long first, long... compareValues) {
        if (compareValues == null || compareValues.length == 0) {
            return false;
        }
        for (long compareValue : compareValues) {
            if (first == compareValue) {
                return true;
            }
        }
        return false;
    }

    /**
     * 第一个值是否在后面的几个值中
     *
     * @param first         第一个值
     * @param compareValues 用于比较的可变参数
     * @return
     */
    public static boolean isIn(String first, String... compareValues) {
        if (first == null || compareValues == null || compareValues.length == 0) {
            return false;
        }
        for (String compareValue : compareValues) {
            if (first.equals(compareValue)) {
                return true;
            }
        }
        return false;
    }

}
