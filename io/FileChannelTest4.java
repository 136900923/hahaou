import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @description:
 * @author: zhoulupeng
 * @date: Created in 2020/2/17 17:00
 * @version: 1.0
 * @modified By:
 */
public class FileChannelTest4 {

    static final String filePath = "D:\\car.txt";

    public static void main(String[] args) {
//        System.out.println("start to write");
        // 通过FileChannel写入数据
//        testWriteOnFileChannel();

//        System.out.println("start to read");
        // 通过FileChannel读取数据
        testReadOnFileChannel();
    }

    /**
     * 文件写操作
     */
    public static void testWriteOnFileChannel() {
        try {
            RandomAccessFile randomAccess = new RandomAccessFile(filePath, "rw");
            FileChannel fileChannel = randomAccess.getChannel();
            byte[] bytes = new String("Java Non-blocking IO").getBytes();
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);

            // 将缓冲区的字节写入文件通道中
            fileChannel.write(byteBuffer);
            // 强制将通道中未写入磁盘的数据立刻写入到磁盘
            fileChannel.force(true);
            // 清空缓冲区，释放内存
            byteBuffer.clear();
            fileChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testReadOnFileChannel() {
        try {
            FileInputStream inputStream = new FileInputStream(new File(filePath));
            FileChannel fileChannel = inputStream.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(10);
            while (fileChannel.read(byteBuffer) != -1) {
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()) {
                    System.out.print((char) byteBuffer.get());
                }
                byteBuffer.clear();
            }
            fileChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
