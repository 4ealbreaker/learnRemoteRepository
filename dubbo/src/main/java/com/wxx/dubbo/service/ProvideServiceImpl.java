package com.wxx.dubbo.service;


import org.apache.dubbo.config.annotation.Service;

/**
 * @PakageName
 * @author: xiuxu.wang
 * @description
 * @ClassName
 * @date: 2020-06-10 10:04
 **/

@Service(version = "1.0.0",interfaceClass = ProvideService.class)
public class ProvideServiceImpl implements ProvideService {
    @Override
    public String provideMessage(String message) {
        System.out.println(message);
        return message+" is received";
    }
}
