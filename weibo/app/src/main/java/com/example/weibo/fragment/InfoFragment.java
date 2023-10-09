package com.example.weibo.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weibo.R;
import com.example.weibo.entity.User;

public class InfoFragment extends BaseFragment {

    TextView name;
    TextView intro;
    TextView sex;
    TextView phone;
    User user;
    public InfoFragment() {
    }

    public static InfoFragment newInstance(User user) {
        InfoFragment fragment = new InfoFragment();
        fragment.user = user;
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_info;
    }

    @Override
    protected void initView() {
        name  = mRootView.findViewById(R.id.nickname_info);
        intro  = mRootView.findViewById(R.id.introduction_info);
        sex  = mRootView.findViewById(R.id.sex_info);
        phone  = mRootView.findViewById(R.id.phone_info);
    }

    @Override
    protected void initData() {
        name.setText(user.getName());
        intro.setText(user.getIntroduction());
        sex.setText(user.getSex());
        phone.setText(user.getPhone());
    }
}