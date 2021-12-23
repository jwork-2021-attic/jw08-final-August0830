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
import java.util.Scanner;
import java.util.Set;

import com.Augustus.app.Main;
import com.Augustus.app.com.anish.calabashbros.World;
import com.Augustus.app.com.anish.screen.Screen;


public class Server extends Thread {
    Selector selector;
    InetSocketAddress listenAddress;
    final static int PORT = 9093;
    Screen screen;

    // public static void main(String[] args) throws IOException{
    // new Server("localhost").startServer();
    // }

    public Server(String addr) {
        listenAddress = new InetSocketAddress(addr, PORT);
    }

    public void run() {
        try {
            this.selector = Selector.open();
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);

            serverChannel.socket().bind(listenAddress);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("Server started on: " + PORT);

        while (true) {
            int readyCnt=0;
            try {
                readyCnt = selector.select();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (readyCnt == 0)
                continue;

            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator iter = readyKeys.iterator();
            while (iter.hasNext()) {
                SelectionKey key = (SelectionKey) iter.next();
                iter.remove();
                ;

                if (!key.isValid())
                    continue;

                if (key.isAcceptable()) {
                    try {
                        this.accept(key);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else if (key.isReadable()) {
                    try {
                        this.read(key);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else if (key.isWritable()) {
                    // this.write(key);
                }
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverch = (ServerSocketChannel) key.channel();
        SocketChannel ch = serverch.accept();
        ch.configureBlocking(false);

        // info not necessary
        Socket socket = ch.socket();
        SocketAddress remoteAddr = socket.getRemoteSocketAddress();
        System.out.println("Conneted to: " + remoteAddr);

        ch.register(selector, SelectionKey.OP_READ);
    }

    void read(SelectionKey key) throws IOException {
        SocketChannel ch = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int numRead = -1;
        numRead = ch.read(buffer);

        if (numRead == -1) {
            // not necessary just get info
            Socket socket = ch.socket();
            SocketAddress remoteAddr = socket.getRemoteSocketAddress();
            System.out.println("connection closed by client: " + remoteAddr);
            // end up
            ch.close();
            key.cancel();
            return;
        }

        byte[] data = new byte[numRead];
        System.arraycopy(buffer.array(), 0, data, 0, numRead);
        buffer.clear();
        // copy bit data
        String info = new String(data);
        if (info.charAt(0) == '0') {
            sendScreen(key, info.substring(1));
        } else {
            // process keypress of client
        }
        System.out.println("Got: " + info);
        // output as string

    }

    private void sendScreen(SelectionKey key, String str) throws IOException {
        SocketChannel ch = (SocketChannel) key.channel();
        System.out.println("Server respond: " + str);
        for(int x=0;x<World.WIDTH;++x){
            for(int y=0;y<World.HEIGHT;++y){
                ByteBuffer buffer = ByteBuffer.allocate(24);
                int[] info = screen.displayInfo(x, y);
                for(int i:info)
                    buffer.putInt(i);
                buffer.flip();
                ch.write(buffer);
                buffer.clear();
            }
        }
    }

    public void getScreen(Main app) {
        screen = app.getScreenInfo();
    }
}
