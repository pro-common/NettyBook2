package com.phei.netty.frame.fault;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;

/**
 * ClassName: TimeClientHandler <br/>
 * Function: 【4.2】Netty 客户端 Handler 处理类. <br/>
 * date: 2019年6月24日 下午5:53:22 <br/>
 *
 * @version
 * @since JDK 1.8
 * @author kaiyun
 */
public class TimeClientHandler extends ChannelHandlerAdapter {

	private static final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());

	private int counter;

	private byte[] req;

	/**
	 * Creates a client-side handler.
	 */
	public TimeClientHandler() {
		req = ("QUERY TIME ORDER" + System.getProperty("line.separator")).getBytes();
	}

	/**
	 * 建立连接后就循环发送一百条消息, 发送一次就刷新一次 保证每条消息都写入Channel, 预想中 客户端会输出一百次服务端的时间 然而由于粘包,
	 * 消息都粘在了一起, 服务端收到的是一坨的消息
	 * 
	 * @param ctx
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		ByteBuf message;
		for (int i = 0; i < 100; i++) {
			message = Unpooled.buffer(req.length);
			message.writeBytes(req);
			ctx.writeAndFlush(message);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req, "UTF-8");
		System.out.println("Now is : " + body + " ; the counter is : " + ++counter);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// 释放资源
		logger.warning("Unexpected exception from downstream : " + cause.getMessage());
		ctx.close();
	}
}
