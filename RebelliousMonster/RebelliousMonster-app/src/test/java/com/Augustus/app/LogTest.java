package com.Augustus.app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import com.Augustus.app.com.anish.screen.ByteUtil;

public class LogTest {
    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        // ArrayList<Thread> threads = new ArrayList<Thread>();
        output(1);
        input();
    }

    static void input() throws FileNotFoundException, IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./log.txt"));
        Object ob;
        
        while(true){
            try{
                ob = ois.readObject();
                ArrayList<Counter> counter = (ArrayList<Counter>)ob;
                for(Counter c:counter){
                    c.showVal();
                }
            }catch(EOFException eofex){
                break;
            }
        }
        
    }

    static void output(int n) throws InterruptedException, IOException {
        ArrayList<Counter> counters = new ArrayList<Counter>();
        for (int i = 0; i < n; ++i) {
            counters.add(new Counter(i+1));
            // threads.add(new Thread(counters.get(i)));
            counters.get(i).start();
        }

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./log.txt"));
       
        Long start = System.currentTimeMillis();
        while(System.currentTimeMillis()-start<=1000*n){
            oos.writeObject(counters);
            for(int i=0;i<n;++i)
                counters.get(i).showVal();
            Thread.sleep(500);
        }

        for(int i=0;i<n;++i){
            counters.get(i).interrupt();
            System.out.println(i+1+" counter is stop:"+counters.get(i).isInterrupted());
        }
       
        oos.close();
    }
}

class Counter extends Thread implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Random r;
    int val;
    int id;

    public Counter(int id) {
        r = new Random();
        val = r.nextInt(100);
        this.id = id;
    }

    public int getVal() {
        return val;
    }

    public void showVal() {
        System.out.println(id + ": " + val);
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (true) {
            if(this.isInterrupted())
                return;
            val = r.nextInt(100);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
                break;
            }
        }
        System.out.println(id+" stop running");
    }

}