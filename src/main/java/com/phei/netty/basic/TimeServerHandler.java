package com.phei.netty.basic;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import java.nio.charset.StandardCharsets;

/**
 * ClassName: TimeServerHandler <br/>
 * Function: 【3.2】Netty 服务端 Handler 处理类（继承自 ChannelHandlerAdapter主要对网络事件进行读写操作）. <br/>
 * date: 2019年6月24日 下午3:57:08 <br/>
 *
 * @version 
 * @since JDK 1.8
 * @author kaiyun
 */
public class TimeServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
    	// 将 msg 转换成 Netty 的 ByteBuf 对象。（ByteBuf 类似于 JDK 中的 java.nio.ByteBuffer 对象，但它提供了更强大和灵活的功能）
        ByteBuf buf = (ByteBuf) msg;
        // 通过 buf.readableBytes() 方法获取缓冲区可读的字节数，根据可读的字节数创建 byte 数组
        byte[] req = new byte[buf.readableBytes()];
        // 将缓冲区中的字节数组复制到新建的 byte 数组中
        buf.readBytes(req);
        // 通过 new String 构造函数获取请求消息
        String body = new String(req, StandardCharsets.UTF_8);
        System.out.println("The time server receive order : " + body);
        // 对请求消息进行判断
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new java.util.Date(
                System.currentTimeMillis()).toString() : "BAD ORDER";
        // 
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        // ChannelHandlerContext 的 write 方法异步发送应答消息给客户端
        ctx.write(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    	// ChannelHandlerContext 的 flush 方法，是将发送队列中的消息写入到 SocketChannel 中发送给对方。
    	// 从性能角度考虑，为了防止频繁地唤醒 Selector进行消息发送，Netty的write方法并不直接将消息写入 SocketChannel 中，
    	// 调用 write 方法只是把待发送的消息放到发送缓存区数组中，再通过调用 flush 方法，将发送缓冲区的消息全部写到 SocketChanel 中。
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	// 当发生异常时，关闭 ChannelHandlerContext，释放和 ChannelHandlerContext 相关联的句柄等资源。
        ctx.close();
    }
}
