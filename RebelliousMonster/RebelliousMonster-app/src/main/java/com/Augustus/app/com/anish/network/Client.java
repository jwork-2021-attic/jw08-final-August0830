package com.Augustus.app.com.anish.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.xml.crypto.Data;

public class Client {
    public SocketChannel channel;

    public Client() throws IOException {
        
    }

    // public void startClient() throws IOException, InterruptedException {
        
    //     String threadName = Thread.currentThread().getName();
    //     String[] messages = new String[] { 1 + " " + threadName + ": msg1", 0 + " " + threadName + ": msg2",
    //             1 + " " + threadName + ": msg3" };

    //     for (int i = 0; i < messages.length; ++i) {
    //         ByteBuffer buffer = ByteBuffer.allocate(74);
    //         buffer.put(messages[i].getBytes());// turn to bit
    //         buffer.flip();
    //         client.write(buffer);
    //         System.out.println(messages[i]);
    //         buffer.clear();
    //         Thread.sleep(500);
    //     }

    //     for (int i = 0; i < messages.length; ++i) {
    //         ByteBuffer buffer = ByteBuffer.allocate(18);
    //         int numRead = -1;
    //         numRead = client.read(buffer);
    //         if (numRead == -1)
    //             System.out.println("Getting response failed");
    //         else {
    //             byte[] data = new byte[numRead];
    //             System.arraycopy(buffer.array(), 0, data, 0, numRead);
    //             String str = new String(data);
    //             System.out.println(numRead+" "+str);
    //         }

    //     }
    //     client.close();
    // }
}
