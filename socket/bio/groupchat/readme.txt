socket多线程仿QQ群聊简单实现

思路如下：

server端

1、创建集合保存所有发送过来的socket
2、使用线程类处理每个socket，遍历server端的所有socket，把接收到的client socket信息广播给所有的client socket

client端
1、获取键盘输入的信息，发送给服务器
2、创建线程类，不停的把server发送来的数据进行获取和显示
