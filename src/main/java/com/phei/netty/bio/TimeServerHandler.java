package com.phei.netty.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * ClassName: TimeServerHandler <br/>
 * Function: 【2.1】 同步阻塞 I/O 的 TimeServerHandler. <br/>
 * date: 2019年6月20日 下午4:45:24 <br/>
 *
 * @version 
 * @since JDK 1.8
 * @author kaiyun
 */
public class TimeServerHandler implements Runnable {

	private Socket socket;

	public TimeServerHandler(Socket socket) {
		this.socket = socket;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			out = new PrintWriter(this.socket.getOutputStream(), true);
			String currentTime = null;
			String body = null;
			while (true) {
				body = in.readLine();
				if (body == null)
					break;
				System.out.println("The time server receive order : " + body);
				currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)
						? new java.util.Date(System.currentTimeMillis()).toString()
						: "BAD ORDER";
				out.println(currentTime);
			}

		} catch (Exception e) {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (out != null) {
				out.close();
				out = null;
			}
			if (this.socket != null) {
				try {
					this.socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				this.socket = null;
			}
		}
	}
}
