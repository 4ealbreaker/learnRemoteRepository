package com.wxx.elasticsearch.test;

import com.wxx.elasticsearch.entity.Employee;
import com.wxx.elasticsearch.repository.EmployeeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.UUID;

/**
 * @PakageName
 * @author: xiuxu.wang
 * @description
 * @ClassName
 * @date: 2020-06-17 09:09
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticSerarchTest {
    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    EmployeeRepository employeeRepository;

    @Test
    public void getAll() {
        Iterable<Employee> repositoryAll = employeeRepository.findAll();
        for (Employee employee : repositoryAll) {
            System.out.println(employee);
        }

    }

    @Test
    public void insert(){
        Employee employee = new Employee();
        employee.setAge(11);
        employee.setName("老王");
        employee.setBirth(new Date(19961104));
        employee.setId(UUID.randomUUID().toString());
        employee.setContent("CI/CD容器平台开放使用\n" +
                "不用申请机器、不占用预算资源、随意扩缩容，接入与试用请钉钉联系 侯诗军、吴姚迪、吴文林");
        Employee save = employeeRepository.save(employee);
    }

    public void filter(){}
}
