package com.example.weibo.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.weibo.Api.Api;
import com.example.weibo.Api.ApiConfig;
import com.example.weibo.Api.TtitCallback;
import com.example.weibo.R;
import com.example.weibo.entity.User;
import com.example.weibo.fragment.BaseFragment;
import com.example.weibo.fragment.CommentFragment;
import com.example.weibo.fragment.ConcernedFragment;
import com.example.weibo.fragment.InfoFragment;
import com.example.weibo.utils.AppConfig;
import com.example.weibo.view.CircleTransform;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PersonInfoActivity extends BaseActivity {

    ImageView header;
    TextView name;
    TextView concerned;
    TextView fans;
    TextView back;
    private PopupWindow pop;    //选择弹窗
    RefreshHandler refreshHandler = new RefreshHandler();
    User user;
    //tab页相关
    private String[] tabs = {"个人信息", "微博"};
    TabLayout tabLayout;
    ViewPager viewPager;
    private List<BaseFragment> tabFragmentList = new ArrayList<>();

    @Override
    protected int initLayout() {
        return R.layout.activity_person_info;
    }

    @Override
    protected void initView() {
        header = findViewById(R.id.header_image);
        name = findViewById(R.id.tv_nickname);
        concerned = findViewById(R.id.tv_followNum);
        fans = findViewById(R.id.tv_followerNum);
        back = findViewById(R.id.btn_back);
        tabLayout = findViewById(R.id.tablayout_personal);
        viewPager = findViewById(R.id.viewPager_personal);
    }

    @Override
    protected void initData() {
        //返回键
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                intent.putExtra("data","refresh");
                LocalBroadcastManager.getInstance(PersonInfoActivity.this).sendBroadcast(intent);
                sendBroadcast(intent);
                finish();
            }
        });
        Intent intent=getIntent();
        Integer uid=intent.getIntExtra("uid",1);
        Log.v("uid",uid.toString());
        //发送请求获取用户信息
        //封装请求
        FormBody formBody = new FormBody.Builder()
                .add("uid",uid.toString())
                .build();
        Api.config(ApiConfig.USER_INFO, formBody).postRequest(this, new TtitCallback() {
            @Override
            public void onSuccess(final String res) {
                user = gson.fromJson(res,User.class);
                Log.v("res",res);
                Message msg = new Message();
                msg.arg1=2;
                refreshHandler.sendMessage(msg);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

    }

    //弹出选择框popWindow选择拍照/从相册中选/取消，同时处理请求权限
    private void showPop() {
        //生成底部选择框bottomView
        View bottomView = View.inflate(this, R.layout.dialog_deliver_bottom, null);
        //获取相册，相机，取消三个选择的textView
        TextView mAlbum = bottomView.findViewById(R.id.tv_album);
        TextView mCamera = bottomView.findViewById(R.id.tv_camera);
        TextView mCancel = bottomView.findViewById(R.id.tv_cancel);
        //用bottomView创建新的弹窗pop
        pop = new PopupWindow(bottomView, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
//        pop.setAnimationStyle(R.style.main_menu_photo_anim);
        pop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_album:
                        //相册
                        PictureSelector.create(PersonInfoActivity.this)
                                //PictureMimeType：全部ofAll()、图片ofImage()、视频ofVideo()、音频ofAudio()
                                .openGallery(PictureMimeType.ofImage())
                                .maxSelectNum(1)   // 最大图片选择数量 int
                                .minSelectNum(1)        // 最小选择数量 int
                                .imageSpanCount(4)      // 每行显示个数 int
                                .selectionMode(PictureConfig.SINGLE)  //多选/单选：MULTIPLE/SINGLE
                                .forResult(PictureConfig.CHOOSE_REQUEST);   //结果回调onActivityResult code
                        break;
                    case R.id.tv_camera:
                        //请求权限
                        if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            // 权限未被授予
                            System.out.println("permission not granted detected.");
//                            requestCameraPermission();
                        }
                        //拍照
                        PictureSelector.create(PersonInfoActivity.this)
                                .openCamera(PictureMimeType.ofImage())
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.tv_cancel:
                        //取消
                        break;
                }
                closePopupWindow();
            }
        };

        mAlbum.setOnClickListener(clickListener);
        mCamera.setOnClickListener(clickListener);
        mCancel.setOnClickListener(clickListener);
    }

    //关闭选择弹窗
    public void closePopupWindow() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }


    private class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1==1){
                System.out.println("刷新界面");
                Picasso.with(PersonInfoActivity.this).load(ApiConfig.PROFILE+findByKey("profile")).memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE).transform(new CircleTransform()).into(header);
            }
            else{
                name.setText(user.getName());
                Log.v("1",user.getConcernedCount().toString());
                concerned.setText(user.getConcernedCount().toString());
                fans.setText(user.getFansCount().toString());

                Picasso.with(PersonInfoActivity.this).load(ApiConfig.PROFILE+user.getProfileUrl())
                        .transform(new CircleTransform()).into(header);
                header.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPop();
                    }
                });

                //添加tab
                for (int i = 0; i < tabs.length; i++) {
                    tabLayout.addTab(tabLayout.newTab().setText(tabs[1]));
                }
                tabFragmentList.add(InfoFragment.newInstance(user));
                tabFragmentList.add(ConcernedFragment.newInstance("user",String.valueOf(user.getId())));
                viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
                    @NonNull
                    @Override
                    public Fragment getItem(int position) {
                        return tabFragmentList.get(position);
                    }

                    @Override
                    public int getCount() {
                        return tabFragmentList.size();
                    }

                    @Nullable
                    @Override
                    public CharSequence getPageTitle(int position) {
                        return tabs[position];
                    }
                });
                //设置TabLayout和ViewPager联动
                tabLayout.setupWithViewPager(viewPager,false);

            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    images = PictureSelector.obtainMultipleResult(data);
                    LocalMedia image = images.get(0);
                    Log.v("msg","选择图像成功");
                    MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                            .addFormDataPart("account", findByKey("account"));
                    //添加图片资源
                    String path = image.getPath();
                    builder = builder.addFormDataPart("picture", path, RequestBody.create(MediaType.parse("application/octet-stream"),
                            new File(path)));
                    RequestBody reqbody = builder.build();
                    Api.config(ApiConfig.UPDATE_PROFILE,reqbody).postRequest(this, new TtitCallback() {
                        @Override
                        public void onSuccess(String res) {
                            Message msg = new Message();
                            msg.arg1=1;
                            Log.v("info","发送完毕，准备刷新界面");
                            refreshHandler.sendMessage(msg);
                            System.out.println(res);
                        }
                        @Override
                        public void onFailure(Exception e) {
                            showToast("当前网络环境不佳，请稍后再试");
                        }
                    });
                    break;
            }
        }
    }
}