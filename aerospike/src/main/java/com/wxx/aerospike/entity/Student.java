package com.wxx.aerospike.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @PakageName
 * @author: xiuxu.wang
 * @description
 * @ClassName
 * @date: 2020-07-07 16:51
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Student implements Serializable {
    private String name;

    private Integer age;

    private Date date;

    private String id;

}
