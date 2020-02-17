import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * @description:
 * @author: zhoulupeng
 * @date: Created in 2020/2/17 18:55
 * @version: 1.0
 * @modified By:
 */
public class DatagramChannelSenderTest {

    public static void main(String[] args) throws IOException {
        // 数据报发送方
        DatagramChannel datagramChannel = DatagramChannel.open();
        ByteBuffer byteBuffer = ByteBuffer.wrap("DatagramChannel Sender".getBytes());
        int byteSend = datagramChannel.send(byteBuffer, new InetSocketAddress("127.0.0.1", 50020));
        System.out.println("byte send is : " + byteSend);
    }
}
