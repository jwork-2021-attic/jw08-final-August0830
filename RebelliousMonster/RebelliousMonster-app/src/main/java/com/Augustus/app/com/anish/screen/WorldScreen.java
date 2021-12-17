package com.Augustus.app.com.anish.screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.Augustus.app.asciiPanel.AsciiPanel;
import com.Augustus.app.com.anish.calabashbros.Floor;
import com.Augustus.app.com.anish.calabashbros.Goblin;
import com.Augustus.app.com.anish.calabashbros.Monster;
import com.Augustus.app.com.anish.calabashbros.Signal;
import com.Augustus.app.com.anish.calabashbros.Stone;
import com.Augustus.app.com.anish.calabashbros.World;

public class WorldScreen implements Screen {

    private World world;
    BlockingQueue<KeyEvent> keyMessage;
    public static final int GOBCNT = 10;
    String[] sortSteps;
    ArrayList<Goblin> gobThreads;
    Monster localMonster;
    Signal sig;
    MapGenerator mapgen;

    // ArrayList<Thread>
    public WorldScreen() {
        world = new World();
        int width = 40;
        int height = 20;
        mapgen = new MapGenerator(width, height);
        System.out.println("load previous map?");
        int load=0;
        try {
            load = System.in.read();
            System.out.print(load);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(load==49){
            try {
                mapgen.resetMap(world);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else{
            System.out.println(WorldScreen.class.getClassLoader().getResource("com/Augustus/app/resources"));
        // int[][] data = mapgen
        //         .getData(WorldScreen.class.getClassLoader().getResource("com/Augustus/app/resources/NJUCS.bmp"));
            mapgen.iniMap(world, 
            WorldScreen.class.getClassLoader().getResource("com/Augustus/app/resources/NJUCS.bmp"));
        }

        sig = new Signal();
        gobThreads = new ArrayList<Goblin>();
        keyMessage = new LinkedBlockingQueue<KeyEvent>();
        Random r = new Random();
        for (int i = 0; i < GOBCNT; ++i) {
            int red = (r.nextInt(255) + 255) / 2;
            int blue = (r.nextInt(255) + 255) / 2;
            int green = (r.nextInt(255) + 255) / 2;
            Goblin gob = new Goblin(new Color(red, green, blue), world, 50);
            gob.setStopSig(sig);
            // hero = new Calabash(new Color(red,green,blue),1,world);
            // world.put(hero,0,startList.get(r.nextInt(startList.size())));
            gobThreads.add(gob);
            new Thread(gob).start();
        }

        localMonster = new Monster(world, 50);
        localMonster.setStopSig(sig);
        localMonster.setReceiver(keyMessage);
        new Thread(localMonster).start();

    }

    @Override
    public void displayOutput(AsciiPanel terminal) {

        for (int x = 0; x < World.WIDTH; x++) {
            for (int y = 0; y < World.HEIGHT; y++) {

                terminal.write(world.get(x, y).getGlyph(), x, y, world.get(x, y).getColor());

            }
        }
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        try {
            if (key.getKeyCode() == KeyEvent.VK_SPACE) {
                if (sig.getStopBit() == false) {
                    sig.setStopBit(true);
                    try {
                        mapgen.saveMap(world);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    sig.setStopBit(false);
                    try {
                        mapgen.resetMap(world);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    //System.out.println((world.get(localMonster.getX(),localMonster.getY())instanceof Floor));
                    for(Goblin t:gobThreads)
                        new Thread(t).start();
                    new Thread(localMonster).start();
                }
            }

            keyMessage.put(key);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return this;
    }

}
