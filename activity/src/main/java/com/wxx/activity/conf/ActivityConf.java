package com.wxx.activity.conf;

import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @PakageName
 * @author: xiuxu.wang
 * @description
 * @ClassName
 * @date: 2020-06-22 13:40
 **/
@Configuration
public class ActivityConf {

    @Bean
    public ProcessEngine processEngine(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();


        return defaultProcessEngine;
    }

    @Bean
    public RepositoryService repositoryService() {
        return processEngine().getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService() {
        return processEngine().getRuntimeService();
    }

    @Bean
    public TaskService taskService() {
        return processEngine().getTaskService();
    }
    @Bean
    public HistoryService historyService() {
        return processEngine().getHistoryService();
    }

}
