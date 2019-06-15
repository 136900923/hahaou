package com.up72.util;

import com.alibaba.fastjson.JSONObject;
import com.up72.framework.dto.Result;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimetypesFileTypeMap;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * http请求工具类
 */
public class HttpUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * post请求
     *
     * @param url  请求地址
     * @param data json化之后的数据
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static String readHttpPost(String url, String data) throws HttpException, IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("data", data);
        return readHttpPost(url, params);
    }

    /**
     * post请求
     *
     * @param url    请求地址
     * @param params 参数
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static String readHttpPost(String url, Map<String, String> params) throws HttpException, IOException {
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod(url);
        StringBuilder result = new StringBuilder();
        InputStream in = null;
        try {
//            HttpMethodParams methodParams = post.getParams();
//            methodParams.setContentCharset("UTF-8");
            post.getParams().setContentCharset("UTF-8");
            addParameters(post, params);
            // 发送http请求
            client.executeMethod(post);
            int statusCode = post.getStatusCode();
            Result rt = new Result(false);
            switch (statusCode) {
                case 200:
                    in = post.getResponseBodyAsStream();
                    byte[] buff = new byte[1024];
                    int flag = -1;

                    while ((flag = in.read(buff)) != -1) {
                        result.append(new String(buff, 0, flag, "UTF-8"));
                    }
                    in.close();
                    break;
                case 404:
                    rt.setMessage("404错误");
                    result = new StringBuilder(JSONObject.toJSONString(rt));
                    break;
                case 500:
                    rt.setMessage("500错误");
                    result = new StringBuilder(JSONObject.toJSONString(rt));
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            throw new HttpException("post请求异常");
        } finally {
            close(in);
            post.releaseConnection();
        }
        String returnStr = result.toString();
        System.out.println("[readHttpPost]请求URL：" + url + "\n结果：" + returnStr);
        return returnStr;
    }


    /**
     * get请求
     *
     * @param url 请求地址
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static String readHttpGet(String url) throws HttpException, IOException {

        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod(url);
        client.executeMethod(get);
        StringBuilder result = new StringBuilder();
        InputStream in = null;
        try {
            in = get.getResponseBodyAsStream();
            byte[] buff = new byte[1024];
            int flag = -1;

            while ((flag = in.read(buff)) != -1) {
                result.append(new String(buff, 0, flag, "UTF-8"));
            }
            in.close();
        } catch (IOException e) {
            throw new HttpException("get请求异常");
        } finally {
            close(in);
            get.releaseConnection();
        }
        String returnStr = result.toString();
        System.out.println("[readHttpGet]请求URL：" + url + "\n结果：" + returnStr);
        return returnStr;
    }


    private static void addParameters(PostMethod post, Map<String, String> params) {
        if (params != null) {
            for (String key : params.keySet()) {
                post.addParameter(key, params.get(key));
            }
        }
    }

    private static void close(InputStream in) {
        if (null != in) {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * <p>
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * <p>
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 192.168.1.100
     * <p>
     * 用户真实IP为： 192.168.1.110
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * form表单上传
     * @param urlStr URL地址
     * @param textMap 表单参数
     * @param fileMap 文件列表
     * @return
     */
    public static String formUpload(String urlStr, Map<String, String> textMap, Map<String, String> fileMap) {

        String res = "";
        HttpsURLConnection conn = null;
        String BOUNDARY = "---------------------------123821742118716"; // boundary就是request头和上传文件内容的分隔符

        try {

            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManager[] tm = {new MyX509TrustManager()};
            sslContext.init(null, tm, new SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(urlStr);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());

            // text
            if (MapUtils.isNotEmpty(textMap)) {
                StringBuffer strBuf = new StringBuffer();
                Iterator iter = textMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");

                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
                    strBuf.append(inputValue);
                }
                out.write(strBuf.toString().getBytes());
            }

            // file
            if (MapUtils.isNotEmpty(fileMap)) {
                Iterator iter = fileMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    File file = new File(inputValue);
                    String filename = file.getName();
                    String contentType = new MimetypesFileTypeMap().getContentType(file);
                    if (filename.endsWith(".png")) {
                        contentType = "image/png";
                    }
                    if (contentType == null || contentType.equals("")) {
                        contentType = "application/octet-stream";
                    }

                    StringBuffer strBuf = new StringBuffer();

                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");

                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n");
                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

                    LOGGER.info("strBuf : {}", strBuf.toString());

                    out.write(strBuf.toString().getBytes());

                    DataInputStream in = new DataInputStream(new FileInputStream(file));
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024000];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    in.close();
                }
            }

            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 数据返回
            StringBuffer strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            res = strBuf.toString();
            reader.close();
            reader = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }

}
