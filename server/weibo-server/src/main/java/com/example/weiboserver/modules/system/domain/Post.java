package com.example.weiboserver.modules.system.domain;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Data
@Entity(name = "Post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                 //帖子的id
    private Integer uid;            //用户的id
    private String profileURL;      //头像的url
    private String name;            //用户名
    private String time;            //时间
    @Column(columnDefinition = "text")
    private String text;            //正文
    @Column(columnDefinition = "text")
    private String pics;            //图片链接集合（先单张图片）
    private Integer likeCount;          //点赞数
    private Integer collectCount;       //收藏数
    private Integer commentCount;       //评论数
}
