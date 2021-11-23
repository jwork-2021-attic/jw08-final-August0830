package com.Augustus.app.com.anish.calabashbros;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Monster extends Creature {

    private int posX;
    private int posY;
    private Stone stone;
    public Monster(World world, int hp) {
        super(Color.WHITE, (char)2, world, hp);
        // TODO Auto-generated constructor stub
        Random r = new Random();

        do {
            posX = r.nextInt(world.getWidth());
            posY = r.nextInt(world.getHeight());
        } while (posX<0 || posY<0 ||
        !(world.get(posX, posY) instanceof Floor));
        // posX=5;  posY=14;
        this.world.put(this, posX, posY);
    }

    public synchronized void move(KeyEvent key) {
        int keycode = key.getKeyCode();
        int[][] action ={{-1,0},{0,-1},{1,0},{0,1}};//left up right down
        int[] step = action[keycode-37];
        int nxtX = step[0]+posX;
        int nxtY = step[1]+posY;
        if(nxtX>=0 && nxtX < World.WIDTH
        && nxtY>=0 && nxtY < World.HEIGHT
        &&(world.get(nxtX,nxtY) instanceof Floor)){//  
            this.moveTo(nxtX, nxtY);
            world.put(new Floor(world),posX,posY);
            posX=nxtX;
            posY=nxtY;
        }
	}

	public synchronized void getStone(KeyEvent key) {
        if(stone!=null)
            return;
        int[][] action ={{-1,0},{0,-1},{1,0},{0,1}};
        //left up right down
        int dir=0;
        if(key.getKeyCode()==KeyEvent.VK_W)
            dir=1;
        else if(key.getKeyCode()==KeyEvent.VK_D)
            dir=2;
        else if(key.getKeyCode()==KeyEvent.VK_S)
            dir=3;
        int[] step = action[dir];
        int nxtX = step[0]+posX;
        int nxtY = step[1]+posY;
        if(nxtX>=0 && nxtX < World.WIDTH
        && nxtY>=0 && nxtY < World.HEIGHT
        &&(world.get(nxtX,nxtY) instanceof Stone)){
            stone = (Stone)(world.get(nxtX,nxtY));
            stone.getOccupied(this);
        }
            
	}

	public boolean isEmpty() {
		return stone==null;
	}

	public void pushStone(KeyEvent key) {
        int[][] action ={{-1,0},{0,-1},{1,0},{0,1}};
        //left up right down
        if(key.getKeyCode()==KeyEvent.VK_A)
            stone.setDir(action[0]);
        else if(key.getKeyCode()==KeyEvent.VK_W)
            stone.setDir(action[1]);
        else if(key.getKeyCode()==KeyEvent.VK_D)
            stone.setDir(action[2]);
        else if(key.getKeyCode()==KeyEvent.VK_S)
            stone.setDir(action[3]);
        Stone weapon = new Stone(world);
        weapon=stone;
        stone=null;//发射的时候不影响怪兽重新拿到石头
        weapon.run();

	}

}
