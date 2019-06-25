package com.phei.netty.frame.delimiter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * ClassName: EchoServerHandler <br/>
 * Function: 【5.1】Netty 服务端（自定义分隔符）Handler 处理类. <br/>
 * date: 2019年6月25日 下午2:50:41 <br/>
 *
 * @version 
 * @since JDK 1.8
 * @author kaiyun
 */
@Sharable
public class EchoServerHandler extends ChannelHandlerAdapter {

    int counter = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String body = (String) msg;
        // 因为分隔符被DelimiterBaseFrameDecoder解码后就丢掉了, 所以需要手动加回去, 然后发送给客户端 才能让客户端正确的解码
        System.out.println("This is " + ++counter + " times receive client : [" + body + "]");
        body += " callback $_";
        ByteBuf echo = Unpooled.copiedBuffer(body.getBytes());
        ctx.writeAndFlush(echo);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();// 发生异常，关闭链路
    }
}
