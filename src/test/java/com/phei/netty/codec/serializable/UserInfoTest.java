package com.phei.netty.codec.serializable;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

/**
 * ClassName: UserInfoTest <br/>
 * Function: 【6.1.2】使用基于 ByteBuffer 的通用二进制编解码技术与传统JDK序列化后的码流大小进行对比. <br/>
 * date: 2019年6月25日 下午8:13:05 <br/>
 *
 * @version 
 * @since JDK 1.8
 * @author kaiyun
 */
public class UserInfoTest {
    @Test
    public void testBuildUserName() throws Exception {
        UserInfo info = new UserInfo();
        // 序列化
        info.buildUserID(100).buildUserName("Welcome to Netty");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(info);
        os.flush();
        os.close();

        // 反序列化
        byte[] b = bos.toByteArray();
        System.out.println("The jdk serializable length is : " + b.length);
        bos.close();
        System.out.println("-------------------------------------");
        System.out.println("The byte array serializable length is : " + info.codeC().length);
    }

    // 测试性能 将Java序列化和二进制编码对比
    @Test
    public void testPerformance() throws IOException {
        UserInfo info = new UserInfo();
        info.buildUserID(100).buildUserName("Welcome to Netty");
        int loop = 1000000;
        ByteArrayOutputStream bos;
        ObjectOutputStream os;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            bos = new ByteArrayOutputStream();
            os = new ObjectOutputStream(bos);
            os.writeObject(info);
            os.flush();
            os.close();
            byte[] b = bos.toByteArray();
            bos.close();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("The jdk serializable cost time is  : "
                + (endTime - startTime) + " ms");

        System.out.println("-------------------------------------");

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        startTime = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            byte[] b = info.codeC(buffer);
        }
        endTime = System.currentTimeMillis();
        System.out.println("The byte array serializable cost time is : "
                + (endTime - startTime) + " ms");

    }
}