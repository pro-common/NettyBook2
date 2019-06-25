package com.phei.netty.codec.msgpack;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * ClassName: EchoClientHandler <br/>
 * Function: 【7.2】Netty 客户端handler 处理类. <br/>
 * date: 2019年6月25日 下午2:56:32 <br/>
 *
 * @version 
 * @since JDK 1.8
 * @author kaiyun
 */
public class EchoClientHandler extends ChannelHandlerAdapter {

    private int counter;

    private static final String ECHO_REQ = "Hi, Lilinfeng. Welcome to Netty.$_";

    /**
     * Creates a client-side handler.
     */
    public EchoClientHandler() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // ByteBuf buf = UnpooledByteBufAllocator.DEFAULT.buffer(ECHO_REQ
        // .getBytes().length);
        // buf.writeBytes(ECHO_REQ.getBytes());
        // 循环发送消息
        for (int i = 0; i < 10; i++) {
            ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_REQ.getBytes()));
        }
    }

    /**
     * 	当服务端回应消息时,该方法被调用, 因为所有过往的消息都会被解码, 所以服务端发回来客户端发送的消息就需要 重新添加分隔符
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("This is " + ++counter + " times receive server : [" + msg + "]");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
