package com.Augustus.app.com.anish.calabashbros;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Monster extends Creature implements Runnable {

    private int posX;
    private int posY;
    private Stone stone;
    BlockingQueue<KeyEvent> keyMessage;

    public Monster(World world, int hp) {
        super(Color.WHITE, (char) 2, world, hp);

        // TODO Auto-generated constructor stub
        Random r = new Random();

        do {
            posX = r.nextInt(world.getWidth());
            posY = r.nextInt(world.getHeight());
        } while (posX < 0 || posY < 0 || !(world.get(posX, posY) instanceof Floor));
        // posX=5; posY=14;
        this.world.put(this, posX, posY);
    }

    public void setReceiver(BlockingQueue<KeyEvent> keyMessage) {
        this.keyMessage = keyMessage;
    }

    public synchronized void move(KeyEvent key) {
        int keycode = key.getKeyCode();
        int[][] action = { { -1, 0 }, { 0, -1 }, { 1, 0 }, { 0, 1 } };// left up right down
        int[] step = action[keycode - 37];
        int nxtX = step[0] + posX;
        int nxtY = step[1] + posY;
        if (nxtX >= 0 && nxtX < World.WIDTH && nxtY >= 0 && nxtY < World.HEIGHT
                && (world.get(nxtX, nxtY) instanceof Floor)) {//
            this.moveTo(nxtX, nxtY);
            world.put(new Floor(world), posX, posY);
            posX = nxtX;
            posY = nxtY;
        }
    }

    public synchronized void getStone(KeyEvent key) {
        if (stone != null)
            return;
        int[][] action = { { -1, 0 }, { 0, -1 }, { 1, 0 }, { 0, 1 } };
        // left up right down
        int dir;
        if (key.getKeyCode() == KeyEvent.VK_W)
            dir = 1;
        else if (key.getKeyCode() == KeyEvent.VK_D)
            dir = 2;
        else if (key.getKeyCode() == KeyEvent.VK_S)
            dir = 3;
        else if (key.getKeyCode() == KeyEvent.VK_A)
            dir = 0;
        else
            return;
        int[] step = action[dir];
        int nxtX = step[0] + posX;
        int nxtY = step[1] + posY;
        if (nxtX >= 0 && nxtX < World.WIDTH && nxtY >= 0 && nxtY < World.HEIGHT
                && (world.get(nxtX, nxtY) instanceof Stone)) {
            stone = (Stone) (world.get(nxtX, nxtY));
            stone.getOccupied(this);
        }

    }

    public boolean isEmpty() {
        return stone == null;
    }

    public void pushStone(KeyEvent key) {
        int[][] action = { { -1, 0 }, { 0, -1 }, { 1, 0 }, { 0, 1 } };
        // left up right down
        if (key.getKeyCode() == KeyEvent.VK_A)
            stone.setDir(action[0]);
        else if (key.getKeyCode() == KeyEvent.VK_W)
            stone.setDir(action[1]);
        else if (key.getKeyCode() == KeyEvent.VK_D)
            stone.setDir(action[2]);
        else if (key.getKeyCode() == KeyEvent.VK_S)
            stone.setDir(action[3]);
        else
            return;
        Stone weapon = new Stone(world);
        weapon = stone;
        stone = null;// 发射的时候不影响怪兽重新拿到石头
        weapon.run();

    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        // System.out.println("monster "+hp);
        while (hp > 0 && !sig.getStopBit()) {
            KeyEvent key;
            while ((key = keyMessage.poll()) != null) {
                int code = key.getKeyCode();
                if (code == KeyEvent.VK_W || code == KeyEvent.VK_A || code == KeyEvent.VK_S || code == KeyEvent.VK_D) {
                    if (stone != null)
                        pushStone(key);
                    else
                        getStone(key);
                } else if (code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN || code == KeyEvent.VK_LEFT
                        || code == KeyEvent.VK_RIGHT) {
                    move(key);
                }
            }
        }
        if (hp <= 0) {
            System.out.println("Monster dead!");
            world.put(new Floor(world), posX, posY);// dead and clean
        }

    }

    @Override
    public synchronized void getHurt(int attackedValue) {
        hp -= attackedValue;
        System.out.println("Monster hp " + this.hp);
    }

    public boolean isAlive() {
        if (hp <= 0) {
            world.put(new Floor(world), posX, posY);
            return false;
        }
        return true;
    }

}
