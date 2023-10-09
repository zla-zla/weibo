package com.example.weibo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.weibo.Api.Api;
import com.example.weibo.Api.ApiConfig;
import com.example.weibo.Api.TtitCallback;
import com.example.weibo.MainActivity;
import com.example.weibo.R;
import com.example.weibo.utils.StringUtils;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class ChangePwdActivity extends BaseActivity {
    EditText pwdOld;
    EditText pwdNew;
    EditText pwdNew2;

    @Override
    protected int initLayout() {
        return R.layout.activity_change_pwd;
    }

    @Override
    protected void initView() {
        pwdNew = findViewById(R.id.pwd_new);
        pwdOld = findViewById(R.id.pwd_old);
        pwdNew2 = findViewById(R.id.pwd_new_again);
    }

    @Override
    protected void initData() {
        findViewById(R.id.btn_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd1 = pwdOld.getText().toString().trim();
                String pwd2 = pwdNew.getText().toString().trim();
                String pwd3 = pwdNew2.getText().toString().trim();
                submit(pwd1,pwd2,pwd3);
            }
        });
    }

    public void submit(String pwd1,String pwd2,String pwd3){
        if(StringUtils.isEmpty(pwd1)||StringUtils.isEmpty(pwd2)||StringUtils.isEmpty(pwd3)){
            showToast("请输入完整");
            return;
        }
        if(!pwd1.equals(findByKey("pwd"))){
            showToast("旧密码错误");
            return;
        }
        if(!pwd2.equals(pwd3)){
            showToast("新密码两次填写不一致！");
            return;
        }
        FormBody requestBody = new FormBody.Builder()
                .add("pwd", pwd2)
                .add("uid", findByKey("id"))
                .build();

        Api.config(ApiConfig.UPDATE_PWD, requestBody).postRequest(this,new TtitCallback() {
            @Override
            public void onSuccess(final String res) {
                System.out.println(res);
                navigateTo(MainActivity.class);
                showToastSync("修改成功");

            }
            @Override
            public void onFailure(Exception e) {
            }
        });
    }

}