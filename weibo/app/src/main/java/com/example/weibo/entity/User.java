package com.example.weibo.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("id")
    private Integer id;
    @SerializedName("profileUrl")
    private String profileUrl;
    @SerializedName("introduction")
    private String introduction;
    @SerializedName("name")
    private String name;
    @SerializedName("account")
    private String account;
    @SerializedName("password")
    private String password;
    @SerializedName("sex")
    private String sex;
    @SerializedName("phone")
    private String phone;
    @SerializedName("post_count")
    private Integer postCount;
    @SerializedName("concerned_count")
    private Integer concernedCount;
    @SerializedName("fans_count")
    private Integer fansCount;

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getPostCount() {
        return postCount;
    }

    public void setPostCount(Integer postCount) {
        this.postCount = postCount;
    }

    public Integer getConcernedCount() {
        return concernedCount;
    }

    public void setConcernedCount(Integer concernedCount) {
        this.concernedCount = concernedCount;
    }

    public Integer getFansCount() {
        return fansCount;
    }

    public void setFansCount(Integer fansCount) {
        this.fansCount = fansCount;
    }
}
