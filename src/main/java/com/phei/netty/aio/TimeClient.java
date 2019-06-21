package com.phei.netty.aio;

/**
 * ClassName: TimeClient <br/>
 * Function: 【2.4】AIO 客户端. <br/>
 * date: 2019年6月21日 下午5:12:20 <br/>
 *
 * @version
 * @since JDK 1.8
 * @author kaiyun
 */
public class TimeClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int port = 8080;
		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				// 采用默认值
			}
		}

		// 在实际项目中我们不需要独立的线程创建异步连接对象，因为底层都是通过 JDK 的系统回调实现的
		new Thread(new AsyncTimeClientHandler("127.0.0.1", port), "AIO-AsyncTimeClientHandler-001").start();
	}
}
