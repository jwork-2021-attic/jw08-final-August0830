package com.Augustus.app.com.anish.calabashbros;

import java.awt.Color;
import java.util.Random;

public class Goblin extends Creature implements Runnable {

    int curX;
    int curY;

    public Goblin(Color color, World world,int hp) {
        // Random r = new Random();
        // int red = (r.nextInt(255)+255)/2;
        // int blue = (r.nextInt(255)+255)/2;
        // int green = (r.nextInt(255)+255)/2;
        // Color color = new Color(red,green,blue);
        super(color, (char) 1, world, hp);
        // TODO Auto-generated constructor stub
        Random r = new Random();

        do {
            curX = r.nextInt(world.getWidth());
            curY = r.nextInt(world.getHeight());
        } while (curX<0 || curY<0 ||
        !(world.get(curX, curY) instanceof Floor));
        // curX=5;
        // curY=14;
        //System.out.print("goblin generate");
        this.world.put(this, curX, curY);

    }

    @Override
    public synchronized void moveTo(final int xPos, final int yPos) {

        if ((world.get(xPos, yPos) instanceof Floor)) {
            this.world.put(this, xPos, yPos);
            this.world.put(new Floor(this.world), curX, curY);
            //System.out.println("cur: "+curX+" "+curY+" next "+xPos+" "+yPos);
            curX = xPos;
            curY = yPos;
            
        }

    }

    // checkout arount and attack
    private synchronized void attack(int attackValue) {
        int[] xMove = { 1, 0, -1, 0 };
        int[] yMove = { 0, 1, 0, -1 };

        for (int i = 0; i < 4; ++i) {
            int xPos = curX + xMove[i];
            int yPos = curY + yMove[i];  
            if(world.get(xPos,yPos) instanceof Monster){
                ((Monster)world.get(xPos,yPos)).getHurt(attackValue);
            }
        }//如果只是在前进路上检查就不会攻击从其他方向靠近的monster

    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        int[] xMove = { 1, 0, -1, 0 };
        int[] yMove = { 0, 1, 0, -1 };
        Random r = new Random();
        int dir = r.nextInt(4);
        while (hp >= 0) {
            try {
                int nextX = curX + xMove[dir];
                int nextY = curY + yMove[dir];
                while (!(nextX >= 0 && nextX < world.getWidth() 
                && nextY >= 0 && nextY < world.getHeight())
                || !(world.get(nextX, nextY) instanceof Floor)) {
                    dir = (dir+1)%4;
                    nextX = curX + xMove[dir];
                    nextY = curY + yMove[dir];
                    //System.out.println("dir "+dir+" x "+nextX+" y "+nextY);
                    
                }
                moveTo(nextX, nextY);
                attack(r.nextInt(50));
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        world.put(new Floor(world),curX,curY);
    }

}