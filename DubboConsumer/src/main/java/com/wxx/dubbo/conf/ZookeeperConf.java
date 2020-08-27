package com.wxx.dubbo.conf;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @PakageName
 * @author: xiuxu.wang
 * @description
 * @ClassName
 * @date: 2020-06-11 10:19
 **/
@Configuration
public class ZookeeperConf {

    @Bean
    public ZkClient getZkClient(){
        ZkSerializer zkSerializer = new SerializableSerializer();
        ZkClient zkClient = new ZkClient("10.57.241.225:2181",2000,5000,zkSerializer);
        return zkClient;
    }




}
