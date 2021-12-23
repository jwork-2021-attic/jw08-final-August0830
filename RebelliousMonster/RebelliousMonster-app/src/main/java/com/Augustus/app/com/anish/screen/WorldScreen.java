package com.Augustus.app.com.anish.screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
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
    BlockingQueue<Integer> keyMessage;
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

        sig = new Signal();
        sig.setMonCnt(1);
        sig.setGobCnt(GOBCNT);

        gobThreads = new ArrayList<Goblin>();
        keyMessage = new LinkedBlockingQueue<Integer>();

        mapgen = new MapGenerator(width, height);
        System.out.println(WorldScreen.class.getClassLoader().getResource("com/Augustus/app/resources"));
        // int[][] data = mapgen
        // .getData(WorldScreen.class.getClassLoader().getResource("com/Augustus/app/resources/NJUCS.bmp"));
        mapgen.iniMap(world,
                WorldScreen.class.getClassLoader().getResource("com/Augustus/app/resources/NJUCS.bmp"));
        Random r = new Random();
        for (int i = 0; i < GOBCNT; ++i) {
            int red = (r.nextInt(255) + 255) / 2;
            int blue = (r.nextInt(255) + 255) / 2;
            int green = (r.nextInt(255) + 255) / 2;
            Goblin gob = new Goblin(new Color(red, green, blue), world, 50);
            // hero = new Calabash(new Color(red,green,blue),1,world);
            // world.put(hero,0,startList.get(r.nextInt(startList.size())));
            gobThreads.add(gob);
        }
        localMonster = new Monster(world, 50);
        for (Goblin gob : gobThreads) {
            gob.setStopSig(sig);
            new Thread(gob).start();
        }
        
        localMonster.setStopSig(sig);
        localMonster.setReceiver(keyMessage);
        new Thread(localMonster).start();

    }

    @Override
    public void displayOutput(AsciiPanel terminal) {

        for (int x = 0; x < World.WIDTH; x++) {
            for (int y = 0; y < World.HEIGHT; y++) {

                terminal.write(world.get(x, y).getGlyph(), x, y, world.get(x, y).getColor());
                //System.out.println(world.get(x, y).getGlyph()+" "+x+" "+y+" "+world.get(x, y).getColor());
            }
        }
        if (sig.getGameEnd())
            System.out.println("display:Game Over");
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        try {
            if (key.getKeyCode() == KeyEvent.VK_SPACE) {
                if (sig.getStopBit() == false) {
                    sig.setStopBit(true);
                    try {
                        mapgen.saveMap(world);
                        saveCreature();
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
                    // System.out.println((world.get(localMonster.getX(),localMonster.getY())instanceof
                    // Floor));
                    for (Goblin t : gobThreads)
                        new Thread(t).start();
                    new Thread(localMonster).start();
                }
            }
            else if(key.getKeyCode()==KeyEvent.VK_L){
                try {
                   
                    sig.setStopBit(true);
                    mapgen.resetMap(world);
                    resetCreature();
                    //no need to start again  press space to continue
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
            else
                keyMessage.put(key.getKeyCode());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return this;
    }

    private synchronized void saveCreature() throws IOException {
        File monFile = new File("./monster.txt");
        PrintWriter output = new PrintWriter(new FileWriter(monFile));
        output.println(localMonster.getHp() + " " + localMonster.getX() + " " + localMonster.getY());
        output.close();

        File gobFile = new File("./goblin.txt");
        output = new PrintWriter(new FileWriter(gobFile));
        for (Goblin g : gobThreads) {
            output.println(g.getHp() + " " + g.getX() + " " + g.getY());
        }
        output.close();
    }

    void resetCreature() throws IOException {
        File monFile = new File("./monster.txt");
        BufferedReader input = new BufferedReader(new FileReader(monFile));
        String line;
        while ((line = input.readLine()) != null) {
            Scanner sc = new Scanner(line);
            sc.useDelimiter(" ");
            int[] info = new int[3];
            int index = 0;
            while (sc.hasNext()) {
                info[index++] = sc.nextInt();
            }
            localMonster = new Monster(world, info[0], info[1], info[2]);
            localMonster.setStopSig(sig);
            localMonster.setReceiver(keyMessage);
            sc.close();
        }
        input.close();

        //System.out.println("gobthreadSize: "+gobThreads.size());
        
        while(gobThreads.size()>0){
            Goblin gb = gobThreads.get(0);
            
            world.put(new Floor(world),gb.getX(),gb.getY());
            gobThreads.remove(0);
            //System.out.println("gobthreadSize: "+gobThreads.size());
        }
        //System.out.println("gobthreadSize: "+gobThreads.size());
        File gobFile = new File("./goblin.txt");
        input = new BufferedReader(new FileReader(gobFile));
        Random r = new Random();
        while ((line = input.readLine()) != null) {
            Scanner sc = new Scanner(line);
            sc.useDelimiter(" ");
            int[] info = new int[3];
            int index = 0;
            while (sc.hasNext()) {
                info[index++] = sc.nextInt();
            }
            int red = (r.nextInt(255) + 255) / 2;
            int blue = (r.nextInt(255) + 255) / 2;
            int green = (r.nextInt(255) + 255) / 2;
            Goblin gob = new Goblin(new Color(red, green, blue), world, info[0], info[1], info[2]);
            gob.setStopSig(sig);
            gobThreads.add(gob);
            sc.close();
        }
        input.close();
    }

    @Override
    // public String displayInfo(int x, int y) {
    //     // TODO Auto-generated method stub
    //     Color color = world.get(x, y).getColor();
    //     int glyphNum = 250;
    //     if(world.get(x, y) instanceof Stone)
    //         glyphNum=177;
    //     else if(world.get(x,y) instanceof Monster)
    //         glyphNum=2;
    //     else if(world.get(x,y) instanceof Goblin)
    //         glyphNum=1;
    //     String info = new String(glyphNum+" "+x+" "+y+" "+color.getRed()+" "+color.getGreen()+" "+color.getBlue());
    //     return info;
    // }

    public int[] displayInfo(int x,int y){
        int[] info = new int[6];
        Color color = world.get(x, y).getColor();
        int glyphNum = 250;
        if(world.get(x, y) instanceof Stone)
            glyphNum=177;
        else if(world.get(x,y) instanceof Monster)
            glyphNum=2;
        else if(world.get(x,y) instanceof Goblin)
            glyphNum=1;
        info[0]=glyphNum;
        info[1]=x;
        info[2]=y;
        info[3]=color.getRed();
        info[4]=color.getGreen();
        info[5]=color.getBlue();
        return info;
    }
}
