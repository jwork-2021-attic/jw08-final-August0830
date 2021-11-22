package com.Augustus.app.com.anish.screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import com.Augustus.app.asciiPanel.AsciiPanel;
import com.Augustus.app.com.anish.calabashbros.Calabash;
import com.Augustus.app.com.anish.calabashbros.Wall;
import com.Augustus.app.com.anish.calabashbros.World;

public class WorldScreen implements Screen {

    private World world;
    private Calabash hero;
    String[] sortSteps;

    public WorldScreen() {
        world = new World();
        int width=40;
        int height=20;
        MapGenerator mapgen = new MapGenerator(width,height);
        System.out.println(WorldScreen.class.getClassLoader().getResource("NJUCS.bmp"));
        int[][] data = mapgen.getData(WorldScreen.class.getClassLoader().getResource("NJUCS.bmp"));
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (data[j][i] == -1) {//有颜色输出
                    //System.out.print("*");
                    Wall w = new Wall(world);
                    world.put(w,j,i);
                } else {            //无颜色输出
                    
                }
            }
        }

            
        // }
        // Random r = new Random();
        // int red = (r.nextInt(255)+255)/2;
        // int blue = (r.nextInt(255)+255)/2;
        // int green = (r.nextInt(255)+255)/2;
        // hero = new Calabash(new Color(red,green,blue),1,world);
        // world.put(hero,0,startList.get(r.nextInt(startList.size())));

    }

    private String[] parsePlan(String plan) {
        return plan.split("\n");
    }

    private void execute(Calabash[] bros, String step) {
        String[] couple = step.split("<->");
        getBroByRank(bros, Integer.parseInt(couple[0])).swap(getBroByRank(bros, Integer.parseInt(couple[1])));
    }

    private Calabash getBroByRank(Calabash[] bros, int rank) {
        for (Calabash bro : bros) {
            if (bro.getRank() == rank) {
                return bro;
            }
        }
        return null;
    }
    @Override
    public void displayOutput(AsciiPanel terminal) {

        for (int x = 0; x < World.WIDTH; x++) {
            for (int y = 0; y < World.HEIGHT; y++) {

                terminal.write(world.get(x, y).getGlyph(), x, y, world.get(x, y).getColor());

            }
        }
    }

    int i = 0;

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        

        return this;
    }

}
