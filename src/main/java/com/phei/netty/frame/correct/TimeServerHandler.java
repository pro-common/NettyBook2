package com.phei.netty.frame.correct;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * ClassName: TimeServer <br/>
 * Function: 【4.3】Netty 服务端 Handler 处理类. <br/>
 * date: 2019年6月25日 上午11:18:52 <br/>
 *
 * @version 
 * @since JDK 1.8
 * @author kaiyun
 */
public class TimeServerHandler extends ChannelHandlerAdapter {

    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 以下两行 简化了String的解码, 也没有考虑粘包问题
        String body = (String) msg;
        System.out.println("The time server receive order : " + body
                + " ; the counter is : " + ++counter);

        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new java.util.Date(
                System.currentTimeMillis()).toString() : "BAD ORDER";
        currentTime = currentTime + System.getProperty("line.separator");
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.writeAndFlush(resp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
