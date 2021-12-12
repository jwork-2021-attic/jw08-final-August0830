package com.Augustus.app;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;

import com.Augustus.app.com.anish.screen.ByteUtil;


import org.junit.Test;


public class TestByteUtil {
    @Test
    public void testByteUtil() throws Exception{
        TestClass test=new TestClass(7,"BTS");
        byte[] buf = ByteUtil.toByteArray(test);
        TestClass copy= (TestClass)(ByteUtil.toObject(buf));
        test.Output();
        copy.Output();
        assertEquals(test.a,copy.a);
        assertEquals(test.str,copy.str);
    }
}

class TestClass implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public int a;
    public String str;
    public TestClass(int val,String s){
        this.a=val;
        this.str=s;
    }
    public void Output(){
        System.out.println(a+" "+str);
    }
}
