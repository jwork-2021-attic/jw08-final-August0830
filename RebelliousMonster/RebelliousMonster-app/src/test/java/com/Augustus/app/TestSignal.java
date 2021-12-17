package com.Augustus.app;

import static org.junit.Assert.assertTrue;

import com.Augustus.app.com.anish.calabashbros.Signal;

import org.junit.Test;

public class TestSignal {
    @Test
    public void testStopSig(){
        Signal sig=new Signal();
        TestCreature tc = new TestCreature();
        tc.setSig(sig);
        tc.showSig();
        assertTrue(tc.getSig().getStopBit()==false);
        sig.setStopBit(true);
        tc.showSig();
        assertTrue(tc.getSig().getStopBit()==true);
    }
}

class TestCreature{
    Signal sig;
    TestCreature(){};

    public void setSig(Signal sig){
        this.sig = sig;
    }

    public void showSig(){
        System.out.println(sig.getStopBit());
    }
    
    public Signal getSig(){
        return sig;
    }
}