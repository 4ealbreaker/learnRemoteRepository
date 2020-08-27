package com.wxx.aerospike.test;

import com.aerospike.client.*;
import com.aerospike.client.cdt.MapOperation;
import com.aerospike.client.cdt.MapPolicy;
import com.aerospike.client.policy.AdminPolicy;
import com.aerospike.client.policy.WritePolicy;
import com.wxx.aerospike.entity.Student;
import jdk.nashorn.internal.parser.JSONParser;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.json.JSONString;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.logging.Logger;

/**
 * @PakageName
 * @author: xiuxu.wang
 * @description
 * @ClassName
 * @date: 2020-07-07 15:39
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class AerospikeTest {

    private AerospikeClient aerospikeClient;

    private WritePolicy writePolicy;

    private Key key;
    @Before
    public void connection(){
        //创建连接
         aerospikeClient = new AerospikeClient("10.57.34.114", 3000);
        //写入策略
         writePolicy = new WritePolicy();

         writePolicy.timeoutDelay=10;
        //key
        key = new Key("ns1", "demo", "foo");

    }

    @Test
    public void opration(){
        Record record = aerospikeClient.operate(writePolicy, key, MapOperation.put(MapPolicy.Default, "foo1", Value.get("deviceInfonew1"), Value.get(10L)));
        Record operate = aerospikeClient.operate(writePolicy, key, MapOperation.put(MapPolicy.Default, "foo1", Value.get("hahaha"), Value.get(123)));

        Record records = aerospikeClient.get(writePolicy, key);
        Map<?, ?> foo = records.getMap("foo");
        Long deviceInfonew1 = (Long) foo.get("deviceInfonew1");
        System.out.println(deviceInfonew1);
    }

    //PK获取record
    @Test
    public void getRecord(){
        Record record = aerospikeClient.get(writePolicy, key);
        Map<String, Object> bins = record.bins;
        for (String s : bins.keySet()) {
        }
    }

    //删除指定key的record
    @Test
    public void deleteSet(){
        boolean delete = aerospikeClient.delete(writePolicy,key);
        System.out.println(delete);
    }
    //添加指定set的数据
    @Test
    public void insert(){
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();

        objectObjectHashMap.put("1234", 1234);


        Bin bin = new Bin("foo",objectObjectHashMap);

        //aerospikeClient.add(writePolicy,key,bin);
        aerospikeClient.put(writePolicy,key,bin);

    }
    //查询对象
    @Test
    public void get(){
            Record record = aerospikeClient.get(writePolicy, key);

        Map<?, ?> map = record.getMap("demo");
        System.out.println(map);

    }
    //
    @Test
    public void choose(){
        AerospikeUtil.setValue("get",20L);

    }

}
