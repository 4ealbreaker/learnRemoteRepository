package com.wxx.drools.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @PakageName
 * @author: xiuxu.wang
 * @description
 * @ClassName
 * @date: 2020-07-13 09:17
 **/
@Data
public class Demo implements Serializable {
    private String name;

    private String id;

    private Integer cardCount;

    private String decision;

}
