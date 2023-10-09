package com.example.weiboserver.modules.system.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "reply")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;             //评论的id
    private Integer fid;            //父评论的id
    private Integer uid;            //评论用户的id
    private String profileURL;      //评论者头像的url
    private String name;            //评论者用户名
    private String fname;           //父评论者用户名
    @Column(columnDefinition = "text")
    private String text;            //评论的内容正文
    private Integer likeCount;          //评论的点赞数
    private Integer isReply;            //本条评论是否为回复
    private Long time;            //评论时间
}
