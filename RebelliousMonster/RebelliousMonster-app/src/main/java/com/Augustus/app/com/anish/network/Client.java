package com.Augustus.app.com.anish.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.xml.crypto.Data;

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

        for(int i=0;i<messages.length;++i){
            ByteBuffer buffer = ByteBuffer.allocate(74);
            int numRead=-1;
            numRead = client.read(buffer);
            
            if(numRead==-1){
                //not necessary just get info
                Socket socket = client.socket();
                SocketAddress remoteAddr = socket.getRemoteSocketAddress();
                System.out.println("connection closed by client: "+remoteAddr);
                //end up
                client.close();
            }
    
            byte[] data = new byte[numRead];
            System.arraycopy(buffer.array(),0,data,0,numRead);
            //copy bit data
            System.out.println("Got: "+new String(data));
            //output as string
        }
        client.close();
    }

    public static void main(String[] args){
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
        Thread a =new Thread(client,"clientA");
        a.start();
        //new Thread(client,"clientB").start();
    }
}
