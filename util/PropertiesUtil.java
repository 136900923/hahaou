package com.up72.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置文件工具类
 */
public class PropertiesUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtil.class);

    // 获取配置文件
    public static Properties getProperties(String fileName) {
        InputStream inputStream = null;
        Properties properties = null;
        try {
            inputStream = ProjectProperties.class.getClassLoader().getResourceAsStream(fileName);
            if (inputStream == null) {
                LOGGER.error("{} 配置文件不存在", fileName);
            }
            properties = new Properties();
            properties.load(inputStream);
        } catch (Exception e) {
            LOGGER.error("加载配置文件出错", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (properties == null) {
            LOGGER.error("加载配置文件失败");
        }
        return properties;
    }
}
