package com.Augustus.app.com.anish.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {
    public void startClient() throws IOException, InterruptedException{
        InetSocketAddress hostAddr = new InetSocketAddress("localhost", 9093);
        SocketChannel client = SocketChannel.open(hostAddr);

        System.out.println("Client... started");
        String threadName = Thread.currentThread().getName();
        String[] messages = new String[] { threadName + ": msg1", threadName + ": msg2", threadName + ": msg3" };
    
        for(int i=0;i<messages.length;++i){
            ByteBuffer buffer = ByteBuffer.allocate(74);
            buffer.put(messages[i].getBytes());//turn to bit
            buffer.flip();
            client.write(buffer);
            System.out.println(messages[i]);
            buffer.clear();
            Thread.sleep(500);
        }
        client.close();
    }

    public void clientMain(){
        Runnable client = new Runnable(){
            @Override 
            public void run(){
                try {
                    new Client().startClient();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        //name the thread
        new Thread(client,"clientA").start();
        new Thread(client,"clientB").start();
    }
}
