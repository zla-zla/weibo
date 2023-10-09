package com.example.weibo.entity;


import static com.example.weibo.entity.CommentEntity.TYPE_COMMENT_PARENT;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;


public class Comment implements MultiItemEntity {
    private Integer id;             //父评论的id
    private Integer pid;            //评论帖子的id
    private Integer uid;            //评论用户的id
    private String profileURL;      //评论者头像的url
    private String name;            //评论者用户名
    private String text;            //评论的内容正文
    private Integer likeCount;          //评论的点赞数
    private Long time;            //评论时间
    private int isLike;             //是否点赞

    //当前评论的总条数（包括这条一级评论）ps:处于未使用状态
    private long totalCount;
    //当前一级评论的位置（下标）
    private int position;
    //当前一级评论所在的位置(下标)
    private int positionCount;

    private List<Reply> replies;
    @Override
    public int getItemType() {
        return TYPE_COMMENT_PARENT;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
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

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
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

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", pid=" + pid +
                ", uid=" + uid +
                ", profileURL='" + profileURL + '\'' +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", likeCount=" + likeCount +
                ", time='" + time + '\'' +
                ", isLike=" + isLike +
                ", totalCount=" + totalCount +
                ", position=" + position +
                ", positionCount=" + positionCount +
                ", replies=" + replies +
                '}';
    }
}
