package chat.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 客户端
 * <p>
 * 负责获取和显示 server 发送过来的信息
 *
 * @description:
 * @author: zhoulupeng
 * @date: Created in 2020/2/18 23:54
 * @version: 1.0
 * @modified By:
 */
public class Client {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("127.0.0.1", 40000));

        /**
         * 开启子线程，负责获取和显示 server 发送过来的信息
         *
         * 主线程，从键盘上获取信息，发送给服务器
         *
         * 子线程，不停从服务器获取信息然后显示
         */
        new Thread(new ClientThread(socket)).start();

        // 读取键盘的信息
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, "GBK"));
        // client send data 给 server
        PrintStream ps = new PrintStream(socket.getOutputStream(), true, "GBK");

        String line = null;
        while ((line = in.readLine()) != null) {
            ps.println(line);
        }

        in.close();
        ps.close();
        socket.close();

    }
}
