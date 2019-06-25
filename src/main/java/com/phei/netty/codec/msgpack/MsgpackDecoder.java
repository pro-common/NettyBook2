/**
 * Project Name:nettybook2
 * File Name:MsgpackDecoder.java
 * Package Name:com.phei.netty.codec.msgpack
 * Date:2019年6月25日下午9:17:04
 * Copyright (c) 2019, kaiyun@qk365.com All Rights Reserved.
 *
*/

package com.phei.netty.codec.msgpack;

import java.util.List;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * ClassName:MsgpackDecoder <br/>
 * Function: 【7.2】MessagePack 解码器. <br/>
 * Date:     2019年6月25日 下午9:17:04 <br/>
 * @author   kaiyun
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf> {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		// 首先从数据报 msg 中获取需要解码的 byte 数组
		final byte[] array;
		final int length = msg.readableBytes();
		array = new byte[length];
		msg.getBytes(msg.readerIndex(), array, 0, length);
		// 然后调用 MessagePack 的 read 方法将其反序列化为 Object 对象，将解码后的对象加入到解码列表 out 中，这样就完成了 MessagePack 的解码操作
		MessagePack msgpack = new MessagePack();
		out.add(msgpack.read(array));
		
	}


}

