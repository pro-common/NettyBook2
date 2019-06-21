package com.phei.netty.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * ClassName: ReadCompletionHandler <br/>
 * Function: 【2.4】AIO 服务端 接受通知回调的业务 Handler. <br/>
 * date: 2019年6月21日 下午4:57:59 <br/>
 *
 * @version
 * @since JDK 1.8
 * @author kaiyun
 */
public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

	private AsynchronousSocketChannel channel;

	public ReadCompletionHandler(AsynchronousSocketChannel channel) {
		if (this.channel == null)
			this.channel = channel;
	}

	@Override
	public void completed(Integer result, ByteBuffer attachment) {
		// 为后续从缓冲区读取数据做准备
		attachment.flip();
		byte[] body = new byte[attachment.remaining()];
		//将缓存区可读的字节数组复制到新创建的字节数组中
		attachment.get(body);
		try {
			String req = new String(body, "UTF-8");
			System.out.println("The time server receive order : " + req);
			String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(req)
					? new java.util.Date(System.currentTimeMillis()).toString()
					: "BAD ORDER";
			doWrite(currentTime);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void doWrite(String currentTime) {
		if (currentTime != null && currentTime.trim().length() > 0) {
			byte[] bytes = (currentTime).getBytes();
			ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
			writeBuffer.put(bytes);
			writeBuffer.flip();
			channel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
				@Override
				public void completed(Integer result, ByteBuffer buffer) {
					// 如果没有发送完成，继续发送
					if (buffer.hasRemaining())
						channel.write(buffer, buffer, this);
				}

				@Override
				public void failed(Throwable exc, ByteBuffer attachment) {
					try {
						channel.close();
					} catch (IOException e) {
						// ingnore on close
					}
				}
			});
		}
	}

	@Override
	public void failed(Throwable exc, ByteBuffer attachment) {
		try {
			this.channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
