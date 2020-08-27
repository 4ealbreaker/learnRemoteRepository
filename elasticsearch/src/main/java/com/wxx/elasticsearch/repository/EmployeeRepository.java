package com.wxx.elasticsearch.repository;

import com.wxx.elasticsearch.entity.Employee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @PakageName
 * @author: xiuxu.wang
 * @description
 * @ClassName
 * @date: 2020-06-17 09:59
 **/
@Repository
public interface EmployeeRepository extends ElasticsearchRepository<Employee, String> {
}
