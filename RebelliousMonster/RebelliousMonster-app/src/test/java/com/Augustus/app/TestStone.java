package com.Augustus.app;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.Augustus.app.com.anish.calabashbros.Monster;
import com.Augustus.app.com.anish.calabashbros.Stone;
import com.Augustus.app.com.anish.calabashbros.World;

import org.junit.Test;

public class TestStone {
    @Test
    public void TestGetRace(){
        World world = new World();
        Monster m1 = new Monster(world,10);
        Monster m2 = new Monster(world,10);
        Stone s1 = new Stone(world,5,5);

        s1.getOccupied(m1);
        s1.getOccupied(m2);
        assertFalse(s1.master().equals(m2));
        assertTrue(s1.master().equals(m1));
    }
}