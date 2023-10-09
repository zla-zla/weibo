package com.example.weiboserver.modules.system.rest;

import com.example.weiboserver.modules.system.domain.Comment;
import com.example.weiboserver.modules.system.domain.Post;
import com.example.weiboserver.modules.system.domain.Reply;
import com.example.weiboserver.modules.system.domain.User;
import com.example.weiboserver.modules.system.domain.Vo.CommentGroup;
import com.example.weiboserver.modules.system.service.Imp.CommentServiceImp;
import com.example.weiboserver.modules.system.service.Imp.PostServiceImp;
import com.example.weiboserver.modules.system.service.Imp.ReplyServiceImp;
import com.example.weiboserver.modules.system.service.Imp.UserServiceImp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

//http://localhost:8000/swagger-ui/index.html
@Slf4j
@Api(tags = "微博帖子管理")
@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    PostServiceImp postService;
    @Autowired
    ReplyServiceImp replyServiceImp;
    @Autowired
    CommentServiceImp commentServiceImp;
    @Autowired
    UserServiceImp userService;

    //获取yaml中配置的上传路径属性
    @Value(("${web.upload-postimg}"))
    private String uploadPath;
    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @ApiOperation("发布新帖子")
    @PostMapping("/publish")
    public ResponseEntity<Object> update(@RequestParam(value = "content") String content,
                                         @RequestParam(value = "uid") String uid,
                                         @RequestParam(value = "pictures", required = false) MultipartFile[] pictures) {
        String picsUrl = "";     //所有图片的保存路径
        boolean uploadSuccess = true;
        String time = sdf.format(new Timestamp(System.currentTimeMillis()));    //获取当前时间
        if (pictures != null) {
            //存储每张图片的名字
            List<String> imageNames = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            for (int i =0;i<pictures.length;i++) {
                MultipartFile img = pictures[i];
                Long timeStamp = System.currentTimeMillis();
                String saveUri=uploadPath+uid+timeStamp+i+".png";        //拼接保存图片的真实地址:uid+时间戳+序号+".png"
                imageNames.add(uid+timeStamp+i+".png");
                log.info("保存一张图片到地址："+saveUri);
                File saveFile = new File(saveUri);
                try {
                    img.transferTo(saveFile);  //保存文件到真实存储路径下
                } catch (IOException e) {
                    uploadSuccess = false;
                    e.printStackTrace();
                }
            }
            for (String name : imageNames) {
                sb.append(name).append(";");
            }
            picsUrl = sb.toString();    //所有图片的保存路径
        }
        if(uploadSuccess){
            Post post = new Post();
            Integer id = Integer.valueOf(uid);
            User user = userService.findById(id);
            //新建帖子对象
            post.setName(user.getName());
            post.setUid(user.getId());
            post.setProfileURL(user.getProfileUrl());
            post.setPics(picsUrl);
            post.setText(content);
            post.setTime(time);
            post.setCollectCount(0);
            post.setCommentCount(0);
            post.setLikeCount(0);
            postService.addPost(post);
            return new ResponseEntity<>("OK", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("NO",HttpStatus.NO_CONTENT);
        }
    }


    @ApiOperation("请求帖子列表")
    @PostMapping("/postList")
    public ResponseEntity<Object> postList(@RequestParam(value = "pageIndex") String pageIndex,
                                           @RequestParam(value = "command") String command,
                                           @RequestParam(value = "info") String info,
                                           @RequestParam(value = "pageSize") String pageSize){
        System.out.println(command+":"+info);
        Page<Post> postPage;
        if(command.equals("all")){     //查询所有帖子
            postPage = postService.findAllOrderTime(Integer.valueOf(pageIndex),Integer.valueOf(pageSize));
        } else if (command.equals("concerned")) {       //查询关注博主的帖子
            postPage = postService.findAllOrderTime(Integer.valueOf(pageIndex),Integer.valueOf(pageSize));
        } else if (command.equals("user")) {        //查询某个用户的所有帖子
            postPage = postService.findAllByUser(Integer.valueOf(pageIndex),Integer.valueOf(pageSize),Integer.valueOf(info));
        }
        else {       //查询搜索的帖子
            postPage = postService.findAllSearch(Integer.valueOf(pageIndex),Integer.valueOf(pageSize),"%"+info+"%");
        }
        for(Post post:postPage){
            post.setName(userService.findById(post.getUid()).getName());
        }
        return new ResponseEntity<>(postPage, HttpStatus.OK);
    }

    @ApiOperation("删除帖子")
    @PostMapping("/delete")
    public void delete(@RequestParam(value = "pid") Integer pid){
        postService.deleteById(pid);
    }

    @ApiOperation("帖子点赞")
    @PostMapping("/like")
    public void addLike(){

    }

    @ApiOperation("帖子转发")
    @PostMapping("/forward")
    public void addForward(){

    }

    @ApiOperation("添加某帖子的父评论")
    @PostMapping("/add_command")
    public ResponseEntity<String> addCommend(@RequestBody Comment com){
        System.out.println("添加评论");
        System.out.println(com.getText());
        commentServiceImp.addComment(com);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @ApiOperation("添加某帖子的子评论")
    @PostMapping("/add_reply")
    public ResponseEntity<String> addReply(@RequestBody Reply com){
        System.out.println("添加回复");
        System.out.println(com.getText());
        replyServiceImp.addComment(com);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @ApiOperation("某帖子下的评论")
    @PostMapping("/commends")
    public ResponseEntity<Collection> commends(@RequestParam(value = "pageIndex") Integer pageIndex,
                           @RequestParam(value = "pageSize") Integer pageSize,
                           @RequestParam(value = "pid") Integer pid){
        Page<Comment> set1 = commentServiceImp.findByPid(pageIndex,pageSize,pid);
        List<Integer> fids = new ArrayList<>();
        Map<Integer,CommentGroup> map = new HashMap<>();
        for(Comment com:set1){
            com.setName(userService.findById(com.getUid()).getName());
            fids.add(com.getId());
            CommentGroup commentGroup = new CommentGroup();
            BeanUtils.copyProperties(com, commentGroup);    //将父类对象的属性逐一赋值给子类
            map.put(com.getId(),commentGroup);
        }
        List<Reply> set2 = replyServiceImp.findByFids(fids);

        for(Reply com:set2){
//            com.setFname(userService.findById(com.getFid()).getName());
            map.get(com.getFid()).add(com);
        }
        Collection<CommentGroup> res = map.values();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


}
