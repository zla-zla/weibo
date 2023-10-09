package com.example.weiboserver.modules.system.domain;


import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Data
@Entity(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;     //id
    private String profileUrl;    //头像图片的Url
    private String introduction;    //介绍
    private String name;    //姓名
    @Column(unique=true)
    private String account;     //账号
    private String password;    //密码
    private String sex;     //性别
    private String phone;   //电话
    private Integer post_count;     //点赞数
    private Integer concerned_count;    //关注数
    private Integer fans_count;     //粉丝数

}
