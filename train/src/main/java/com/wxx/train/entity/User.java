package com.wxx.train.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @PakageName
 * @author: xiuxu.wang
 * @description
 * @ClassName
 * @date: 2020-08-14 18:07
 **/
@Table(name="user")
public class User {
    @Id
    private String id;

    private String name;

    private boolean sex;

    private Date birth;

    private Integer age;


}
