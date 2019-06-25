package com.phei.netty.frame.correct;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * ClassName: TimeServer <br/>
 * Function: 【4.3】Netty 服务端（利用LineBasedFramDecoder解决TCP粘包问题）. <br/>
 * date: 2019年6月25日 上午11:18:52 <br/>
 *
 * @version 
 * @since JDK 1.8
 * @author kaiyun
 */
public class TimeServer {

    public void bind(int port) throws Exception {
        // 配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
        	// 创建服务端启动辅助类并对其进行配置。（ServerBootstrap是 Netty 用于启动 NIO 服务端的辅助启动类，目的是降低服务端的开发复杂度。）
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)		// 它的功能对应于 JDK NIO类库中的 ServerSocketChannel类
                    .option(ChannelOption.SO_BACKLOG, 1024)		// 配置 NioServerSocketChannel 的TCP参数
                    .childHandler(new ChildChannelHandler());	// 它的作用类似于 Reactor 模式中的 Handler 类，主要用于处理网络I/O事件，例如记录日志、对消息进行编解码等。
            // 绑定端口，同步等待成功
            ChannelFuture f = b.bind(port).sync();

            // 等待服务端监听端口关闭，等待服务端链路关闭之后 main 函数才退出
            f.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel arg0) {
            // 在此管道的最后位置插入通道处理程序。
            // 在原来的客户端的处理类 前加上了两个解码器 一个处理粘包, 一个处理String的解码
            arg0.pipeline().addLast(new LineBasedFrameDecoder(1024)); // 并且设置单行最大长度
            arg0.pipeline().addLast(new StringDecoder());
            arg0.pipeline().addLast(new TimeServerHandler());
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
        new TimeServer().bind(port);
    }
}
