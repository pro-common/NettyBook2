package com.phei.netty.codec.msgpack;

import com.phei.netty.codec.serializable.UserInfo;

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

    private final int sendNumber;

    public EchoClientHandler(int sendNumber) {
    	this.sendNumber = sendNumber;
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
    	UserInfo[] infos = UserInfo();
    	for(UserInfo infoE : infos) {
    		ctx.write(infoE);
    	}
    }
    private UserInfo[] UserInfo() {
    	UserInfo[] userInfos = new UserInfo[sendNumber];
    	UserInfo userInfo = null;
    	for(int i=0; i<sendNumber; i++) {
    		userInfo = new UserInfo();
    		userInfo.setUserName("ABCDEFG --->" + i);
    		userInfos[i] = userInfo;
    	}
    	return userInfos;
    }

    /**
     * 	当服务端回应消息时,该方法被调用, 因为所有过往的消息都会被解码, 所以服务端发回来客户端发送的消息就需要 重新添加分隔符
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("Client receive the msgpack message : " + msg);
        ctx.write(msg);
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
