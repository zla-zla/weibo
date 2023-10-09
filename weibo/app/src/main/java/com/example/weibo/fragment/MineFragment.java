package com.example.weibo.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.weibo.Api.ApiConfig;
import com.example.weibo.MainActivity;
import com.example.weibo.R;
import com.example.weibo.activity.ChangeInfoActivity;
import com.example.weibo.activity.ChangePwdActivity;
import com.example.weibo.activity.DeliverActivity;
import com.example.weibo.activity.PersonInfoActivity;
import com.example.weibo.utils.AppConfig;
import com.example.weibo.view.CircleTransform;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.squareup.picasso.Picasso;

public class MineFragment extends BaseFragment {
    ImageView header;
    TextView post;
    TextView name;
    TextView introduction;
    TextView concerned;
    TextView fans;
    RelativeLayout edit_pwd;
    RelativeLayout edit_info;
    RelativeLayout exit;
    String profileUrl;

    public MineFragment() {
    }

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged( hidden );
        System.out.println("运行onHiddenChanged");
        post.setText(findByKey("post_count"));
        name.setText(findByKey("name"));
        introduction.setText(findByKey("introduction"));
        concerned.setText(findByKey("concerned_count"));
        fans.setText(findByKey("fans_count"));
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("运行onResume");
        post.setText(findByKey("post_count"));
        name.setText(findByKey("name"));
        introduction.setText(findByKey("introduction"));
        concerned.setText(findByKey("concerned_count"));
        fans.setText(findByKey("fans_count"));
    }


    //返回个人页面时刷新一下
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String msg = intent.getStringExtra("data");
                if("refresh".equals(msg)){
                    refresh();
                }
            }
        };
        broadcastManager.registerReceiver(mItemViewListClickReceiver, intentFilter);
    }

    private void refresh() {
        Picasso.with(getActivity()).load(profileUrl)
                .transform(new CircleTransform()).into(header);
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        header = mRootView.findViewById(R.id.img_header);
        post = mRootView.findViewById(R.id.post_count);
        name = mRootView.findViewById(R.id.name_mine);
        introduction = mRootView.findViewById(R.id.introduction);
        concerned = mRootView.findViewById(R.id.concerned_count);
        fans = mRootView.findViewById(R.id.fans_count);
        edit_info = mRootView.findViewById(R.id.edit_info);
        edit_pwd = mRootView.findViewById(R.id.edit_pwd);
        exit = mRootView.findViewById(R.id.exit);
    }

    @Override
    protected void initData() {

        profileUrl = ApiConfig.PROFILE +findByKey("profile");
        post.setText(findByKey("post_count"));
        name.setText(findByKey("name"));
        introduction.setText(findByKey("introduction"));
        concerned.setText(findByKey("concerned_count"));
        fans.setText(findByKey("fans_count"));
        Log.v("url", ApiConfig.PROFILE +findByKey("profile"));
        Picasso.with(getActivity()).load(ApiConfig.PROFILE +findByKey("profile"))
                .transform(new CircleTransform()).into(header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getActivity(), PersonInfoActivity.class);
                intent.putExtra("uid", Integer.valueOf(findByKey("id")));
                getActivity().startActivity(intent);
            }
        });
        edit_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(ChangePwdActivity.class);
            }
        });
        edit_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(ChangeInfoActivity.class);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(MainActivity.class);
            }
        });
    }

    public void getData(){

    }

}