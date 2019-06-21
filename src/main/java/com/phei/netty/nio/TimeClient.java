package com.phei.netty.nio;

/**
 * ClassName: TimeClient <br/>
 * Function: 【2.3】NIO 创建客户端 TimeClient. <br/>
 * date: 2019年6月21日 下午3:36:55 <br/>
 *
 * @version 
 * @since JDK 1.8
 * @author kaiyun
 */
public class TimeClient {

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				// 采用默认值
			}
		}
        new Thread(new TimeClientHandle("127.0.0.1", port), "TimeClient-001").start();
    }
}
