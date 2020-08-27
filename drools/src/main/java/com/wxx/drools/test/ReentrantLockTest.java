package com.wxx.drools.test;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @PakageName
 * @author: xiuxu.wang
 * @description
 * @ClassName
 * @date: 2020-07-15 17:07
 **/

public class ReentrantLockTest {
    Lock lock = new ReentrantLock(true);//手工锁//设置为公平锁那么执行方法线程交替获取


    public void method(){
        try {
            lock.lock();
            for (int i = 0;i<10;i++){
                System.out.println(i);
                TimeUnit.SECONDS.sleep(100000);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();//手动释放锁
        }
    }
    public void method2(){
        boolean isLocked = false;
        try {
            //isLocked = lock.tryLock(10, TimeUnit.SECONDS);//等待时间那到锁
            lock.lockInterruptibly();//尝试获取锁,若获取不到锁则一直阻塞
            if(isLocked){
                System.out.println("hahah");
                lock.unlock();
            }else{
                System.out.println("no");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            if(isLocked){
                lock.unlock();
            }
        }
    }


    public static void main(String[] args) {
        ReentrantLockTest reentrantLockTest = new ReentrantLockTest();
        new Thread(()->{
            reentrantLockTest.method();

        },"2").start();

        Thread t1 = new Thread(() -> {
            reentrantLockTest.method2();

        }, "1");
        t1.start();


        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t1.interrupt();
    }




}
class T {

    ReentrantLock lock = new ReentrantLock();

    void m() {
        boolean isLocked = false;        // 记录是否得到锁

        // 改变下面两个量的大小关系,观察输出
        int synTime = 4;   	 // 同步操作耗时
        int waitTime = 2;    // 获取锁的等待时间

        try {
            isLocked = lock.tryLock(waitTime, TimeUnit.SECONDS);    // 线程在这里阻塞waitTime秒,尝试获取锁
            if (isLocked) {
                // 若waitTime秒内得到锁,则执行同步操作
                for (int i = 1; i <= synTime; i++) {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println(Thread.currentThread().getName() + "持有锁,执行同步操作");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 使用tryLock()方法,尝试解除标记时,一定要先判断当前线程是否持有锁
            if (isLocked) {
                lock.unlock();
            }
        }
        // 执行非同步操作
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "没持有锁,执行非同步操作");
        }
    }

    public static void main(String[] args) {
        T t = new T();
        new Thread(t::m, "线程1").start();
        new Thread(t::m, "线程2").start();
    }
}





class FixedContainer<T>{
    public final Integer MAX_SIZE = 10;
    public volatile Integer count = 0;//count 可见

    Lock lock = new ReentrantLock();//感觉等待时间进行线程的调度
    CountDownLatch countDownLatch = new CountDownLatch(10);

    LinkedList<T> list = new LinkedList<T>();

    public Integer getCount(){
        return count;
    }
    //消费的方法
    public synchronized T get(){
        //lock.lock();
        T t = null;
        try{
            //如果线程容器满了就通知消费线程消费 把自己阻塞了
            while (list.size() <= 0){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
             t = list.removeFirst();//消费最新的数据
            count--;
            this.notifyAll();
            return t;
        }finally {
            //lock.unlock();
        }
    }
    //生产方法
    public synchronized void put(T obj){
        //lock.lock();//手动上锁
        try{
            while (count >= MAX_SIZE) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            list.add(obj);
            count++;
            this.notifyAll();
        }finally {
            //lock.unlock();//手动解锁
        }
    }

    public static void main(String[] args) {
        FixedContainer<String> fixed = new FixedContainer<>();

        //消费线程
        for(int i = 0;i < 10;i++){
            System.out.println("消费者启动");
            new Thread(()->{
                for(int j = 0;j < 2;j++){
                    fixed.get();
                    System.out.println(Thread.currentThread().getName()+"   "+fixed.getCount());
                }
            },"consumer"+i).start();
        }
        //消费后睡眠一会儿
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //生产线程
        for (int i = 0;i < 2;i++){
            new Thread(()->{
                System.out.println("生产者启动");
                for(int j = 0;j < 2;j++){
                    fixed.put(Thread.currentThread().getName()+">>>>>"+j);
                    System.out.println(Thread.currentThread().getName()+"  "+fixed.getCount());
                }
            },"producer"+i).start();
        }


    }





}

