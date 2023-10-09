package com.example.weibo.entity;

import static com.example.weibo.entity.CommentEntity.TYPE_COMMENT_CHILD;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import lombok.Data;


@Data
public class Reply implements MultiItemEntity {
    private Integer id;             //评论的id
    private Integer fid;            //父评论的id
    private String fname;           //父评论者用户名
    private Integer uid;            //评论用户的id
    private String profileURL;      //评论者头像的url
    private String name;            //评论者用户名
    private String text;            //评论的内容正文
    private Integer likeCount;          //评论的点赞数
    private int isLike;             //是否点赞
    private int isReply;            //本条评论是否为回复
    private Long time;            //评论时间

    //当前评论的总条数（包括这条一级评论）ps:处于未使用状态
    private long totalCount;
    //当前一级评论的位置（下标）
    private int position;
    //当前二级评论所在的位置(下标)
    private int positionCount;
    //当前二级评论所在一级评论条数的位置（下标）
    private int childPosition;

    @Override
    public int getItemType() {
        return TYPE_COMMENT_CHILD;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public int getIsReply() {
        return isReply;
    }

    public void setIsReply(int isReply) {
        this.isReply = isReply;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPositionCount() {
        return positionCount;
    }

    public void setPositionCount(int positionCount) {
        this.positionCount = positionCount;
    }

    public int getChildPosition() {
        return childPosition;
    }

    public void setChildPosition(int childPosition) {
        this.childPosition = childPosition;
    }
}
