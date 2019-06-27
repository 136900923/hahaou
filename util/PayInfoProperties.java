package com.up72.hn.util;

import com.up72.framework.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 支付配置，对应payInfo.properties文件
 *
 * @author 周录鹏
 */
public class PayInfoProperties {

    private static Logger LOGGER = LoggerFactory.getLogger(PayInfoProperties.class);

    /** 配置文件名称 */
    private static String propertiesFileName = "payInfo.properties";

    //微信公众号配置
    public static String appId; //公众号appid
    public static String appSecret; //公众号appSecret
    public static String mchID; //商户id
    public static String domain; //域名

    static {
        loadProperty(); // 加载配置文件
        LOGGER.info("appId : {}", appId);
        LOGGER.info("appSecret : {}", appSecret);
        LOGGER.info("mchID : {}", mchID);
        LOGGER.info("domain : {}", domain);
    }

    /** 加载配置文件 */
    public static void loadProperty() {
        PropertiesUtil util = new PropertiesUtil(propertiesFileName);
        appId = util.getStr("wx_appId");
        appSecret = util.getStr("wx_appSecret");
        mchID = util.getStr("mchID");
        domain = util.getStr("domain");
    }
}
