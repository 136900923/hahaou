import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * post方式发送数据
 *
 * @description:
 * @author: zhoulupeng
 * @date: Created in 2020/2/18 20:45
 * @version: 1.0
 * @modified By:
 */
public class PostSendData {

    public static void main(String[] args) throws IOException {


        String urlPath = "请求地址";
        String param = "name="+ URLEncoder.encode("张三", StandardCharsets.UTF_8.name());
        // 建立连接
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // POST请求数据必须设置参数
        conn.setDoOutput(true);
        conn.setDoInput(true);

        conn.setUseCaches(false);// 禁用缓存
        conn.setRequestMethod("POST");

        // 设置请求头信息
        conn.setRequestProperty("Charset", StandardCharsets.UTF_8.name());
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // 获取输出流，把param放到放到请求体中
        conn.connect();
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        dos.writeBytes(param);
        dos.flush();
        dos.close();

        int code = conn.getResponseCode();
        if (code == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

    }
}
