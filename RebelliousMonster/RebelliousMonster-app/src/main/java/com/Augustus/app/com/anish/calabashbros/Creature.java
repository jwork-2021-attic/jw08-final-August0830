package com.Augustus.app.com.anish.calabashbros;

import java.awt.Color;

public class Creature extends Thing {
    int hp;
    Signal sig;
    Creature(Color color, char glyph, World world,int hp) {
        super(color, glyph, world);
        this.hp=hp;

    }

    public void setStopSig(Signal stop){
        this.sig=stop;
    }

    public Signal getStopSig(){
        return this.sig;
    }

    public void moveTo(int xPos, int yPos) {
        this.world.put(this, xPos, yPos);
    }

    public synchronized void getHurt(int attackedValue){
        hp-=attackedValue;
        //System.out.println("hp "+this.hp);
    }
}
