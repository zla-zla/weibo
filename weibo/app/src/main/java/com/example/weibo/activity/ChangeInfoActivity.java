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

public class ChangeInfoActivity extends BaseActivity {
    EditText et1;
    EditText et2;
    EditText et3;
    EditText et4;

    @Override
    protected int initLayout() {
        return R.layout.activity_change_info;
    }

    @Override
    protected void initView() {
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);

    }

    @Override
    protected void initData() {
        findViewById(R.id.btn_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.info_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info1 = et1.getText().toString().trim();
                String info2 = et2.getText().toString().trim();
                String info3 = et3.getText().toString().trim();
                String info4 = et4.getText().toString().trim();
                submit(info1,info2,info3,info4);
            }
        });
    }

    public void submit(String info1,String info2,String info3,String info4){
        if(StringUtils.isEmpty(info1)||StringUtils.isEmpty(info2)||StringUtils.isEmpty(info3)||StringUtils.isEmpty(info4)){
            showToast("请输入完整");
            return;
        }
        FormBody requestBody = new FormBody.Builder()
                .add("name", info1)
                .add("intro", info2)
                .add("sex", info3)
                .add("phone", info4)
                .add("uid", findByKey("id"))
                .build();

        Api.config(ApiConfig.UPDATE_INFO, requestBody).postRequest(this,new TtitCallback() {
            @Override
            public void onSuccess(final String res) {
                System.out.println(res);
                insertVal("name",info1);
                insertVal("introduction",info2);
                insertVal("sex",info3);
                insertVal("phone",info4);
                finish();
                showToastSync("修改成功");

            }
            @Override
            public void onFailure(Exception e) {
            }
        });
    }
}