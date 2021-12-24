package com.Augustus.app.com.anish.screen;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.Augustus.app.asciiPanel.AsciiPanel;
import com.Augustus.app.com.anish.calabashbros.Monster;

public interface Screen {

    public void displayOutput(AsciiPanel terminal);

    public Screen respondToUserInput(KeyEvent key);

    //public String displayInfo(int x,int y);

    public int[] displayInfo(int x,int y);

    public ArrayList<Monster> getClient();
}
