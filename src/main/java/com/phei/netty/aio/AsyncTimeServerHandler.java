package com.phei.netty.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * ClassName: AsyncTimeServerHandler <br/>
 * Function: 【2.4】AIO 异步服务器处理类. <br/>
 * date: 2019年6月21日 下午4:08:55 <br/>
 *
 * @version
 * @since JDK 1.8
 * @author kaiyun
 */
public class AsyncTimeServerHandler implements Runnable {

	private int port;

	CountDownLatch latch;
	AsynchronousServerSocketChannel asynchronousServerSocketChannel;

	public AsyncTimeServerHandler(int port) {
		this.port = port;
		try {
			// 创建一个异步的服务端通道 AsynchronnousServerChannel
			asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
			// 绑定监听端口
			asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
			System.out.println("The time server is start in port : " + port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

		// CountDownLatch 对象作用：在完成一组正在执行的操作之前，允许当前的线程一直阻塞。
		latch = new CountDownLatch(1);
		doAccept();
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void doAccept() {
		asynchronousServerSocketChannel.accept(this, new AcceptCompletionHandler());
	}

}
