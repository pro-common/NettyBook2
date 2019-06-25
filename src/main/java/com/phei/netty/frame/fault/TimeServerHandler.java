package com.phei.netty.frame.fault;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * ClassName: TimeServerHandler <br/>
 * Function: 【4.2】Netty 服务端 Handler 处理类. <br/>
 * date: 2019年6月24日 下午5:50:58 <br/>
 *
 * @version
 * @since JDK 1.8
 * @author kaiyun
 */
public class TimeServerHandler extends ChannelHandlerAdapter {

	private int counter;

	/**
	 * 服务端收到请求后就计数,并发回给客户端
	 * 
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req, "UTF-8").substring(0, req.length - System.getProperty("line.separator").length());

		System.out.println("The time server receive order : " + body + " ; the counter is : " + ++counter);
		String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)
				? new java.util.Date(System.currentTimeMillis()).toString()
				: "BAD ORDER";
		currentTime = currentTime + System.getProperty("line.separator");
		ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
		ctx.writeAndFlush(resp);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
	}
}
