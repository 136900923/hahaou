package chat.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * 客户端 Socket 线程类
 * <p>
 * 负责获取和显示从服务端发送来的信息
 *
 * @description:
 * @author: zhoulupeng
 * @date: Created in 2020/2/19 14:40
 * @version: 1.0
 * @modified By:
 */
public class ClientThread implements Runnable {

    private Socket socket;

    private BufferedReader in;

    public ClientThread(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "GBK"));
    }

    @Override
    public void run() {

        // 这里获取和显示从服务端发送来的信息

        String content = null;
        try {

            // 使用循环不断获取 server 发送的 data
            while ((content = in.readLine()) != null) {
                System.out.println(content);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
