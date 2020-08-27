package com.wxx.drools.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @PakageName
 * @author: xiuxu.wang
 * @description
 * @ClassName
 * @date: 2020-07-13 14:39
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student implements Serializable {
    private String name;

    private Integer age;

    private String tag;

    private Date birth;

}
