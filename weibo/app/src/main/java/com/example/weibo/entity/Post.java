package com.example.weibo.entity;

import java.io.Serializable;

public class Post implements Serializable {
    private int id;                 //帖子的id
    private int uid;                 //帖子的id
    private String profileURL;      //头像的url
    private String name;            //用户名
    private String time;            //时间
    private boolean isConcerned;    //是否为关注
    private String text;            //正文
    private String pics;            //图片链接集合（先单张图片）
    private int likeCount;          //点赞数
    private int collectCount;       //收藏数
    private int commentCount;       //评论数

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isConcerned() {
        return isConcerned;
    }

    public void setConcerned(boolean concerned) {
        isConcerned = concerned;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
