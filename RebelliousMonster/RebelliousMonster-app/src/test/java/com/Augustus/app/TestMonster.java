package com.Augustus.app;

import static org.junit.Assert.assertFalse;

import java.awt.Color;

import com.Augustus.app.com.anish.calabashbros.Goblin;
import com.Augustus.app.com.anish.calabashbros.Monster;
import com.Augustus.app.com.anish.calabashbros.World;

import org.junit.Test;

public class TestMonster {
    @Test
    public void testRace(){
        World world =  new World();
        Monster mon1 = new Monster(world,10);
        world.put(mon1,mon1.getX(),mon1.getY());
        Goblin gob = new Goblin(Color.white,world,10);
        int xPos = mon1.getX();
        int yPos = mon1.getY();
        gob.moveTo(xPos, yPos);
        System.out.println("mon1 "+mon1.getX()+" "+mon1.getY());
        System.out.println("gob "+gob.getX()+" "+gob.getY());
        assertFalse(mon1.getX()==gob.getX() && mon1.getY()==gob.getY());
    }
}