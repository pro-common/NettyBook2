package com.phei.netty.codec.msgpack;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * ClassName: EchoServerHandler <br/>
 * Function: 【7.2】Netty 服务端 Handler. <br/>
 * date: 2019年6月25日 下午4:16:05 <br/>
 *
 * @version 
 * @since JDK 1.8
 * @author kaiyun
 */
@Sharable
public class EchoServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("Server receive the msgpack message : " + msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();// 发生异常，关闭链路
    }
}
