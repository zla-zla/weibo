package com.example.weiboserver.modules.system.domain.Vo;

import com.example.weiboserver.modules.system.domain.Comment;
import com.example.weiboserver.modules.system.domain.Reply;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CommentGroup extends Comment {
    List<Reply> replies;

    public CommentGroup() {
        this.replies = new ArrayList<>();
    }

    public void add(Reply com){
        replies.add(com);
    }
}
