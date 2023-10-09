package com.example.weibo.entity;

//账号密码
public class Login {
    private String account;
    private String pwd;

    public Login(String account, String pwd) {
        this.account = account;
        this.pwd = pwd;
    }

    public String getAccount() {
        return account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
