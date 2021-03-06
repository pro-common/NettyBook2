package com.phei.netty.basic;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * ClassName: TimeClient <br/>
 * Function: 【3.2】Netty 客户端. <br/>
 * date: 2019年6月24日 下午4:45:23 <br/>
 *
 * @version 
 * @since JDK 1.8
 * @author kaiyun
 */
public class TimeClient {

    public void connect(int port, String host) throws Exception {
        // 配置客户端NIO线程组（处理 I/O 读写的 NioEventLoopGroup 线程组）
        EventLoopGroup group = new NioEventLoopGroup();
        try {
        	// 创建客户端辅助启动类并对其进行配置
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                        	// 在进行初始化时，将NioSocketChannel的ChannelHandlerContext 设置到 ChannelPipeline 中，用于处理网络I/O事件。
                            ch.pipeline().addLast(new TimeClientHandler());
                            System.out.println("添加一个管道");
                        }
                    });

            // 发起异步连接操作
            ChannelFuture f = b.connect(host, port).sync();

            // 当代客户端链路关闭
            f.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放NIO线程组
            group.shutdownGracefully();
        }
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }
        new TimeClient().connect(port, "127.0.0.1");
        System.out.println("断开");
    }
}
