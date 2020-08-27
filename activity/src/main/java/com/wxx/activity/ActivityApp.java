package com.wxx.activity;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @PakageName
 * @author: xiuxu.wang
 * @description
 * @ClassName
 * @date: 2020-06-22 13:38
 **/

@SpringBootApplication(exclude =  SecurityAutoConfiguration.class)
public class ActivityApp {

    public static void main(String[] args) {
        SpringApplication.run(ActivityApp.class, args);
    }

}
