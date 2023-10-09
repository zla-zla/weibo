package com.example.weibo;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.weibo.Api.Api;
import com.example.weibo.Api.ApiConfig;
import com.example.weibo.Api.TtitCallback;
import com.example.weibo.activity.AppActivity;
import com.example.weibo.activity.BaseActivity;
import com.example.weibo.activity.PhoneActivity;
import com.example.weibo.activity.RegisterActivity;
import com.example.weibo.entity.User;
import com.example.weibo.fragment.ConcernedFragment;
import com.example.weibo.utils.StringUtils;
import com.google.gson.Gson;

import okhttp3.FormBody;

public class MainActivity extends BaseActivity {

    private EditText etAccount;     //账号输入框
    private EditText etPwd;         //密码输入框
    private TextView tvPhone;       //手机验证码登录
    private TextView tvRegister;    //注册新用户
    private Button btnLogin;        //登录按钮
    private ImageView logo;     //logo



    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        etAccount = findViewById(R.id.et_account);
        etPwd = findViewById(R.id.et_pwd);
        tvPhone = findViewById(R.id.tv_phone);
        tvRegister = findViewById(R.id.tv_register);
        btnLogin = findViewById(R.id.btn_login);
        logo=findViewById(R.id.logo);
    }

    @Override
    protected void initData() {
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(PhoneActivity.class);
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(RegisterActivity.class);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etAccount.getText().toString().trim();
                String pwd = etPwd.getText().toString().trim();
                login(account, pwd);
            }
        });
    }


    //输入账号密码，请求后端验证并做对应处理
    private void login(String account, String pwd) {
        //输入有效性验证
        if (StringUtils.isEmpty(account)) {
            showToast("请输入账号");
            return;
        }
        if (StringUtils.isEmpty(pwd)) {
            showToast("请输入密码");
            return;
        }
        //封装请求
        FormBody formBody = new FormBody.Builder()
                .add("account", account)
                .add("pwd", pwd)
                .build();
        Api.config(ApiConfig.LOGIN, formBody).postRequest(this,new TtitCallback() {
            @Override
            public void onSuccess(final String res) {   //不理解，这个res=响应体？
                if(res.equals("NO")){
                    showToastSync("登录失败，请检查账号或密码是否输入错误");
                }
                else{
                    Log.v("login",res);
                    User user = gson.fromJson(res,User.class);
                    Log.v("login",user.getProfileUrl());
                    insertVal("name",user.getName());
                    insertVal("account",user.getAccount());
                    insertVal("pwd",user.getPassword());
                    insertVal("id",String.valueOf(user.getId()));
                    insertVal("concerned_count",String.valueOf(user.getConcernedCount()));
                    insertVal("post_count",String.valueOf(user.getPostCount()));
                    insertVal("fans_count",String.valueOf(user.getFansCount()));
                    insertVal("profile",user.getProfileUrl());
                    insertVal("introduction",user.getIntroduction());
                    insertVal("sex",user.getSex());
                    insertVal("phone",user.getPhone());
                    navigateToWithFlag(AppActivity.class,
                            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    showToastSync("登录成功");//内部创建loop，如果跳转放在这行后面是无法执行的
                }
            }

            @Override
            public void onFailure(Exception e) {
                showToast("当前网络环境不佳，请稍后再试");
            }
        });

    }

}