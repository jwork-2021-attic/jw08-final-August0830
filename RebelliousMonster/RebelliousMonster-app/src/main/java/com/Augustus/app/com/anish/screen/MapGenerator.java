package com.Augustus.app.com.anish.screen;

import java.io.IOException;
import java.net.URL;
import java.io.File;

import javax.imageio.ImageIO;
public class MapGenerator {
    int Width;
    int Height;
    public MapGenerator(int Width,int Height)
    {
        this.Width=Width;
        this.Height=Height;
    }
    public int[][] getData(URL path) {
        //创建一个二维数组，用来存放图像每一个像素位置的颜色值
        
        int[][] data = new int[Width][Height];
        try {
            //获取图像资源，转成BufferedImage对象
            java.awt.image.BufferedImage bimg = ImageIO.read(new File(path.getFile()));
            
            
            //以高度为范围，逐列扫描图像，存进数组对应位置
            for (int i = 0; i < bimg.getWidth(); i++) {
                for (int j = 0; j < bimg.getHeight(); j++) {
                    data[3+i][2+j] = bimg.getRGB(i, j);//得到的是sRGB值，4字节
                }
            }
            for(int i=0;i<Width;++i)
            {
                data[i][0]=-1;
                data[i][Height-1]=-1;
            }    
            for(int i=0;i<Height;++i)
            {
                data[0][i]=-1;
                data[Width-1][i]=-1;
            }
            //System.out.println("Width: "+bimg.getWidth()+"Height: "+bimg.getHeight());
            //将数组旋转90°输出，实现逐行输出图像
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}