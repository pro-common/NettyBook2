package com.phei.netty.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ClassName: TimeServer <br/>
 * Function: 【2.1】 同步阻塞式 I/O 创建的 TimeServer. <br/>
 * Desc:  
 * date: 2019年6月20日 下午4:39:07 <br/>
 *
 * @version 
 * @since JDK 1.8
 * @author kaiyun
 */
public class TimeServer {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// 根据传入的参数设置监听端口，如果没有入参，使用默认值 8080
		int port = 8080;
		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				// 采用默认值
			}
		}
		ServerSocket server = null;
		try {
			server = new ServerSocket(port);
			System.out.println("The time server is start in port : " + port);
			Socket socket = null;
			while (true) {// 通过一个无限循环来监听客户端的连接，若没有客户端接入，则主线程阻塞在 ServerSocket 的 accept 操作上
				socket = server.accept();
				// 一个客户端需要一个线程处理（缺点：资源浪费容易导致堆栈溢出问题）
				new Thread(new TimeServerHandler(socket)).start();
			}
		} finally {
			if (server != null) {
				System.out.println("The time server close");
				server.close();
				server = null;
			}
		}
	}
}
