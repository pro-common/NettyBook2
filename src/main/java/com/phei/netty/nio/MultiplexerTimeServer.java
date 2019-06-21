package com.phei.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * ClassName: MultiplexerTimeServer <br/>
 * Function: 【2.3】 NIO服务端多路复用类. <br/>
 * date: 2019年6月21日 上午11:30:52 <br/>
 *
 * @version 
 * @since JDK 1.8
 * @author kaiyun
 */
public class MultiplexerTimeServer implements Runnable {

    private Selector selector;

    private volatile boolean stop;

    /**
     * 初始化多路复用器、绑定监听端口
     *
     * @param port 端口
     */
    public MultiplexerTimeServer(int port) {
        try {
            // 打开 ServerSoketChannel，用于监听客户端的连接，它是所有客户端连接的父管道
            ServerSocketChannel servChannel = ServerSocketChannel.open();
            // 绑定监听端口，设置连接为非阻塞模式
            servChannel.socket().bind(new InetSocketAddress(port), 1024);
            servChannel.configureBlocking(false);
            // 创建多路复用器
            selector = Selector.open();
            // 将管道（ ServerSoketChannel）注册到多路复用器上，监听 accept 事件
            servChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("The time server is start in port : " + port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
            	// 多路复用器在线程 run 方法的无限循环体内轮询准备就绪的 Key
                selector.select(1000);
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectedKeys.iterator();
                SelectionKey key;
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null)
                                key.channel().close();
                        }
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        // 多路复用器关闭后，所有注册在上面的Channel和Pipe等资源都会被自动去注册并关闭，所以不需要重复释放资源
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {

        if (key.isValid()) {
            if (key.isAcceptable()) {
            	// 多路复用器监听到有新的客户端接入，处理新接入的请求，完成 TCP 三次握手，建立物理链路
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                // 将新接入的客户端连接注册到多路复用器上，监听读操作，读取客户端发送的网络消息
                sc.register(selector, SelectionKey.OP_READ);
            }
            // 异步读取客户端请求消息到缓存区
            if (key.isReadable()) {
                SocketChannel sc = (SocketChannel) key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);//开辟一个1MB的缓存区
                int readBytes = sc.read(readBuffer);//读取请求码流（由于已经将 SocketChannel 设置为异步阻塞模式，因此它的read是非阻塞的）
                if (readBytes > 0) {
                    readBuffer.flip();//将缓冲区当前的 limit 设置为 position，position设置为0，用于后续对缓存区的读取操作
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);//将缓存区可读的字节数组复制到新创建的字节数组中
                    String body = new String(bytes, "UTF-8");//调用字符串的构造函数创建请求消息体
                    System.out.println("The time server receive order : "
                            + body);
                    String currentTime = "QUERY TIME ORDER"
                            .equalsIgnoreCase(body) ? new java.util.Date(
                            System.currentTimeMillis()).toString()
                            : "BAD ORDER";
                    if("STOP".equalsIgnoreCase(body)){
                        stop();
                    }
                    // 将ByteBuffer，调用 SocketChannel 的异步 write 接口，将消息异步发送给客户端
                    doWrite(sc, currentTime);
                } else if (readBytes < 0) {
                    // 对端链路关闭
                    key.cancel();
                    sc.close();
                }
                // 读到0字节，忽略
            }
        }
    }

    private void doWrite(SocketChannel channel, String response)
            throws IOException {
        if (response != null && response.trim().length() > 0) {
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);//将字节数组复制到缓冲区中
            writeBuffer.flip();//将缓冲区当前的 limit 设置为 position，position设置为0，用于后续对缓存区的读取操作
            channel.write(writeBuffer);//将缓冲区中的字节数组发送出去
        }
    }
}
