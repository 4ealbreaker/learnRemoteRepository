package com.wxx.dubbo.service;

import org.apache.dubbo.config.annotation.Service;

/**
 * @PakageName
 * @author: xiuxu.wang
 * @description
 * @ClassName
 * @date: 2020-06-10 18:02
 **/

@Service(version = "1.0.0",interfaceClass = TestService.class)
//@Service(version = "1.0.0")
public class TestServiceImpl implements TestService {
    @Override
    public void ins() {
        System.out.println("insert");
    }

    @Override
    public void del() {
        System.out.println("delete");
    }

    @Override
    public void upd() {
        System.out.println("update");
    }

    @Override
    public void sel() {
        System.out.println("select");
    }
}
