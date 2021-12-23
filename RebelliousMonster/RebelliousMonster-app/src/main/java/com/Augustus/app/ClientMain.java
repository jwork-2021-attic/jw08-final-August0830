package com.Augustus.app;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.awt.Color;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

import javax.crypto.KeyAgreement;
import javax.swing.JFrame;

import com.Augustus.app.asciiPanel.AsciiFont;
import com.Augustus.app.asciiPanel.AsciiPanel;
import com.Augustus.app.com.anish.calabashbros.World;
import com.Augustus.app.com.anish.network.Client;
import com.Augustus.app.com.anish.network.Server;
import com.Augustus.app.com.anish.screen.ClientScreen;
import com.Augustus.app.com.anish.screen.Screen;

public class ClientMain extends JFrame implements KeyListener {
    private static final long serialVersionUID = 1L;
    private AsciiPanel terminal;
    private SocketChannel channel;

    public ClientMain(SocketChannel ch) throws InterruptedException {
        super();
        terminal = new AsciiPanel(World.WIDTH, World.HEIGHT + 10, AsciiFont.TALRYTH_15_15);
        // 屏幕设置 ui可以在这里更改 以及屏幕大小
        add(terminal);
        pack();
        addKeyListener(this);
        channel = ch;
    }

    @Override
    public void repaint() {
        terminal.clear();
        String request = new String(0 + " screen");
        ByteBuffer buffer = ByteBuffer.allocate(74);
        buffer.put(request.getBytes());
        buffer.flip();
        try {
            channel.write(buffer);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        buffer.clear();

        for (int i = 0; i < World.HEIGHT * World.WIDTH; ++i) {
            buffer = ByteBuffer.allocate(24);
            int numRead = -1;
            try {
                numRead = channel.read(buffer);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (numRead == -1)
                System.out.println("Getting response failed");
            else {
                // int[] data = new int[6];
                // System.arraycopy(buffer.array(), 0, data, 0, numRead);
                buffer.flip();
                char glyph = (char) buffer.getInt();
                int x = buffer.getInt();
                int y = buffer.getInt();
                int red = buffer.getInt();
                int green = buffer.getInt();
                int blue = buffer.getInt();
                Color c = new Color(red, green, blue);
                // System.out.println(numRead+" "+glyph+" "+x+" "+y+" "+red+" "+green+" "+blue);
                terminal.write(glyph, x, y, c);
            }
        }
        super.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // send to server

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public static void main(String[] args) throws InterruptedException, IOException {
        InetSocketAddress hostAddr = new InetSocketAddress("localhost", 9093);
        SocketChannel channel = SocketChannel.open(hostAddr);

        System.out.println("Client... started");

        ClientMain app = new ClientMain(channel);

        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
        app.repaint();
        while (true) {
            app.repaint();
            // System.out.println("paint");
            Thread.sleep(500);
        }

    }
}