package org.sang.utils;

import org.springframework.http.MediaType;

import java.nio.charset.Charset;

/**
 * http 工具类
 *
 * 需要借助于 spring-web 中的 MediaType
 */
public abstract class HttpUtils {

    /**
     * 根据 MediaType 和 Charset 获取 ContentType
     * @param mediaType
     * @param charset
     * @return
     */
    public static String getContentType(MediaType mediaType, Charset charset) {
        MediaType returnType = new MediaType(mediaType, charset);
        return returnType.toString();
    }
}
