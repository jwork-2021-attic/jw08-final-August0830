package com.Augustus.app.com.anish.calabashbros;

import java.awt.Color;
import java.util.Random;

public class Goblin extends Creature implements Runnable {

    int curX;
    int curY;

    public Goblin(final Color color, final char glyph, final World world) {
        // Random r = new Random();
        // int red = (r.nextInt(255)+255)/2;
        // int blue = (r.nextInt(255)+255)/2;
        // int green = (r.nextInt(255)+255)/2;
        // Color color = new Color(red,green,blue);
        super(color, (char) 1, world, 50);
        // TODO Auto-generated constructor stub
        final Random r = new Random();
        try {
            do {
                curX = r.nextInt() % world.getWidth();
                curY = r.nextInt() % world.getHeight();
            } while (!Class.forName("Floor").isInstance(world.get(curX, curY)));
            this.world.put(this, curX, curY);
        } catch (final ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public synchronized void moveTo(final int xPos, final int yPos) {
        try {
            if (Class.forName("Floor").isInstance(world.get(xPos, yPos))) {
                this.world.put(this, xPos, yPos);
                this.world.put(new Floor(this.world), curX, curY);
                curX = xPos;
                curY = yPos;
            }
        } catch (final ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // checkout arount and attack
    private synchronized void attack(int attackValue) {
        int[] xMove = { 1, 0, -1, 0 };
        int[] yMove = { 0, 1, 0, -1 };

        try {
            for (int i = 0; i < 4; ++i) {
                int xPos = curX + xMove[i];
                int yPos = curY + yMove[i];
                if (Class.forName("Monster").isInstance(world.get(xPos, yPos))) {
                    ((Creature) world.get(xPos, yPos)).getHurt(attackValue);
                    
                }
            }
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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
                moveTo(nextX, nextY);
                attack(r.nextInt(100));
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

}