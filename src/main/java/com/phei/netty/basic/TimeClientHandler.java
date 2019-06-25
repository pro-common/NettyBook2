package com.phei.netty.basic;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;

/**
 * ClassName: TimeClientHandler <br/>
 * Function: 【3.2】Netty 客户端 Handler 处理类. <br/>
 * date: 2019年6月24日 下午3:53:16 <br/>
 *
 * @version
 * @since JDK 1.8
 * @author kaiyun
 */
public class TimeClientHandler extends ChannelHandlerAdapter {

	private static final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());

	private final ByteBuf firstMessage;

	/**
	 * Creates a client-side handler.
	 */
	public TimeClientHandler() {
		byte[] req = "QUERY TIME ORDER".getBytes();
		firstMessage = Unpooled.buffer(req.length);
		firstMessage.writeBytes(req);

	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		// 当客户端和服务端TCP链路建立成功之后，Netty 的 NIO 线程会调用 channelActive 方法，发送查询时间的指令给服务端，
		// 调用 ChannelHandlerContext 的 writeAndFlush 方法将请求消息发送给服务端。
		ctx.writeAndFlush(firstMessage);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 当服务端返回应答消息时，channelRead 方法被调用。
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req, "UTF-8");
		System.out.println("Now is : " + body);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// 释放资源
		logger.warning("Unexpected exception from downstream : " + cause.getMessage());
		ctx.close();
	}
}
