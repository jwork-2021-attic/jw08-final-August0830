package com.Augustus.app;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import com.Augustus.app.asciiPanel.AsciiFont;
import com.Augustus.app.asciiPanel.AsciiPanel;
import com.Augustus.app.com.anish.calabashbros.World;
import com.Augustus.app.com.anish.screen.Screen;
import com.Augustus.app.com.anish.screen.WorldScreen;

public class Main extends JFrame implements KeyListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private AsciiPanel terminal;
    private Screen screen;

    public Main() throws InterruptedException {
        super();
        terminal = new AsciiPanel(World.WIDTH, World.HEIGHT+10, AsciiFont.TALRYTH_15_15);
        //屏幕设置 ui可以在这里更改 以及屏幕大小
        add(terminal);
        pack();
        screen = new WorldScreen();
        addKeyListener(this);
        
 
    }

    @Override
    public void repaint() {
        terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        screen = screen.respondToUserInput(e);
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public static void main(String[] args) throws InterruptedException {
        Main app = new Main();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
        while(true){
            app.repaint();
            //System.out.println("paint");
            Thread.sleep(500);
        }
    }

}
