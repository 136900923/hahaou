package chat.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * 服务器 Socket 线程类
 * <p>
 * 处理与客户端进行数据交互
 *
 * @description:
 * @author: zhoulupeng
 * @date: Created in 2020/2/19 11:27
 * @version: 1.0
 * @modified By:
 */
public class ServerThread implements Runnable {

    private Socket socket;

    private BufferedReader br;

    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;
        br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "GBK"));
    }

    @Override
    public void run() {
        String content;// 收到的信息，也是广播出去的信息

        // 不断接收 client 发送过来的信息
        while ((content = readFromClient()) != null) {
            // 广播给其他的客户端
            for (Socket s : Server.socketList) {
                try {
                    PrintStream ps = new PrintStream(s.getOutputStream(), true, "GBK");
                    ps.println(content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 定义 server 接收 client 信息的方法
     *
     * @return
     */
    private String readFromClient() {
        try {

            // 读到信息就返回，对应的客户端 Socket 如果关闭，信息就读不到，异常捕获
            return br.readLine();
        } catch (Exception e) {
            // 读不到信息，表示 client 已经关了，把关掉的 client 从 list 集合中删除
            Server.socketList.remove(socket);
        }
        return null;
    }
}
