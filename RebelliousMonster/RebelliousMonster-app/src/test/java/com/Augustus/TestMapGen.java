package com.Augustus;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import com.Augustus.app.com.anish.calabashbros.Stone;
import com.Augustus.app.com.anish.calabashbros.World;
import com.Augustus.app.com.anish.screen.MapGenerator;
import com.Augustus.app.com.anish.screen.WorldScreen;

import org.junit.Test;

public class TestMapGen {

    @Test
    public void testSave() throws IOException {
        int height=20;
        int width =40;
        World world = new World();
        MapGenerator mapgen = new MapGenerator(40, 20);
        int[][] data = mapgen
        .getData(WorldScreen.class.getClassLoader().getResource("com/Augustus/app/resources/NJUCS.bmp"));

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (data[j][i] == -1) {// 有颜色输出
                    // System.out.print("*");
                    world.put(new Stone(world, j, i), j, i);
                    data[j][i]=1;
                } else { // 无颜色输出
                    data[j][i]=0;
                }
            }
        }

        mapgen.saveMap(world);
    }

    @Test
    public void testReset() throws IOException{
        int height=20;
        int width =40;
        World world = new World();
        MapGenerator mapgen = new MapGenerator(40, 20);
        int[][] data = mapgen
        .getData(WorldScreen.class.getClassLoader().getResource("com/Augustus/app/resources/NJUCS.bmp"));

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (data[j][i] == -1) {// 有颜色输出
                    // System.out.print("*");
                    world.put(new Stone(world, j, i), j, i);
                    data[j][i]=1;
                } else { // 无颜色输出
                    data[j][i]=0;
                }
            }
        }

        mapgen.saveMap(world);
        World setWorld = new World();
        mapgen.resetMap(setWorld);
        int[][] setdata = mapgen.getData();
        assertTrue(data.equals(setdata));
        mapgen.showData();
        assertTrue(world.equals(setWorld));
    }
}