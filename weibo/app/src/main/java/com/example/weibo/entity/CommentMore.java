package com.example.weibo.entity;

import static com.example.weibo.entity.CommentEntity.TYPE_COMMENT_MORE;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author ganhuanhui
 * 时间：2019/12/12 0012
 * 描述：更多item
 */
public class CommentMore implements MultiItemEntity {

    private long totalCount;
    private long position;
    private long positionCount;

    @Override
    public int getItemType() {
        return TYPE_COMMENT_MORE;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public long getPositionCount() {
        return positionCount;
    }

    public void setPositionCount(long positionCount) {
        this.positionCount = positionCount;
    }
}
