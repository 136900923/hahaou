package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
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

        // client send data
        PrintStream ps = new PrintStream(socket.getOutputStream(), true, "GBK");
        ps.println("我是客户端");

        // 客户端读取服务器端发送的数据
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "GBK"));
        String str = in.readLine();
        System.out.println("服务器发送的数据:" + str);

        in.close();
        ps.close();
        socket.close();

    }
}
