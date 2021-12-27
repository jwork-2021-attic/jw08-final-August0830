package com.Augustus.app.com.anish.screen;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import javax.imageio.ImageIO;

import com.Augustus.app.com.anish.calabashbros.Floor;
import com.Augustus.app.com.anish.calabashbros.Stone;
import com.Augustus.app.com.anish.calabashbros.World;

public class MapGenerator {
    int Width;
    int Height;
    int[][] data;

    public MapGenerator(int Width, int Height) {
        this.Width = Width;
        this.Height = Height;
        data =new int[Width][Height];
    }

    public int[][] getData(){
        return data;
    }
    
    public void showData(){
        for (int i = 0; i < Height; ++i) {
            for (int j = 0; j < Width; ++j) {
                System.out.print(data[j][i] + " ");
            }
            System.out.println();
        }
    }

    public void iniMap(World world,InputStream input){
        data = getData(input);
        for (int i = 0; i < Height; i++) {
            for (int j = 0; j < Width; j++) {
                if (data[j][i] == -1) {// 有颜色输出
                    // System.out.print("*");
                    world.put(new Stone(world, j, i), j, i);
                    data[j][i] = 1;
                } else { // 无颜色输出
                    data[j][i] = 0;
                }
            }
        }
    }

    public int[][] getData(InputStream input) {
        // 创建一个二维数组，用来存放图像每一个像素位置的颜色值

        
        try {
            // 获取图像资源，转成BufferedImage对象
            
            java.awt.image.BufferedImage bimg = ImageIO.read(input);

            // 以高度为范围，逐列扫描图像，存进数组对应位置
            for (int i = 0; i < bimg.getWidth(); i++) {
                for (int j = 0; j < bimg.getHeight(); j++) {
                    data[3 + i][2 + j] = bimg.getRGB(i, j);// 得到的是sRGB值，4字节
                }
            }
            for (int i = 0; i < Width; ++i) {
                data[i][0] = -1;
                data[i][Height - 1] = -1;
            }
            for (int i = 0; i < Height; ++i) {
                data[0][i] = -1;
                data[Width - 1][i] = -1;
            }
            // System.out.println("Width: "+bimg.getWidth()+"Height: "+bimg.getHeight());
            // 将数组旋转90°输出，实现逐行输出图像

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public synchronized void  saveMap(World world) throws IOException {
        for (int i = 0; i < Height; i++) {
            for (int j = 0; j < Width; j++) {
                if (world.get(j, i) instanceof Stone) {// 有颜色输出
                    // System.out.print("*");
                    data[j][i] = 1;
                } else { // 无颜色输出
                    data[j][i] = 0;
                }
            }
        }

        File mapfile = new File("./mapInfo.txt");
        PrintWriter output = new PrintWriter(new FileWriter(mapfile));
        for (int i = 0; i < Height; ++i) {
            for (int j = 0; j < Width; ++j) {
                output.print(data[j][i] + " ");
            }
            output.println();
        }
        output.close();
        System.out.println("Finish saving");
    }

    public void resetMap(World world) throws IOException {
        File mapfile = new File("./mapInfo.txt");
        BufferedReader input = new BufferedReader(new FileReader(mapfile));
        String line;
        int i=0;
        int j=0;
        while((line=input.readLine())!=null){
            i=0;
            Scanner sc = new Scanner(line);
            //System.out.println(line);
            sc.useDelimiter(" ");
            while(sc.hasNext()){
                int val = sc.nextInt();
                data[i][j]=val;
                if(val==1)
                    world.put(new Stone(world,i,j),i,j);
                else
                    world.put(new Floor(world),i,j);
                ++i;
                //System.out.println(j+" "+i+" "+val);
            }
            j++;
            sc.close();
        }
        input.close();
    }
}