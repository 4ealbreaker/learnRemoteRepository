package com.wxx.dubbo.controller;



import com.wxx.dubbo.service.TestService;
import org.apache.dubbo.config.annotation.Reference;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @PakageName
 * @author: xiuxu.wang
 * @description
 * @ClassName
 * @date: 2020-06-10 11:01
 **/
@RestController
public class TestController {

    //    @Resource
//    private TestService testServiceImpl;
    @Reference(version = "1.0.0")
    private TestService testService;

    @RequestMapping("/ins")
    public String ins(){
        testService.ins();
        return "ins";
    }

    @RequestMapping("/del")
    public String del(){
        testService.del();
        return "del";
    }

    @RequestMapping("/upd")
    public String upd(){
        testService.upd();
        return "upd";
    }

    @RequestMapping("/sel")
    public String sel(){
        testService.sel();
        return "sel";
    }
}

