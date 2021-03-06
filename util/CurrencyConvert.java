package com.up72.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 货币转换
 */
public class CurrencyConvert {

    final static DecimalFormat NF_FEN = new DecimalFormat("##");
    final static DecimalFormat NF_YUAN = new DecimalFormat("##0.00");
    final static DecimalFormat NF_COMMA_YUAN = new DecimalFormat("#,###.00");
    public static final String REGEX_MONEY_FEN = "^-?[0-9]{1}\\d*$";
    public static final String ZERO = "0.00";

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyConvert.class);

    public static BigDecimal formatYuan(BigDecimal yuan) {
        if (yuan == null) {
            return null;
        } else {
            return new BigDecimal(NF_YUAN.format(yuan));
        }
    }

    public static String formatYuan2Str(Long yuan) {
        if (yuan == null) {
            return ZERO;
        } else {
            return NF_YUAN.format(yuan);
        }
    }

    public static String formatYuan2Str(String yuan) {
        if (yuan == null) {
            return ZERO;
        } else {
            return NF_YUAN.format(yuan);
        }
    }

    public static BigDecimal formatFen(BigDecimal fen) {
        return fen == null ? null : new BigDecimal(NF_FEN.format(fen));
    }

    public static BigDecimal moneyYuan2Fen(BigDecimal yuan) {
        return yuan == null ? null : formatFen(yuan.multiply(new BigDecimal("100")));
    }

    public static BigDecimal moneyFen2Yuan(String fen) {
        if (fen == null || !fen.matches(REGEX_MONEY_FEN)) {
            return null;
        }
        BigDecimal yuan = new BigDecimal(fen).divide(new BigDecimal("100"));
        BigDecimal result = formatYuan(yuan);
        return result;
    }

    /**
     * 人民币分转换为元
     *
     * @param fen
     * @return
     */
    public static String moneyFen2YuanStr(String fen) {
//        LOGGER.info("fen : {}", fen);
        String result = null;
        if (StringUtils.isBlank(fen) || "null".equals(fen)) {
            return ZERO;
        } else {
            result = NF_YUAN.format(moneyFen2Yuan(fen));
        }
        return result;
    }

    /**
     * 人民币元转换为分
     *
     * @param yuan
     * @return
     */
    public static String moneyYuan2FenStr(String yuan) {
        if (yuan == null || "null".equals(yuan)) {
            return "0";
        } else {
            return NF_FEN.format(new BigDecimal(yuan).multiply(new BigDecimal("100")));
        }
    }

    /**
     * 人民币元转换为整数分
     *
     * @param yuan
     * @return
     */
    public static Long moneyYuan2FenLong(String yuan) {
        if (yuan == null || "null".equals(yuan)) {
            return 0L;
        } else {
            return new BigDecimal(yuan).multiply(new BigDecimal("100")).longValue();
        }
    }

    /**
     * 人民币元转换为整数分
     *
     * @param yuan
     * @return
     */
    public static Long moneyYuan2FenLong(Long yuan) {
        if (yuan == null || "null".equals(yuan)) {
            return 0L;
        } else {
            return new BigDecimal(yuan).multiply(new BigDecimal("100")).longValue();
        }
    }

    /**
     * 人民币元转换为以逗号分隔
     *
     * 例如：5000.00，转换后是5,000.00
     *
     * @param yuan
     * @return
     */
    public static String formatYuan2CommaYuan(Long yuan) {
        String result = null;
        if (yuan == null) {
            return ZERO;
        } else {
            result = NF_COMMA_YUAN.format(yuan);
        }
        return result;
    }

    public static String formatStrYuan2CommaYuan(String yuan) {
//        LOGGER.info("yuan : {}", yuan);
        String result = null;
        if (yuan == null || yuan.equals(ZERO) || StringUtils.isBlank(yuan)) {
            return ZERO;
        } else {
            Long fen = moneyYuan2FenLong(yuan);
            if (fen > 100L) {
                result = NF_COMMA_YUAN.format(moneyFen2Yuan(String.valueOf(fen)));
            } else {
                result = NF_YUAN.format(moneyFen2Yuan(String.valueOf(fen)));
            }

        }
        return result;
    }

    /**
     * 人民币分转换为以逗号分隔
     *
     * 例如：500000分，转换后是5,000.00
     *
     * @param fen
     * @return
     */
    public static String formatFen2CommaYuan(String fen) {
        String result = null;
        if (StringUtils.isBlank(fen) || "null".equals(fen)) {
            return ZERO;
        } else {
            long fenLong = Long.parseLong(fen);
//            LOGGER.info("fenLong : {}", fenLong);
            if (fenLong > 100L) {
                result = NF_COMMA_YUAN.format(moneyFen2Yuan(fen));
            } else {
                result = NF_YUAN.format(moneyFen2Yuan(fen));
            }
        }
        return result;
    }

    public static void main(String[] args) {
//        System.out.println(moneyFen2YuanStr("1"));
//        System.out.println(moneyYuan2FenLong("0.01"));
//        String s = "0.00";
//        System.out.println(moneyYuan2FenLong(s));

//        Long yuan = 160L;
//        System.out.println(formatStrYuan2CommaYuan("0.26"));
//        System.out.println(moneyYuan2FenLong("0.26"));
    }
}
