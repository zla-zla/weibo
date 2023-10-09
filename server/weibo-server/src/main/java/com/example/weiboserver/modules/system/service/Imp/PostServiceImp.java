package com.example.weiboserver.modules.system.service.Imp;

import com.example.weiboserver.modules.system.domain.Post;
import com.example.weiboserver.modules.system.service.Imp.repository.PostDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImp {
    @Autowired
    PostDao postDao;
    //发表帖子
    public void addPost(Post post){
        postDao.save(post);
    }

    //按时间排序分页查询全部结果
    public Page<Post> findAllOrderTime(Integer pageIndex,Integer pageSize){
        //排序
        Sort sort = Sort.by(Sort.Direction.DESC, "time");
        //分页
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        //测试1：分页查询所有用户
        Page<Post> postPage = postDao.findAll(pageable);
        return postPage;
    }

    //根据查询信息返回帖子列表
    public Page<Post> findAllSearch(Integer pageIndex,Integer pageSize,String info){
        //排序
        Sort sort = Sort.by(Sort.Direction.DESC, "time");
        //分页
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        //测试1：分页查询所有用户
        Page<Post> postPage = postDao.findByTextLikeOrNameLike(pageable,info,info);
        return postPage;
    }

    //根据查询信息返回帖子列表
    public Page<Post> findAllByUser(Integer pageIndex,Integer pageSize,Integer info){
        //排序
        Sort sort = Sort.by(Sort.Direction.DESC, "time");
        //分页
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        Page<Post> postPage = postDao.findByUid(pageable,info);
        return postPage;
    }

    public void deleteById(Integer id){
        postDao.deleteById(id);
    }



}
