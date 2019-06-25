/*
 * Copyright 2013-2018 Lilinfeng.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phei.netty.pio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.phei.netty.bio.TimeServerHandler;

/**
 * ClassName: TimeServer <br/>
 * Function: 【2.2】 伪异步 I/O 的 TimeServer. <br/>
 * date: 2019年6月20日 下午5:29:00 <br/>
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
        int port = 8090;
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("The time server is start in port : " + port);
            Socket socket;
            TimeServerHandlerExecutePool singleExecutor = new TimeServerHandlerExecutePool(
                    50, 10000);// 创建IO任务线程池
            while (true) {
                socket = server.accept();
                singleExecutor.execute(new TimeServerHandler(socket));
            }
        } finally {
            if (server != null) {
                System.out.println("The time server close");
                server.close();
            }
        }
    }
}
