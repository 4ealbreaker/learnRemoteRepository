package com.wx.test;

import org.junit.Test;

import java.util.HashMap;

public class TestQuickTick  {
    @Test
    public void testFor(){
        HashMap<Object, Object> map = new HashMap<>();
        map.put(1,"love");
        map.put(2,"gate");
        map.forEach((k,v)->{
            System.out.println(k+"----"+v);
        });

        for (                   Object o : map.keySet()) {
            System.out.println(o.toString());
        }
        /*for (Object o : map.keySet()) {

        }*/
        //哈哈哈
        //哈哈哈
        


    }


}
