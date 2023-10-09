package com.example.weibo.utils;

public class AppConfig {
    public final static String server_addr = "http://10.0.2.2:8000/";   //服务器请求地址
    public final static String post_list = "http://10.0.2.2:8000/post/postList";   //帖子

    public final static int port = 443;
    public final static String defaultImg = "default.jpg";
    public static String myProfile = "";  //用户本人的头像名
    public static int myId = -1;    //用户本人的id
    private static String sessionId = "H6gIffhWb1iwXAa8";

    static public void setSessionId(String s) {
        sessionId = s;
    }

    static public String getSessionId() {
        return sessionId;
    }
}
