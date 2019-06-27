package com.up72.hn.util;

import com.up72.framework.util.PropertiesUtil;

/**
 * 阿里云OSS配置，对应oss.properties文件
 *
 * @author 周录鹏
 */
public class OSSProperties {

    /** 配置文件名称 */
    private static String propertiesFileName = "oss.properties";

    //配置阿里云OSS
    public static String endpoint;//地域节点
    public static String accessKeyId;//
    public static String accessKeySecret;//
    public static String bucketName;//bucket名称
    public static String domain;//域名

    static {
        loadProperty(); // 加载配置文件
    }

    /** 加载配置文件 */
    public static void loadProperty() {
        PropertiesUtil util = new PropertiesUtil(propertiesFileName);
        endpoint = util.getStr("endpoint");
        accessKeyId = util.getStr("accessKeyId");
        accessKeySecret = util.getStr("accessKeySecret");
        bucketName = util.getStr("bucketName");
        domain = util.getStr("domain");
    }
}
