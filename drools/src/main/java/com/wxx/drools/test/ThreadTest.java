package com.wxx.drools.test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @PakageName
 * @author: xiuxu.wang
 * @description
 * @ClassName
 * @date: 2020-07-15 14:44
 **/



public class ThreadTest {
    volatile Boolean flag = true;
    void m(){
        while (flag){
            System.out.println("hahahah");
        }
        System.out.println("111111");
    }

    public static void main(String[] args) {
       ThreadTest t = new ThreadTest();
        new Thread(t::m,"t1").start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t.flag = false;
    }

}

class MyContainer{
    List list = new ArrayList<>();

    public void add(Object o){
        list.add(o);
    }
    public Integer size(){
        return list.size();
    }


    public static void main(String[] args) {
        MyContainer myContainer = new MyContainer();
        Object lock = new Object();
        //创建门闩对象 这个对象不需要锁定如何对象相比等待同步机制更加灵活
        CountDownLatch countDownLatch = new CountDownLatch(1);

        new Thread(()->{
            System.out.println("启动"+Thread.currentThread().getName());
            if(myContainer.size() != 5){
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /*try {
                    lock.wait();
                    lock.notify();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                System.out.println("第五个结束");
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        },"t2").start();

        new Thread(()->{
            System.out.println("启动"+Thread.currentThread().getName());
            synchronized (lock){
                for (int i = 1;i<=10;i++){
                    myContainer.add(i);
                    if(myContainer.size() == 5){
                        countDownLatch.countDown();

//                        lock.notify();
//                        try {
//                            lock.wait();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }

                    }
                    System.out.println("添加"+i);
                }
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t1").start();



    }


}
