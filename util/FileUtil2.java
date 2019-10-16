package com.up72.hn.util;

import com.aliyun.oss.OSSClient;
import com.up72.component.upload2.util.UploadProperties;
import com.up72.framework.util.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.schild.jave.MultimediaInfo;
import ws.schild.jave.MultimediaObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.NumberFormat;

public class FileUtil {

    public static String endpoint;
    public static String accessKeyId;
    public static String accessKeySecret;
    public static String bucketName;

    /** 配置文件名称 */
    private static String propertiesFileName = "oss.properties";

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    public static void loadProperty() {
        PropertiesUtil util = new PropertiesUtil(propertiesFileName);
        endpoint = util.getStr("endpoint");
        accessKeyId = util.getStr("accessKeyId");
        accessKeySecret = util.getStr("accessKeySecret");
        bucketName = util.getStr("bucketName");
    }

    static {
        loadProperty(); // 加载配置文件
        LOGGER.info("endpoint : {}", endpoint);
        LOGGER.info("accessKeyId : {}", accessKeyId);
        LOGGER.info("accessKeySecret : {}", accessKeySecret);
        LOGGER.info("bucketName : {}", bucketName);
    }

    public static final String ALGORITHM_MD5 = "MD5";
    public static final String ALGORITHM_SHA_1 = "SHA-1";

    public static String getDurationCnStr(Long duration) {
        String str = "";
        if (null != duration && duration > 0) {
            long minutes = duration / 60;
            long hour = minutes / 60;
            if (hour > 0) {
                str += hour + "小时";
                minutes = minutes % 60;
                if (minutes > 0) {
                    str += minutes + "分";
                }
            } else if (minutes > 0) {
                str += minutes + "分";
            }
            if (duration % 60 > 0) {
                str += duration % 60 + "秒";
            }
        }
        return str;
    }

    private static NumberFormat numberFormat = NumberFormat.getInstance();

    public static String getHourStr(Integer duration) {
        String str = "";
        if (null != duration && duration > 0) {
            long minutes = duration / 60;
            long hour = minutes / 60;
            if (hour > 0) {
                str += hour;
                minutes = minutes % 60;
                if (minutes > 0) {
                    str += "." + (minutes * 10 / 60);
                }
            } else if (minutes > 0) {
                str += "0." + (minutes * 10 / 60) + "";
            }
        }
        if (str == "") {
            str = "0";
        }
        return str;
    }

    public static String getDurationEnStr(Long duration) {
        String str = "";
        if (null != duration && duration > 0) {
            long minutes = duration / 60;
            long hour = minutes / 60;
            if (hour > 0) {
                str += hour + ":";
                minutes = minutes % 60;
                str += minutes + ":";
            } else {
                str += minutes + ":";
            }
            if (duration % 60 > 0) {
                str += duration % 60;
            }
        }
        return str;
    }

    public static String getFileExt(String filePath) {
        LOGGER.info("filePath : {}", filePath);
        int index = filePath.lastIndexOf(".");
        if (index > 0) {
            String suffix = filePath.substring(index + 1, filePath.length()).toLowerCase();
            LOGGER.info("suffix : {}", suffix);
            return suffix;
        }
        return "";
    }

    /**
     * 获取音频时长
     *
     * @param filePath
     * @return
     */
    public static Long getDuration(String filePath) {
        long length = 0;
        try {
            MP3File mp3File = (MP3File) AudioFileIO.read(new File(filePath));
            MP3AudioHeader audioHeader = (MP3AudioHeader) mp3File.getAudioHeader();
            // 单位为秒
            length = audioHeader.getTrackLength();
            return length;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return length;
    }

    /**
     * 文件上传到阿里云oss
     * @param dbFilePath 数据库文件路径
     * @param fileName 自定义文件别名
     */
    public static void file2oss(String dbFilePath, String fileName) {
        LOGGER.info("dbFilePath : {}", dbFilePath);
        if (StringUtils.isBlank(dbFilePath)) {
            LOGGER.info("dbFilePath is null");
            return;
        }
        // 获取完整的存储路径
        String fullPath = UploadProperties.storageFolder + dbFilePath.replace("/upload", "");
        String urlPath = OSSProperties.domain + dbFilePath;
        LOGGER.info("dbFilePath fullPath : {}", fullPath);
        File file = new File(fullPath);
        if (file.exists()) {
            LOGGER.info("本地文件存在");
        } else {
            LOGGER.info("本地文件不存在");
            try {
                if (!file.getParentFile().exists()) {
                    LOGGER.info("本地目录不存在");
                    file.getParentFile().mkdirs();
                    LOGGER.info("本地目录创建完毕");
                }
                file.createNewFile();
                LOGGER.info("本地文件创建完毕");

                if (validateRemoteFileIsExist(urlPath)) {
                    LOGGER.info("从远程获取文件写入本地开始");
                    URL url2 = new URL(urlPath);
                    InputStream is = url2.openStream();
                    OutputStream os = new FileOutputStream(fullPath);
                    byte bf[] = new byte[102400];
                    int length = 0;
                    while ((length = is.read(bf, 0, 102400)) != -1) {
                        os.write(bf, 0, length);
                    }
                    is.close();
                    os.close();
                    LOGGER.info("从远程获取文件写入本地结束");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!validateRemoteFileIsExist(urlPath)) {
            LOGGER.info("{} upload begin", fileName);
            String ossPath = dbFilePath.substring(1);
            LOGGER.info("ossPath : {}", ossPath);
            upload2oss(fullPath, ossPath);
            LOGGER.info("{} upload end", fileName);
        }
    }

    public static Long getMp4Duration(String filePath) {
        File source = new File(filePath);
        long duration = 0L;
        try {
            MultimediaObject instance = new MultimediaObject(source);
            MultimediaInfo result = instance.getInfo();
            duration = result.getDuration() / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return duration;
    }

    /**
     * 计算文件信息摘要值，比如MD5，SHA256等
     *
     * @param file
     * @param algorithm
     * @return
     */
    public static String getFileMessageDigest(File file, String algorithm) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[102400];
        int len;
        try {
            digest = MessageDigest.getInstance(algorithm);
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            BigInteger bigInt = new BigInteger(1, digest.digest());
            String digestInfo = bigInt.toString();
            LOGGER.info("digestInfo : {}", digestInfo);
            return digestInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean compareContentEquals(String originalFileFullPath, String newFileFullPath) {

        LOGGER.info("newFileFullPath : {}", newFileFullPath);
        File newFile = new File(newFileFullPath);

        LOGGER.info("originalFileFullPath : {}", originalFileFullPath);
        File originalFile = new File(originalFileFullPath);

        String newFileDigest = FileUtil.getFileMessageDigest(newFile, FileUtil.ALGORITHM_SHA_1);
        String originalFileDigest = FileUtil.getFileMessageDigest(originalFile, FileUtil.ALGORITHM_SHA_1);
        LOGGER.info("newFileDigest : {}", newFileDigest);
        LOGGER.info("originalFileDigest : {}", originalFileDigest);
        // 文件一致
        if (StringUtils.equals(newFileDigest, originalFileDigest)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validateRemoteFileIsExist(String urlPath) {
        LOGGER.info("urlPath : {}", urlPath);
        boolean flag = false;
        try {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            int state = conn.getResponseCode();
            if (state == 200) {
                LOGGER.info("远程文件存在");
                flag = true;
            } else {
                LOGGER.info("远程文件不存在");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static void img2oss(String dbFilePath, String fileName) {
        LOGGER.info("dbFilePath : {}", dbFilePath);
        if (StringUtils.isBlank(dbFilePath)) {
            LOGGER.info("dbFilePath is null");
            return;
        }
        // 获取完整的存储路径
        String fullPath = UploadProperties.storageFolder + dbFilePath.replace("/upload", "");
        String urlPath = ProjectProperties.domain + dbFilePath;
        LOGGER.info("dbFilePath fullPath : {}", fullPath);
        File file = new File(fullPath);
        if (file.exists()) {
            LOGGER.info("本地文件存在");
        } else {
            LOGGER.info("本地文件不存在");
            try {
                if (!file.getParentFile().exists()) {
                    LOGGER.info("本地目录不存在");
                    file.getParentFile().mkdirs();
                    LOGGER.info("本地目录创建完毕");
                }
                file.createNewFile();
                LOGGER.info("本地文件创建完毕");

                if (validateRemoteFileIsExist(urlPath)) {
                    LOGGER.info("从远程获取文件写入本地开始");
                    URL url2 = new URL(urlPath);
                    InputStream is = url2.openStream();
                    OutputStream os = new FileOutputStream(fullPath);
                    byte bf[] = new byte[102400];
                    int length = 0;
                    while ((length = is.read(bf, 0, 102400)) != -1) {
                        os.write(bf, 0, length);
                    }
                    is.close();
                    os.close();
                    LOGGER.info("从远程获取文件写入本地结束");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!validateRemoteFileIsExist(urlPath)) {
            LOGGER.info("{} upload begin", fileName);
            String ossPath = dbFilePath.substring(1);
            LOGGER.info("ossPath : {}", ossPath);
            upload2oss(fullPath, ossPath);
            LOGGER.info("{} upload end", fileName);
        }
    }

    public static boolean validateFileExist(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 网络文件创建
     *
     * @param webPath
     * @param path
     * @return
     */
    public static boolean webFileCreate(String webPath, String path) {
        LOGGER.info("webPath : {}", webPath);
        LOGGER.info("path : {}", path);
        try {

            //new一个URL对象
            URL url = new URL(webPath);
            //打开链接
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            //设置请求方式为"GET"
            conn.setRequestMethod("GET");
            //超时响应时间为5秒
            conn.setConnectTimeout(5 * 1000);
            //通过输入流获取图片数据
            InputStream inStream = conn.getInputStream();
            //得到图片的二进制数据，以二进制封装得到数据，具有通用性
            byte[] data = readInputStream(inStream);
            //new一个文件对象用来保存图片，默认保存当前工程根目录
            File webFile = new File(path);
            if (!webFile.exists()) {
                LOGGER.info("本地文件不存在");
                webFile.createNewFile();
            }
            //创建输出流
            FileOutputStream outStream = new FileOutputStream(webFile);
            //写入数据
            outStream.write(data);
            //关闭输出流
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        LOGGER.info("读取网络放返回的信息");
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[102400];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    public static void upload2oss(String localPath, String ossPath) {

        LOGGER.info("oss上传文件开始");

        // Endpoint以杭州为例，其它Region请按实际情况填写。
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        // 上传文件。<yourLocalFile>由本地文件路径加文件名包括后缀组成，例如/users/local/myfile.txt。
//        String path = "E:/upload/ce/ce3fbab0f32799958e87456b5061a534/file.jpg";
//        Long curTime = TimeUtil.curTime();
//        String objectName = "upload/ce/ce3fbab0f32799958e87456b5061a534/"+ curTime + ".jpg";

        ossClient.putObject(bucketName, ossPath, new File(localPath));

        // 关闭OSSClient。
        ossClient.shutdown();
        LOGGER.info("oss上传文件结束");
    }

    /**
     * 获取操作系统全路径
     * @param dbFilePath 数据库文件路径
     * @return 操作系统全路径
     */
    public static String getOsFullPath(String dbFilePath) {
        String storageFolder = UploadProperties.storageFolder;
        LOGGER.info("storageFolder : {}", storageFolder);
        StringBuffer sb = new StringBuffer();
        sb.append(dbFilePath);
        sb.append(storageFolder.replaceFirst("/", ""));
        LOGGER.info("path : {}", sb.toString());
        return sb.toString();
    }

    public static void main(String[] args) {
        String priceStr = "0.01";
        System.out.println(priceStr.matches("\\d+(\\.\\d{1,2})?"));

//        upload2oss();
    }
}
