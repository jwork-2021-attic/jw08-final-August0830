package com.Augustus.app;

import java.util.ArrayList;
import java.util.Random;

public class LogTest {
    public static void main(String[] args) {
        ArrayList<Thread> threads = new ArrayList<Thread>();
        int n=1;
        for (int i = 0; i < n; ++i) {
            threads.add(new Thread(new Counter(i + 1)));
            threads.get(i).start();
        }
    }

}

class Counter implements Runnable {
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
            showVal();
            val = r.nextInt(100);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

}