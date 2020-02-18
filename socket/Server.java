package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @description:
 * @author: zhoulupeng
 * @date: Created in 2020/2/18 23:54
 * @version: 1.0
 * @modified By:
 */
public class Server {

    public static void main(String[] args) throws IOException {
        // 根据流程第一步，创建 ServerSocket
        ServerSocket serverSocket = new ServerSocket();// 无参数，表示没有连接 Socket
        serverSocket.bind(new InetSocketAddress("127.0.0.1", 40000));

        // 用循环来接收 client 的连接
        while (true) {

            // 获取连接进来 client Socket
            Socket clientSocket = serverSocket.accept();// 此方法会阻塞，一直等到接收到client Socket 请求才会继续，接收的(调用方法返回的) Socket 与 client 发送的 Socket 对应

            // 通过 client 的输入流接收数据
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "GBK"));

            // 获取 client 发送的信息
            String str = in.readLine();
            System.out.println("客户端来的信息:" + str);

            // 发送给 client 信息
            PrintStream ps = new PrintStream(clientSocket.getOutputStream(), true, "GBK");
            ps.println("我是服务器");

            // 关闭流和 client socket
            ps.close();
            in.close();
            clientSocket.close();

        }
    }
}
