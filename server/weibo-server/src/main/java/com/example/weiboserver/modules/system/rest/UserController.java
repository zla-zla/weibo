package com.example.weiboserver.modules.system.rest;

/*用户相关的功能需要完成：
* 用户的登录，注册
* 用户信息的修改
* */

import com.example.weiboserver.modules.system.domain.Vo.Login;
import com.example.weiboserver.modules.system.domain.User;
import com.example.weiboserver.modules.system.service.Imp.UserServiceImp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserServiceImp userService;

    //获取yaml中配置的上传路径属性（存储头像）
    @Value(("${web.upload-profile}"))
    private String uploadPath;


    @ApiOperation("更新用户密码")
    @PostMapping("/updatePwd")
    public void updatePwd(@RequestParam(value = "pwd") String pwd,
                       @RequestParam(value = "uid") Integer uid){
        System.out.println(pwd);
        System.out.println(uid);
        userService.updatePwd(pwd,uid);
    }

    @ApiOperation("更新用户信息")
    @PostMapping("/updateInfo")
    public void updateInfo(@RequestParam(value = "name") String name,
                           @RequestParam(value = "intro") String intro,
                           @RequestParam(value = "sex") String sex,
                           @RequestParam(value = "phone") String phone,
                           @RequestParam(value = "uid") Integer uid){
        userService.updateInfo(name, intro, sex, phone, uid);
        //根据uid查询所有post,reply,comment表将该用户相关的所有评论
    }

    @ApiOperation("注册用户（数据库新增）")
    @PostMapping ("/register")
    public ResponseEntity<Object>  register(@RequestBody User user){
        if(userService.findByAccount(user.getAccount())!=null){
            return new ResponseEntity<>("NO",HttpStatus.OK);
        }
        System.out.println(user);
        user.setProfileUrl(user.getAccount()+".png");
        user.setFans_count(0);
        user.setPost_count(0);
        user.setConcerned_count(0);
        userService.addUser(user);
        return new ResponseEntity<>("OK",HttpStatus.OK);
    }

    @ApiOperation("更新用户头像")
    @PostMapping ("/updateProfile")
    public void updateProfile(@RequestParam(value = "account") String account,
                              @RequestPart(value = "picture") MultipartFile picture) throws IOException {
        String saveUri = uploadPath + account + ".png";
        System.out.println("用户更新了头像");
        File saveFile = new File(saveUri);
        picture.transferTo(saveFile);
    }

    @ApiOperation("删除用户")
    @GetMapping("/delete")
    public void deleteByAccount(String account){
        userService.deleteByAccount(account);
    }

    @ApiOperation("根据id返回用户信息")
    @PostMapping("/info")
    public ResponseEntity<Object> deleteByAccount(@RequestParam(value = "uid") Integer uid){
        User user = userService.findById(uid);
        System.out.println("/user/info");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public ResponseEntity<Object> Login(Login login){
        System.out.println(login);
        if(userService.userLogin(login)){
            User user = userService.findByAccount(login.getAccount());
            return new ResponseEntity<>(user,HttpStatus.OK);
        }
        else return new ResponseEntity<>("NO",HttpStatus.OK);
    }

}
