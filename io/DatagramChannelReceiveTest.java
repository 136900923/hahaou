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
public class DatagramChannelReceiveTest {

    public static void main(String[] args) throws IOException {
        // 数据报接收方
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.bind(new InetSocketAddress(50020));

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        datagramChannel.receive(byteBuffer);
        byteBuffer.flip();
        while (byteBuffer.hasRemaining()) {
            System.out.println((char) byteBuffer.get());
        }
    }
}
