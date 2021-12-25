package com.Augustus.app.com.anish.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.Augustus.app.Main;
import com.Augustus.app.com.anish.calabashbros.Monster;
import com.Augustus.app.com.anish.screen.Screen;

public class MainReactor implements Runnable{
    private final Selector selector;
    private final ServerSocketChannel serverSocket;
    
    private static final int cores = Runtime.getRuntime().availableProcessors();

    private static ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(cores);

    private final Selector[] selectors;
    private int next = 0;
    ArrayList<Monster> clientMonster;
    Screen screen;
    Main app;
    // public static void main(String[] args) throws IOException {
    //     new MainReactor(9093,).run();
    // check before call mainReactor
    // }
    
    public MainReactor(String addr ,int port) throws IOException {
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(addr,port));
        serverSocket.configureBlocking(false);
        // Multiplexing IO will wakeup this, class acceptor will run.
        serverSocket.register(selector, SelectionKey.OP_ACCEPT, new Acceptor());

        selectors = new Selector[cores];
        for (int i = 0; i < cores; i++) {
            selectors[i] = Selector.open();
        }
    }

    public boolean getScreen(Main app){
        screen = app.getScreenInfo();
        this.app = app;
        return screen != null;
    }

    public boolean getClientMonster(Main app){
        clientMonster = app.getClientMonster();
        return clientMonster!=null;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                selector.select();
                // Multiplexing IO events set.
                Set<SelectionKey> selected = selector.selectedKeys();
                for (SelectionKey o : selected) {
                    dispatch(o);
                }
                
                selected.clear();

            }
        } catch (IOException ignored){
        }
    }

    void dispatch(SelectionKey k) {
        Runnable r = (Runnable) k.attachment();
        if (r != null) {
            r.run();
        }
    }

    private class Acceptor implements Runnable {
        @Override
        public synchronized void run() {
            try {
                //System.out.println("MainReactor dispatch");
                BlockingQueue<Integer> keyMessage = new LinkedBlockingQueue<Integer>();
                for(int i=0;i<clientMonster.size();++i){
                    if(clientMonster.get(i).getReceiver()==null){
                        clientMonster.get(i).setReceiver(keyMessage);
                        new Thread(clientMonster.get(i)).start();
                        break;
                    }
                }
                SocketChannel c = serverSocket.accept();
                if (c != null) {
                    SubReactor subReactor = new SubReactor(c, selectors[next],keyMessage,screen);
                    pool.execute(subReactor);
                }
                if (++next == selectors.length) {
                    next = 0;
                }
            } catch (IOException ignored) {

            }
        }
    }
}