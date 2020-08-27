package com.wxx.kafkademo.test;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @PakageName
 * @author: xiuxu.wang
 * @description
 * @ClassName
 * @date: 2020-06-15 11:21
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class ConsumeTest {

    KafkaConsumer kafkaConsumer;
    @Before
    public void connect(){
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        //指定消费者组的名字
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer_test1");

        //📶消费者分区策略
        properties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, "");

        //关闭位移的自动提交
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        //设置为新的消费者组的读取数据为起始位置
        //叫earliest而不是0是因为partition的删除策略导致开始读的数据不是第一条
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        kafkaConsumer = new KafkaConsumer(properties);

    }
    @Test
    public void consume(){
        //订阅主题和分区 参数（主题，再均衡的监听器） 多个订阅方法同时使用只会订阅最后一个 订阅多个用patten的重载方法
        kafkaConsumer.subscribe(Collections.singletonList("topic-demo"), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> collection) {

            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> collection) {

            }
        });
        //kafka消费端消费符合正则的主题
        kafkaConsumer.subscribe(Pattern.compile("topic-*"));
        //assign的方式订阅 指定分区消费
        //类似一个实体的概念对象
        TopicPartition tp = new TopicPartition("topic-demo", 1);
        kafkaConsumer.assign(Collections.singleton(tp));

        //指定哪个分区offset消费
        kafkaConsumer.seek(new TopicPartition("topic-demo",2),0);

        //返回指定主题的分区元数据信息
        List<TopicPartitionInfo> demo = kafkaConsumer.partitionsFor("demo");

        //kafka取消订阅
        kafkaConsumer.unsubscribe();
        //assignment获取的是kafka分配的分区信息
        Set<TopicPartition> assignment = kafkaConsumer.assignment();

        try {
            while (true) {
                //消息的消费和ProducerRecord对应 消费的阻塞时间
                //拿到的是消息对象的集合 包含topic的分区，key，value等元信息
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    String topic = record.topic();
                    int partition = record.partition();
                    long offset = record.offset();
                }

                //获取消费集的所有分区
                Set<TopicPartition> partitions = records.partitions();
                //获取消费集的指定分区消息
                for (TopicPartition partition : partitions) {
                    //消费集
                    List<ConsumerRecord<String, String>> consumerRecords = records.records(partition);
                    for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                        System.out.println("分区："+consumerRecord.partition()+" 主题："+consumerRecord.topic());
                        //可以按照分区粒度进行同步提交
                    }
                    long lastOffset = consumerRecords.get(consumerRecords.size() - 1).offset();
                    //同步提交了每个分区下的位移
                    kafkaConsumer.commitSync(Collections.singletonMap(partition,new OffsetAndMetadata(lastOffset)));
                }

                //位移的提交
                //位移的延迟提交 提交的offset是已经消费的条数+1
                kafkaConsumer.commitSync(Duration.ofMillis(1000));
                //kafkaConsumer.position(TopicPartition topicPartition) kafka下一次要消费的offset
                //消费集中每个分区对应的已提交位移
                Map committed = kafkaConsumer.committed(partitions);
                //可以按照分区粒度进行同步提交
                kafkaConsumer.commitSync();

                //kafka消费暂停
                kafkaConsumer.paused();
                //kafka恢复消费
                kafkaConsumer.resume(Collections.singleton(new TopicPartition("demo",2)));

                //kafka的异步提交
                kafkaConsumer.commitAsync((map, e) -> {
                    if(null != e) {
                        System.out.println("其实可以不用处理因为后面的提交也会成功的");
                    }else {
                        map.forEach((TopicPartition k, OffsetAndMetadata v) ->{
                            System.out.println(k+"-->"+v);
                        });
                    }

                });


            }
        } finally {
            //保证offset一定提交
            kafkaConsumer.commitSync();
            kafkaConsumer.close(); //4
        }

    }

}
