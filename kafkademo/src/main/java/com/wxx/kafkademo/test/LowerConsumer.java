/*
package com.wxx.kafkademo.test;

import kafka.consumer.SimpleConsumer;

*/
/**
 * @PakageName
 * @author: xiuxu.wang
 * @description 使用低级api进行指定topic的partition的leader的offset的数据进行消费
 * @ClassName
 * @date: 2020-07-03 16:41
 **//*


public class LowerConsumer {
    public static void main(String[] args) {

    }


    public static final String TOPIC = "topic-demo";

    public static final Integer PARTITION = 1;

    public static final Integer OFFSET = 1;

    static final String IP = "localhost";

    static final Integer PORT = 9092;

    public SimpleConsumer getSimpleConsumer(){
        SimpleConsumer simpleConsumer = new SimpleConsumer(IP,PORT,1000,1024*4,"1");
        return simpleConsumer;

    }

    public void getLeader(){



    }






}
*/
