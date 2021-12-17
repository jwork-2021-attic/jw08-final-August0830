package com.Augustus.app.com.anish.calabashbros;

import com.Augustus.app.asciiPanel.AsciiPanel;

public class Stone extends Thing implements Runnable {

    Monster monster;
    int posX;
    int posY;
    int[] dir;

    public Stone(World world, int posX, int posY) {
        super(AsciiPanel.cyan, (char) 177, world);
        this.posX = posX;
        this.posY = posY;
        monster=null;
    }

    public Stone(World world) {
        super(AsciiPanel.cyan, (char) 177, world);
        monster=null;
    }

    public synchronized void getOccupied(Monster mon) {
        if(monster==null){
            this.monster = mon;
            world.put(new Floor(world), posX, posY);
        }
    }

    public void setDir(int[] is) {
        this.dir = is;
        posX = monster.getX();
        posY = monster.getY();
    }

    private synchronized void moveTo(int x, int y) {
        world.put(this, x, y);
        if (posX != monster.getX() || posY != monster.getY()){
            world.put(new Floor(world), posX, posY);
        }
            
        // System.out.println("stone x "+posX+" y "+posY+
        // "monster x "+monster.getX()+" y "+monster.getY());
        posX = x;
        posY = y;
    }

    public Monster master(){
        return monster;
    } 
    
    @Override
    public void run() {
        // TODO Auto-generated method stub
        int nxtX = posX + dir[0];
        int nxtY = posY + dir[1];
        while (nxtX >= 0 && nxtX < World.WIDTH && nxtY >= 0 && nxtY < World.HEIGHT
                && (world.get(nxtX, nxtY) instanceof Floor)) {
            
            moveTo(nxtX, nxtY);
            nxtX = posX + dir[0];
            nxtY = posY + dir[1];
            try {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        this.monster = null;
        if(nxtX >= 0 && nxtX < World.WIDTH && nxtY >= 0 && nxtY < World.HEIGHT
        && world.get(nxtX, nxtY) instanceof Goblin){
            ((Goblin)world.get(nxtX,nxtY)).getHurt(150);   
        }
    }

}