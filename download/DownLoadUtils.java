import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 多线程下载文件
 *
 * @description:
 * @author: zhoulupeng
 * @date: Created in 2020/2/18 17:14
 * @version: 1.0
 * @modified By:
 */
public class DownLoadUtils {

    // 下载的目标文件url
    private String urlPath;
    // 确认保存文件的位置
    private String targetFile;
    // 下载用几个线程
    private int threadNum;
    // 现在线程数组类
    private DownLoadThread[] threads;
    // 目标文件大小
    private int fileSize;

    public DownLoadUtils(String urlPath, String targetFile, int threadNum) {
        this.urlPath = urlPath;
        this.targetFile = targetFile;
        this.threadNum = threadNum;
        this.threads = new DownLoadThread[threadNum];
    }

    public static void main(String[] args) throws IOException {

        /**
         * 多线程下载文件
         *
         * 断点续传，是在多线程下载基础上，添加一个配置文件，记录每个线程下载到哪个字节，
         *
         * 断网再次下载时，只需要继续从上次下载的位置继续下载即可。
         *
         * 迅雷下载时也是生成了两个文件，另外一个小文件就是配置文件
         */

        String urlPath = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1582031309318&di=985bbeb7401ea15311f25d5f888333ae&imgtype=0&src=http%3A%2F%2Fgss0.baidu.com%2F9vo3dSag_xI4khGko9WTAnF6hhy%2Fzhidao%2Fpic%2Fitem%2F241f95cad1c8a786d814d6eb6709c93d70cf501c.jpg";
        DownLoadUtils downLoadUtils = new DownLoadUtils(urlPath, "E:/test.jpg", 5);
        downLoadUtils.download();
        new Thread(new Runnable() {

            @Override
            public void run() {
//                double rate = downLoadUtils.getCompleteRate();
                while (downLoadUtils.getCompleteRate() <= 1) {
                    System.out.println("已经下载:" + downLoadUtils.getCompleteRate());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 实现下载文件的方法
     */
    public void download() throws IOException {
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // 超时时间
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "*/*");// 允许处理所有文件
        conn.setRequestProperty("Accept-Language", "zh-CN");
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setRequestProperty("Connection", "Keep-Alive");

//        conn.connect();// 连接，可省略
        fileSize = conn.getContentLength();
        System.out.println("download fileSize : " + fileSize);

        conn.disconnect();//关闭资源

        // 根据文件的大小和下载线程的数量，进行目标文件的切块
        // 在本地创建一个和目标文件相同大小的文件
        RandomAccessFile file = new RandomAccessFile(targetFile, "rw");
        file.setLength(fileSize);
        file.close();

        // 每个线程负责下载的文件块的大小
        int currentPartSize = fileSize / threadNum + 1;

        for (int i = 0; i < threadNum; i++) {

            // 每个线程下载的文件块的开始值
            int startPos = i * currentPartSize;
            RandomAccessFile currentPart = new RandomAccessFile(targetFile, "rw");
            currentPart.seek(startPos);
            threads[i] = new DownLoadThread(startPos, currentPartSize, currentPart);
            threads[i].start();
        }

    }

    /**
     * 获取下载完成百分比
     *
     * @return
     */
    public double getCompleteRate() {
        int sumSize = 0;
        for (int i = 0; i < threadNum; i++) {
            sumSize += threads[i].length;
        }
        return sumSize * 1.0 / fileSize;
    }

    private class DownLoadThread extends Thread {

        /**
         * 线程类里具体实现
         * <p>
         * 连接到远程的文件，然后把文件分成几块，让线程分别去下载，
         */

        private int startPos;
        private int currentPartSize;
        private RandomAccessFile currentPart;

        private int length;// 记录每个线程下载下来的字节数

        public DownLoadThread(int startPos, int currentPartSize, RandomAccessFile currentPart) {
            this.startPos = startPos;
            this.currentPartSize = currentPartSize;
            this.currentPart = currentPart;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(urlPath);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // 超时时间
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "*/*");// 允许处理所有文件
                conn.setRequestProperty("Accept-Language", "zh-CN");
                conn.setRequestProperty("Charset", "UTF-8");
                conn.setRequestProperty("Connection", "Keep-Alive");

//                conn.connect();// 连接，可省略
                InputStream in = conn.getInputStream();
                // 把in的指针，跳到该线程负责下载的位置
                in.skip(this.startPos);
                byte[] buffer = new byte[1024];
                int hasRead = 0;// 文件长度
                while (length < currentPartSize && (hasRead = in.read(buffer)) != -1) {
                    currentPart.write(buffer, 0, hasRead);
                    length += hasRead;
                }
                currentPart.close();
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
