package com.example.weibo.Api;

public class ApiConfig {
    public static final int PAGE_SIZE = 5;
    public static final String BASE_URl = "http://10.0.2.2:8000/";
    public final static String POST_IMG = "http://10.0.2.2:8000/post_img/";   //服务器请求地址
    public final static String PROFILE = "http://10.0.2.2:8000/profile/";   //服务器请求地址
    //用户功能
    public static final String LOGIN = "user/login"; //登录
    public static final String REGISTER = "user/register";//注册
    public static final String USER_INFO = "user/info";//请求用户信息
    public static final String UPDATE_PROFILE = "user/updateProfile";//更新用户头像
    public static final String UPDATE_PWD = "user/updatePwd";//更新用户头像
    public static final String UPDATE_INFO = "user/updateInfo";//更新用户头像
    //帖子功能
    public static final String PUBLISH = "post/publish";//发布新帖子
    public static final String POST_LIST = "post/postList";//发布新帖子
    public static final String POST_Delete = "post/delete";//删除帖子
    public static final String POST_COMMENT = "post/add_command";//提交评论
    public static final String POST_REPLY = "post/add_reply";//提交回复
    public static final String GET_COMMENTS = "post/commends";//获取所有评论
}
