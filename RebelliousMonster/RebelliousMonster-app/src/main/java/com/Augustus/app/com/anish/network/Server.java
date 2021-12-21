package com.Augustus.app.com.anish.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;



public class Server {
    Selector selector;
    InetSocketAddress listenAddress;
    final static int PORT = 9093;

    // public static void main(String[] args) throws IOException{
    //     new Server("localhost").startServer();
    // }

    public Server(String addr){
        listenAddress = new InetSocketAddress(addr, PORT);
    }

    public void startServer() throws IOException{
        this.selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        serverChannel.socket().bind(listenAddress);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("Server started on: "+PORT);

        while(true){
            int readyCnt = selector.select();
            if(readyCnt==0)
                continue;
            
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator iter =readyKeys.iterator();
            while(iter.hasNext()){
                SelectionKey key = (SelectionKey)iter.next();
                iter.remove();;

                if(!key.isValid())
                    continue;
                
                    if(key.isAcceptable()){
                        this.accept(key);
                    }
                    else if(key.isReadable()){
                        this.read(key);
                    }
                    else if(key.isWritable()){
                        //write to client
                    }
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverch = (ServerSocketChannel) key.channel();
        SocketChannel ch = serverch.accept();
        ch.configureBlocking(false);

        //info not necessary
        Socket socket = ch.socket();
        SocketAddress remoteAddr = socket.getRemoteSocketAddress();
        System.out.println("Conneted to: "+remoteAddr);

        ch.register(selector, SelectionKey.OP_READ);
    }

    void read(SelectionKey key) throws IOException{
        SocketChannel ch = (SocketChannel)key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int numRead=-1;
        numRead = ch.read(buffer);

        if(numRead==-1){
            //not necessary just get info
            Socket socket = ch.socket();
            SocketAddress remoteAddr = socket.getRemoteSocketAddress();
            System.out.println("connection closed by client: "+remoteAddr);
            //end up
            ch.close();
            key.cancel();
            return;
        }

        byte[] data = new byte[numRead];
        System.arraycopy(buffer.array(),0,data,0,numRead);
        //copy bit data
        System.out.println("Got: "+new String(data));
        //output as string
    }
}
