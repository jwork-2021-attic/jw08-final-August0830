package com.Augustus.app.com.anish.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import com.Augustus.app.com.anish.calabashbros.World;
import com.Augustus.app.com.anish.screen.Screen;

public class Handler implements Runnable {
    private static final int MAX_IN = 1024;
    private static final int MAX_OUT = 4096;

    private static final int cores = Runtime.getRuntime().availableProcessors();

    private final SocketChannel socket;
    private final SelectionKey sk;
    private ByteBuffer input = ByteBuffer.allocateDirect(MAX_IN);
    private ByteBuffer output = ByteBuffer.allocateDirect(MAX_OUT);
    protected final BlockingQueue<Integer> keyMessage;
    Screen screen;

    private static ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(cores);

    public Handler(SocketChannel socket, Selector sel, BlockingQueue<Integer> keyMessage, Screen screen)
            throws IOException {
        this.socket = socket;
        this.keyMessage = keyMessage;
        this.screen = screen;
        socket.configureBlocking(false);
        sk = socket.register(sel, SelectionKey.OP_READ, this);
        sel.wakeup();
    }

    // void reset(){
    // sk.attach();
    // sk.interestOps(SelectionKey.OP_WRITE);
    // // wakeup all threads which interesting this io event.
    // sk.selector().wakeup();
    // }

    // private void process() {
    // input.flip();
    // System.out.println(Thread.currentThread().getName() + "------process------");
    // byte[] bytes = new byte[input.limit()];
    // input.get(bytes);
    // input.clear();
    // String str = new String(bytes);
    // System.out.println(str);
    // output.put(("received---" + str).getBytes());
    // output.flip();

    // }

    private void sendScreen(ByteBuffer buffer) throws IOException {
        System.out.println("Server send screen");
        for (int x = 0; x < World.WIDTH; ++x) {
            for (int y = 0; y < World.HEIGHT; ++y) {
                int[] info = screen.displayInfo(x, y);
                for (int i : info) {
                    buffer.putInt(i);
                    System.out.println(i);
                }
            }
        }
        buffer.flip();
    }

    @Override
    public void run() {
        try {
            read();
        } catch (IOException ignored) {
        }
    }

    synchronized void read() throws IOException {
        socket.read(input);
        pool.execute(new Processor());

    }

    synchronized void processAndHandOff() {
        // process();
        // register write io event and interested.
        input.flip();
        //System.out.println(Thread.currentThread().getName() + "------process------");
        byte[] bytes = new byte[input.limit()];
        input.get(bytes);
        input.clear();
        String str = new String(bytes);
        if (str.charAt(0) == '0') {
            sk.attach(new Sender());
            sk.interestOps(SelectionKey.OP_WRITE);
            // wakeup all threads which interesting this io event.
            sk.selector().wakeup();

        } else if (str.charAt(0) == '1') {
            Scanner sc = new Scanner(str.substring(0));
            while (sc.hasNextInt()) {
                int keyCode = sc.nextInt();
                try {
                    keyMessage.put(keyCode);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    private class Processor implements Runnable {
        @Override
        public void run() {
            processAndHandOff();
        }
    }

    private class Sender implements Runnable {
        @Override
        public void run() {
            try {
                // Handler.this.sendScreen(output);

                //System.out.println("Server send screen");
                for (int x = 0; x < World.WIDTH; ++x) {
                    for (int y = 0; y < World.HEIGHT; ++y) {
                        int[] info = screen.displayInfo(x, y);
                        for (int i : info) {
                            output.putInt(i);
                            // System.out.println(i);
                        }
                        output.flip();
                        socket.write(output);
                        output.flip();
                    }
                }
                // output.flip();
                // socket.write(output);
                output.clear();
                sk.interestOps(SelectionKey.OP_READ);
                sk.attach(Handler.this);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // sk.cancel();

        }
    }
}