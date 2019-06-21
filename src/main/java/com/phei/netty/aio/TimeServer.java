package com.phei.netty.aio;

import java.io.IOException;

/**
 * ClassName: TimeServer <br/>
 * Function: 【2.4】AIO 服务端 TimeServer. <br/>
 * date: 2019年6月21日 下午4:08:48 <br/>
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
	int port = 8080;
	if (args != null && args.length > 0) {
	    try {
		port = Integer.valueOf(args[0]);
	    } catch (NumberFormatException e) {
		// 采用默认值
	    }
	}
	// 创建异步的服务器处理类，然后启动线程将 AsyncTimeServerHandler拉起
	AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);
	new Thread(timeServer, "AIO-AsyncTimeServerHandler-001").start();
    }
}
