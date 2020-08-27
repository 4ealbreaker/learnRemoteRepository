package com.wxx.elasticsearch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @PakageName
 * @author: xiuxu.wang
 * @description
 * @ClassName
 * @date: 2020-06-17 09:37
 **/
@Document(indexName = "ems",type = "emp")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Employee {
    @Id
    private String id;
    @Field(type = FieldType.Text,searchAnalyzer = "ik_max_word")
    private String name;
    @Field(type = FieldType.Integer)
    private Integer age;
    @Field(type = FieldType.Date,format = DateFormat.basic_date)
    private Date birth;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String content;

}
