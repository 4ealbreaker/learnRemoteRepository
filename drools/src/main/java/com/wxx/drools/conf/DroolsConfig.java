package com.wxx.drools.conf;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.kie.spring.KModuleBeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

/**
 * @PakageName
 * @author: xiuxu.wang
 * @description
 * @ClassName
 * @date: 2020-07-14 09:08
 **/
@Configuration
public class DroolsConfig {
    private static final String RULES_PATH = "rules/";

    private static final KieServices services = KieServices.Factory.get();

    @Bean
    @ConditionalOnBean//这个注解判断spring工厂是否存在了这个bean，不存在就实例化这个bean
    public KieFileSystem getKieFileSystem() throws IOException {
        KieFileSystem kieFileSystem = services.newKieFileSystem();
        //加载规则文件目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:" + RULES_PATH + "*.drl");
        String str = null;
        //写入每个文件到文件系统
        for (Resource resource : resources) {
            str = RULES_PATH + resource.getFilename();
            kieFileSystem.write(ResourceFactory.newClassPathResource(str,"UTF-8"));
        }

        return kieFileSystem;
    }
    @Bean
    @ConditionalOnMissingBean
    public KieContainer getKieContainer() throws IOException {
        KieRepository repository = services.getRepository();
        repository.addKieModule(repository::getDefaultReleaseId);

        KieBuilder kieBuilder = services.newKieBuilder(getKieFileSystem());

        kieBuilder.buildAll();

        return services.newKieContainer(repository.getDefaultReleaseId());

    }
    @Bean
    @ConditionalOnMissingBean
    public KModuleBeanFactoryPostProcessor getKModuleBeanFactoryPostProcessor(){
        return new KModuleBeanFactoryPostProcessor();
    }

    @Bean
    public KieBase kieBase() throws IOException {
        KieBase kieBase = getKieContainer().getKieBase();
        return kieBase;
    }


}
