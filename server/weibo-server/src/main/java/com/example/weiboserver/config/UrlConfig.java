package com.example.weiboserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UrlConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //addResourceHandler是指你想在url请求的路径
        //addResourceLocations是图片存放的真实路径
        registry.addResourceHandler("/post_img/**").addResourceLocations("file:/D:/weibo_resources/post_img/");
        registry.addResourceHandler("/profile/**").addResourceLocations("file:/D:/weibo_resources/profile/");
    }
}
