package com.Augustus.app;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;

import com.Augustus.app.com.anish.calabashbros.Goblin;
import com.Augustus.app.com.anish.calabashbros.Monster;
import com.Augustus.app.com.anish.calabashbros.Signal;
import com.Augustus.app.com.anish.calabashbros.World;

import org.junit.Test;

public class TestGoblin {
    @Test
    public void testRace(){
        World world =  new World();
        Goblin gob1 = new Goblin(Color.WHITE,world,10);
        Goblin gob2 = new Goblin(Color.WHITE,world,10);
        int xPos = gob1.getX();
        int yPos = gob1.getY();
        gob2.moveTo(xPos, yPos);
        System.out.println("mon1 "+gob1.getX()+" "+gob1.getY());
        System.out.println("mon2 "+gob2.getX()+" "+gob2.getY());
        assertFalse(gob1.getX()==gob2.getX() && gob1.getY()==gob2.getY());
    }

    @Test
    public void testAttack(){
        World world =  new World();
        Goblin gob1 = new Goblin(Color.WHITE,world,10);
        Goblin gob2 = new Goblin(Color.WHITE,world,10);
        Monster mon = new Monster(world,10);
        world.put(gob1,gob1.getX(),gob1.getY());
        world.put(gob2,gob2.getX(),gob2.getY());
        world.put(mon,mon.getX(),mon.getY());
        int xPos=gob1.getX();
        int yPos=gob1.getY();
        gob2.moveTo(xPos+1, yPos);
        mon.moveTo(xPos, yPos-1);
        gob1.attack(15);
        System.out.println(gob2.isAlive()+" "+mon.isAlive());
        assertTrue(gob2.isAlive()==true);
        assertTrue(mon.isAlive()==false);
    }

    @Test
    public void testStopBit(){
        Signal sig=new Signal();
        World world=new World();
        Goblin c = new Goblin(Color.WHITE,world,10);
        c.setStopSig(sig);
        System.out.println(c.getStopSig().getStopBit());
        assertTrue(c.getStopSig().getStopBit()==false);
        sig.setStopBit(true);
        System.out.println(c.getStopSig().getStopBit());
        assertTrue(c.getStopSig().getStopBit()==true);
    }

}