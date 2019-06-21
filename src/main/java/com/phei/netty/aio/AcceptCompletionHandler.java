package com.phei.netty.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * ClassName: AcceptCompletionHandler <br/>
 * Function: 【2.4】AIO 服务端 操作完成回调类. <br/>
 * Description：通过 AcceptCompletionHandler 实例作为 handler 来接受通知消息
 * date: 2019年6月21日 下午4:19:18 <br/>
 *
 * @version
 * @since JDK 1.8
 * @author kaiyun
 */
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncTimeServerHandler> {

	@Override
	public void completed(AsynchronousSocketChannel result, AsyncTimeServerHandler attachment) {
		// 既然已经接受客户端成功了，为什么还要再次调用accept方法呢？
		// 原因是这样的：调用 AsynchronousServerSocketChannel 的 accept 方法后，如果有新的客户端连接接入，系统将回调我们传入的 CompletionHandler
		// 实例的 completed 方法，表示新的客户端已经接入成功。因为一个 AsynchonousServerSocket Channel 可以接收成千上万个客户端，所以需要继续
		// 调用它的 accept 方法，接受其他客户端连接，最终形成一个循环。每当接受一个客户端连接成功之后，再异步接受新客户端连接。
		attachment.asynchronousServerSocketChannel.accept(attachment, this);
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		// 异步读
		result.read(buffer, buffer, new ReadCompletionHandler(result));
	}

	@Override
	public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
		exc.printStackTrace();
		attachment.latch.countDown();
	}

}
