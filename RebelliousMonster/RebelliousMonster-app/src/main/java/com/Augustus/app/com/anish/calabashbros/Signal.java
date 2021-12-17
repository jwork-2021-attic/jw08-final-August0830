package com.Augustus.app.com.anish.calabashbros;

public class Signal {
    Boolean stop;
    public Signal(){
        this.stop=false;
    }

    public void setStopBit(boolean b){
        stop=b;
    }

    public boolean getStopBit(){
        return stop;
    }
}