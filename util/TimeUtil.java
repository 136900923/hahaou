package com.up72.framework.util;

import java.util.Calendar;
import java.util.Date;

import static com.up72.framework.util.SdfUtil.TIME_NUM;
import static com.up72.framework.util.SdfUtil.format;
import static com.up72.framework.util.SdfUtil.parse;

/**
 * 时间工具类，【重点强调如下】：
 * 1、这个类是针对17位的时间（如20151103173941521）的处理的工具类，不处理System.currentTimeMillis()获取到的时间。
 * 2、如果需要对其他的时间做处理，可以自已写工具类。推荐使用{@link com.up72.framework.util.SdfUtil}工具类，可以防止出现线程安全问题。
 *
 * @author HuKaiXuan
 */
public class TimeUtil {

    /** 标准的时间长度(17位) */
    private static final int STANDARD_TIME_LENGTH = TIME_NUM.length();
    /** 最大的时间 */
    private static final String MAX_TIME_OF_17 = "99991231235959999";

    /** 获取方便识别的当前时间毫秒数，如20151103173941521 */
    public static Long curTime() {
        Date date = new Date();
        String format = format(date, TIME_NUM);
        Long curTime = Long.parseLong(format);
        return curTime;
    }

    /**
     * 把17位的Long类型的时间转换成格式化时间，如把20151103173941521转换成2015-11-03
     *
     * @param timeOf17 17位的Long类型的时间，如：20151103173941521
     * @return 常用的字符串类型的日期，如：2015-11-03
     */
    public static String getFormatDate(Long timeOf17) {
        if (timeOf17 == null) {
            return "";
        }
        String tmp = timeOf17.toString();
        if (tmp.length() != STANDARD_TIME_LENGTH) {
            return "";
        }
        return new StringBuilder().append(tmp.substring(0, 4)).append("-").append(tmp.substring(4, 6)).append("-").append(tmp.substring(6, 8)).toString();
    }

    /**
     * 把Long类型的时间转换成格式化时间，如把20151103173941521转换成2015-11-03 17:39:41
     *
     * @param timeOf17 17位的Long类型的时间，如：20151103173941521
     * @return 常用的字符串类型的时间，如：2015-11-03 17:39:41
     */
    public static String getFormatTime(Long timeOf17) {
        if (timeOf17 == null) {
            return "";
        }
        String tmp = timeOf17.toString();
        if (tmp.length() != STANDARD_TIME_LENGTH) {
            return "";
        }
        return tmp.substring(0, 4) + "-" + tmp.substring(4, 6) + "-" + tmp.substring(6, 8) +
                " " + tmp.substring(8, 10) + ":" + tmp.substring(10, 12) + ":" + tmp.substring(12, 14);
    }

    /**
     * 把17位的Long类型的时间转换成Date对象
     *
     * @param timeOf17 17位的Long类型的时间，如：20151103173941521
     * @return
     */
    public static Date long2date(Long timeOf17) {
        if (timeOf17 == null) {
            return null;
        }
        String tmp = timeOf17.toString();
        if (tmp.length() != STANDARD_TIME_LENGTH) {
            return null;
        }
        Date date = parse(tmp, TIME_NUM);
        return date;
    }

    /**
     * 把Date对象转换成17位的Long类型的时间
     *
     * @param date Date对象
     * @return
     */
    public static Long date2long(Date date) {
        if (date == null) {
            return null;
        }
        String format = format(date, TIME_NUM);
        Long time = Long.parseLong(format);
        return time;
    }

    /**
     * 把格式化的时间转换为17位的Long类型的时间，原理是：把非数字删除，然后在后面补0
     * 例子1：参数为“2018-01-01”，返回结果为“20180101000000000”
     * 例子2：参数为“2018-01-01 12:12:12”，返回结果为“20180101121212000”
     *
     * @param formatTime 如：2018-01-01 或 2018-01-01 12:12:12
     * @return
     */
    public static Long format2Long(String formatTime) {
        return format2Long(formatTime, true);
    }

    /**
     * 把格式化的时间转换为17位的Long类型的时间，原理是：把非数字删除，然后在后面补0或者补235959999
     * 例子1：formatTime为“2018-01-01”，isStartTime为true，返回结果为“20180101000000000”
     * 例子2：formatTime为“2018-01-01”，isStartTime为false，返回结果为“20180101235959999”
     * 例子3：formatTime为“2018-01-01 12:12:12”，isStartTime为true，返回结果为“20180101121212000”
     * 例子4：formatTime为“2018-01-01 12:12:12”，isStartTime为false，返回结果为“20180101121212999”
     *
     * @param formatTime  如：2018-01-01 或 2018-01-01 12:12:12
     * @param isStartTime 是否为开始时间（如果是，则后面补0，如果不是，则后面补235959999）
     * @return
     */
    public static Long format2Long(String formatTime, boolean isStartTime) {
        if (CheckUtil.isBlank(formatTime)) {
            return null;
        }
        formatTime = formatTime.replaceAll("\\D", "");
        if (formatTime.length() > STANDARD_TIME_LENGTH) {
            return null;
        }
        // 补充长度
        int fillLen = STANDARD_TIME_LENGTH - formatTime.length();
        StringBuilder longStr = new StringBuilder(formatTime);
        String fillStr = MAX_TIME_OF_17.substring((MAX_TIME_OF_17.length() - fillLen));
        for (int i = 0; i < fillLen; i++) {
            longStr.append(isStartTime ? "0" : fillStr.charAt(i));
        }
        long longTime = Long.parseLong(longStr.toString());
        return longTime;
    }

    /**
     * 在原来时间的基础上增加（或减少）时间。
     * 如，加一天： add(20151103173941521L, Calendar.DATE, int 1)
     *
     * @param timeOf17      17位的Long类型的时间，如：20151103173941521
     * @param calendarField 日历字段，请使用{@link java.util.Calendar}类中的常量，如“天”为：{@link java.util.Calendar#DATE}
     * @param addTimeAmount 增加时间数量，如：1，可以是负值，如-1，正值为加时间，负值为减时间。
     * @return
     */
    public static Long add(Long timeOf17, int calendarField, int addTimeAmount) {
        Date date = long2date(timeOf17);
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendarField, addTimeAmount);
        date = calendar.getTime();
        timeOf17 = date2long(date);
        return timeOf17;
    }

    public static void main(String[] args) {
        try {
            System.out.println(format2Long("2018-12-12", true));
            System.out.println(format2Long("2018-12-12 12:34:56", true));
            System.out.println(format2Long("2", false));
            System.out.println(format2Long("2018-12-12", false));
            System.out.println(format2Long("2018-12-12 12:34:56", false));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
