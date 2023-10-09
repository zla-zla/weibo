package com.example.weiboserver.modules.system.service.Imp;

import com.example.weiboserver.modules.system.domain.Reply;
import com.example.weiboserver.modules.system.service.Imp.repository.ReplyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyServiceImp {
    @Autowired
    ReplyDao sonCommentDao;

    //新增一条评论
    public void addComment(Reply com){
        sonCommentDao.save(com);
    }

    //删除一条评论
    public void deletaComment(Reply com){
        sonCommentDao.delete(com);
    }

    //根据fid集合查询所有相关的子评论
    public List<Reply> findByFids(List<Integer> fids){
        //排序
        Sort sort = Sort.by(Sort.Direction.ASC, "time");
        //分页
        Pageable pageable = PageRequest.of(0, 100, sort);
        return sonCommentDao.findByFidIn(pageable,fids);
    }

}
