package com.Augustus.app.com.anish.network;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import com.Augustus.app.com.anish.screen.Screen;

public class SubReactor implements Runnable {
    private final SocketChannel socket;
    private final Selector selector;
    private final BlockingQueue<Integer> keyMessage;
    Screen screen;
    public SubReactor(SocketChannel socket, Selector sel,BlockingQueue<Integer> keyMessage,Screen screen) throws IOException {
        this.socket = socket;
        this.selector = sel;
        this.keyMessage = keyMessage;
        socket.configureBlocking(false);
        this.screen = screen;
        // register read io event and interested.
        socket.register(sel, SelectionKey.OP_READ, new Handler(this.socket, sel,keyMessage,screen));
        // wakeup all threads which interesting this io event.
        // sel.wakeup();
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                selector.select();
                // Multiplexing IO events set.
                Set<SelectionKey> selected = selector.selectedKeys();
                for (SelectionKey key : selected) {
                    Runnable handler = (Runnable) key.attachment();
                    if (handler != null) {
                        //System.out.println("hander");
                        handler.run();
                    }
                }
                selected.clear();
            }
        } catch (IOException ignored) {

        }
    }
}