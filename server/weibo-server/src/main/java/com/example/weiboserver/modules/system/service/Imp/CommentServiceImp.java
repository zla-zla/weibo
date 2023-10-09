package com.example.weiboserver.modules.system.service.Imp;

import com.example.weiboserver.modules.system.domain.Comment;
import com.example.weiboserver.modules.system.service.Imp.repository.CommentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImp {
    @Autowired
    CommentDao fatherCommentDao;
    //新增一条评论
    public void addComment(Comment com){
        fatherCommentDao.save(com);
    }

    //删除一条评论
    public void deletaComment(Comment com){
        fatherCommentDao.delete(com);
    }

    //根据pid查找该帖子下的所有父评论
    public Page<Comment> findByPid(Integer pageIndex, Integer pageSize, Integer pid){
        //排序
        Sort sort = Sort.by(Sort.Direction.ASC, "time");
        //分页
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        //测试1：分页查询所有用户
        Page<Comment> commentPage = fatherCommentDao.findByPid(pageable,pid);
        return commentPage;
    }


}
