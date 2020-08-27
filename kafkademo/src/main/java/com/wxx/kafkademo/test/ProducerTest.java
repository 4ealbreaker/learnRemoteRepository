package com.wxx.kafkademo.test;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.clients.producer.internals.DefaultPartitioner;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @PakageName
 * @author: xiuxu.wang
 * @description
 * @ClassName
 * @date: 2020-06-15 10:14
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class ProducerTest {
    KafkaProducer kafkaProducer;
    @Before
    public void connectKafka(){
        Properties properties = new Properties();
        //填写kafka的配置

        //配置消息发送的重试次数
        properties.put(ProducerConfig.RETRIES_CONFIG, 3);
        //配置kafka的分区器 自定义的分区器实现Partitioner接口
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, DefaultPartitioner.class.getName());
        //连接配置
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");//kafka地址
//        key和value的序列化 自定义的序列化器实现Serializable<T>接口
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        List<String> interceptorList = new ArrayList<>();
        interceptorList.add("com.wxx.kafkademo.conf.KafkaIntercept");

        //最大的发送大小 默认1M
        properties.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG,10248576);

        //RecordAccumulator缓存的大 小可以通过生产者客户端参数 buffer memory 配置默认32M
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);

        //如果生产者发送数据的速度大于传输到kafka的速度，则send（）方法被阻塞或者抛出异常
        properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 60);

        //发送前缓存的byteBuffer缓存的字节区大小供BufferPool（存在于RecordAccumulator）管理，详情间kafka文档
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);//16k

        //并且通过配置参数还可 以限制每个连接（也就是客户端与 Node 之间的连接）最多缓存的请求数
        properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 10);

        //拦截器链的配置 自定义的拦截器实现ProducerInterceptor
        properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, interceptorList);

        //ack的机制 1 leader副本同步完就返回消息丢失风险 0 生产者不需要等待leader的响应就继续发送 -1/all等待ISR全部同步
        properties.put(ProducerConfig.ACKS_CONFIG,"-1");

        //发送失败重试
        properties.put(ProducerConfig.RETRIES_CONFIG,10);
        //重试间隔时间ms
        properties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG,10);

        //producerBatch的发送等待时间 如果想batch累加多点则可设置久点
        properties.put(ProducerConfig.LINGER_MS_CONFIG,0);

        kafkaProducer = new KafkaProducer(properties);
    }

    @Test
    public void produce(){
        Future send = null;
        for (int i = 0; i < 10; i++) {
            String stringBuilder = "produce message"+i;
            ProducerRecord<String,String> producerRecord = new ProducerRecord<String, String>("topic-demo","topic-demo",stringBuilder.toString());

            send = kafkaProducer.send(producerRecord, (recordMetadata, e) -> {
            });
        }


        try {
            RecordMetadata o = (RecordMetadata) send.get(1000, TimeUnit.MILLISECONDS);
            System.out.println(o);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {
            kafkaProducer.close();
        }


    }


    @Test
    public void receive(){
        //kafka的常用对象和方法

        //构建消息对象
        ProducerRecord<String,String> producerRecord = new ProducerRecord<String, String>
                ("topic-dem0",2,System.currentTimeMillis(),"1","kafka-demo");
        //消息发送方法 callback为异步发送的方法 send方法可以发送多条消息对象
        Future send = kafkaProducer.send(producerRecord, (recordMetadata, e) -> {
            if(null != e){
                System.out.println("发送失败，做重新发送的处理");
            }else{
                System.out.println("发送成功返回");
                System.out.println("主题"+recordMetadata.topic());
                System.out.println("分区"+recordMetadata.partition());
                System.out.println("偏移量"+recordMetadata.offset());
            }

        });
        try {
            //获取的是消息的元信息
            RecordMetadata metadata = (RecordMetadata) send.get();
            System.out.println("主题"+metadata.topic());
            System.out.println("分区"+metadata.partition());
            System.out.println("偏移量"+metadata.offset());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }finally {
            //有参的close（）方法 等待时间关闭
            kafkaProducer.close(Duration.ofMillis(10000));
        }

    }


}
