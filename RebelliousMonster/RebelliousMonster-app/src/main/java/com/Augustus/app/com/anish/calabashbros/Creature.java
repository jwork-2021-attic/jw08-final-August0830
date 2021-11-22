package com.Augustus.app.com.anish.calabashbros;

import java.awt.Color;

public class Creature extends Thing {
    int hp;
    Creature(Color color, char glyph, World world,int hp) {
        super(color, glyph, world);
        this.hp=hp;
    }

    public void moveTo(int xPos, int yPos) {
        this.world.put(this, xPos, yPos);
    }

    public void getHurt(int attackedValue){
        hp-=attackedValue;
    }
}
