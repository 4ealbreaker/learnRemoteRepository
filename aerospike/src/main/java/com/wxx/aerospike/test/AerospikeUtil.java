package com.wxx.aerospike.test;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.cdt.MapOperation;
import com.aerospike.client.cdt.MapPolicy;
import com.aerospike.client.policy.WritePolicy;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @PakageName
 * @author: xiuxu.wang
 * @description
 * @ClassName
 * @date: 2020-08-21 11:56
 **/
@Component
public class AerospikeUtil {


    public static Integer ttl;


    @Value("${ttl}")
    public void setTtl(Integer ttl) {
        AerospikeUtil.ttl = ttl;
    }

    public static void setValue(String key,Long value){
        AerospikeClient aerospikeClient = new AerospikeClient("10.57.34.114",3000);

        WritePolicy policy = null;
        System.out.println(ttl);
        if (null != ttl) {
            policy = BeanUtils.instantiateClass(WritePolicy.class);
            policy.expiration = ttl;
        }
        Key asKey = new Key("ns1", "demo", "map");
        //Bin asValue = new Bin(asDefaultBinName,map);
        Record operate = aerospikeClient.operate(policy, asKey, MapOperation.put(MapPolicy.Default,"foo", com.aerospike.client.Value.get(key), com.aerospike.client.Value.get(value)));

        Record record = aerospikeClient.get(policy, asKey);
        Map<?, ?> map = record.getMap("foo");
        Object choose = map.get("choose");
        System.out.println(choose);


    }



    public static void main(String[] args) {
        setValue("choose",60l);

    }


}
