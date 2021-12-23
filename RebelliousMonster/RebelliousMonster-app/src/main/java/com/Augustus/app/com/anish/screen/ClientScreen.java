package com.Augustus.app.com.anish.screen;

import java.awt.event.KeyEvent;

import com.Augustus.app.asciiPanel.AsciiPanel;
import com.Augustus.app.com.anish.calabashbros.World;
import com.Augustus.app.com.anish.network.Client;

public class ClientScreen implements Screen {

    World world;
    //BlockingQueue<String> que;
    Client client;
    @Override
    public void displayOutput(AsciiPanel terminal) {
        // TODO Auto-generated method stub

    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int[] displayInfo(int x, int y) {
        // TODO Auto-generated method stub
        return null;
    }

}