package com.Augustus.app.com.anish.calabashbros;

public class Signal {
    Boolean stop;
    Boolean gameEnd;
    int gobCnt;
    int monCnt;
    //support points fix position on screen
    public Signal(){
        this.stop=false;
        gameEnd = false;
    }

    public void setMonCnt(int cnt){
        monCnt=cnt;
    }

    public synchronized void decreaseMon(){
        monCnt--;
        if(monCnt<=0)
            gameEnd=true;
    }

    public synchronized void decreaseGob(){
        gobCnt--;
        if(gobCnt<=0)
            gameEnd=true;
    }

    public void setGobCnt(int cnt){
        gobCnt=cnt;
    }

    public void setStopBit(boolean b){
        stop=b;
    }

    public boolean getStopBit(){
        return stop;
    }

    public void setGameEnd(boolean b){
        gameEnd=b;
    }

    public boolean getGameEnd(){
        return gameEnd;
    }
}