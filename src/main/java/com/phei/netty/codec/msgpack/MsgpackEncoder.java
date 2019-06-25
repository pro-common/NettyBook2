package com.phei.netty.codec.msgpack;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * ClassName:MsgpackEncoder <br/>
 * Function: 【7.2】MessagePack 编码器. <br/>
 * Date:     2019年6月25日 下午8:39:56 <br/>
 * @author   kaiyun
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
public class MsgpackEncoder extends MessageToByteEncoder<Object> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
		MessagePack msgpack = new MessagePack();
		byte[] raw = msgpack.write(msg);
		out.writeBytes(raw);
	}

}

