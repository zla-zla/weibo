package com.example.weibo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.weibo.Api.Api;
import com.example.weibo.Api.ApiConfig;
import com.example.weibo.Api.TtitCallback;
import com.example.weibo.MainActivity;
import com.example.weibo.R;
import com.example.weibo.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RegisterActivity extends BaseActivity {
    EditText name_tv;
    EditText account_tv;
    EditText phone_tv;
    EditText intro_tv;
    EditText pwd_tv;
    Button submit;

    @Override
    protected int initLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        name_tv = findViewById(R.id.register_tv_nickName);
        account_tv = findViewById(R.id.register_account);
        phone_tv = findViewById(R.id.register_phone);
        intro_tv = findViewById(R.id.register_intro);
        pwd_tv = findViewById(R.id.register_pwd);
        submit = findViewById(R.id.btn_register);
    }

    @Override
    protected void initData() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = name_tv.getText().toString().trim();
                String account = account_tv.getText().toString().trim();
                String phone = phone_tv.getText().toString().trim();
                String intro = intro_tv.getText().toString().trim();
                String pwd = pwd_tv.getText().toString().trim();
                RadioButton selectedRbSex = findViewById(((RadioGroup)findViewById(R.id.register_rg_sex)).getCheckedRadioButtonId());
                String sex = selectedRbSex.getText().toString();
                try {
                    register(name, phone, intro, sex, account, pwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void register(String name,String phone,String intro,String sex,String account, String pwd) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("name",name);
        json.put("phone",phone);
        json.put("introduction",intro);
        json.put("sex",sex);
        json.put("account",account);
        json.put("password",pwd);
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));

        Api.config(ApiConfig.REGISTER, requestBody).postRequest(this,new TtitCallback() {
            @Override
            public void onSuccess(final String res) {
                if(res.equals("NO")){
                    showToastSync("该账号已注册，请重新申请");
                    return;
                }
                System.out.println(res);
                navigateTo(MainActivity.class);
                showToastSync("注册成功");

            }
            @Override
            public void onFailure(Exception e) {
            }
        });


    }
}