package com.example.weiboserver.modules.utils;

import com.example.weiboserver.modules.utils.annotation.query;
import lombok.Data;

import java.security.Timestamp;
import java.util.List;

@Data
public class QueryCriteria {

    // 等于
    @query
    private String a;

    // 左模糊
    @query(type = query.Type.LEFT_LIKE)
    private String b;

    // 右模糊
    @query(type = query.Type.RIGHT_LIKE)
    private String c;

    // 大于等于
    @query(type = query.Type.GREATER_THAN, propName = "createTime")
    private Timestamp startTime;

    // 小于等于
    @query(type = query.Type.LESS_THAN, propName = "createTime")
    private Timestamp endTime;

//    // BETWEEN
//    @query(type = query.Type.BETWEEN)
//    private List<Timestamp> startTime;

    // 多字段模糊查询，blurry 为字段名称
    @query(blurry = "a,b,c")
    private String blurry;

    // IN 查询
    @query(type = query.Type.IN)
    private List<String> d;

    // 左关联查询，left Join ， joinName为关联实体名称 , propName为关联实体 字段
    @query(joinName = "", propName="")
    private String e;

    // 右关联查询，right Join ， joinName为关联实体名称 , propName为关联实体 字段
    @query(joinName = "", propName="", join = query.Join.RIGHT)
    private String f;

    // NOT_EQUAL 不等于
    @query(type = query.Type.NOT_EQUAL)
    private String g;

//    // NOT_NULL 不为空
//    @query(type = query.Type.NOT_NULL)
//    private String g;
}

