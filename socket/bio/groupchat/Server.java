package chat.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @description:
 * @author: zhoulupeng
 * @date: Created in 2020/2/18 23:54
 * @version: 1.0
 * @modified By:
 */
public class Server {

    public static List<Socket> socketList = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) throws IOException {

        /**
         * server 要做的事情
         *
         * 在 server 端使用 List 来保存所有连接进来的 client socket
         *
         * 使用多线程处理每个连接上的 socket
         *
         * server 要把收到的 client 的信息 “广播” 给其他的 client
         *
         */

        // 根据流程第一步，创建 ServerSocket
        ServerSocket serverSocket = new ServerSocket();// 无参数，表示没有连接 Socket
        serverSocket.bind(new InetSocketAddress("127.0.0.1", 40000));

        // 用循环来接收 client 的连接
        while (true) {

            // 获取连接进来 client Socket
            Socket clientSocket = serverSocket.accept();// 此方法会阻塞，一直等到接收到client Socket 请求才会继续，接收的(调用方法返回的) Socket 与 client 发送的 Socket 对应
            // 将请求的 clientSocket 添加到集合中
            socketList.add(clientSocket);

            // 多线程处理 Socket 连接，每进来一个 Socket 连接，就创建一个线程
            new Thread(new ServerThread(clientSocket)).start();
//            clientSocket.close();// 此方法不能有，关闭后，无法打印数据

        }
    }
}
