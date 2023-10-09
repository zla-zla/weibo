package com.example.weibo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.weibo.Api.Api;
import com.example.weibo.Api.ApiConfig;
import com.example.weibo.Api.TtitCallback;
import com.example.weibo.R;
import com.example.weibo.adapter.GridImageAdapter;
import com.example.weibo.adapter.HomeAdapter;
import com.example.weibo.entity.FullyGridLayoutManager;
import com.example.weibo.utils.AppConfig;
import com.example.weibo.utils.toast;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.roger.catloadinglibrary.CatLoadingView;

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

public class DeliverActivity extends BaseActivity {
    private int maxSelectNum = 20;   //最多允许上传6张
    private List<LocalMedia> selectList = new ArrayList<>();    //要发送的图像集合
    private GridImageAdapter adapter;   //九宫格图片适配器
    private RecyclerView mRecyclerView; //循环列表布局
    private TextView cancel;    // 取消按键
    private Button publish;     //发布微博按键
    private EditText text;      //编辑文本的输入文本框
    private PopupWindow pop;    //弹窗
//    CatLoadingView loadingView = new CatLoadingView();  //加载弹窗
    Handler mHandler = new deliverHandler();

    private int REQUEST_CAMERA_CODE = 1;

    @Override
    protected int initLayout() {
        return R.layout.activity_deliver;
    }

    @Override
    protected void initView() {
        //设置标题栏并去掉默认的标题
        Toolbar toolbar = findViewById(R.id.top_bar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        //绑定控件
        cancel = findViewById(R.id.deliver_return); //取消按键
        publish = findViewById(R.id.publish);       //发布按键
        text = findViewById(R.id.deliver_text);     //文本框
        mRecyclerView = findViewById(R.id.deliver_recycler_img);    //循环列表布局
    }

    @Override
    protected void initData() {
        //绑定返回点击事件
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //发布帖子
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: 向服务器发送
                publishBlog();
            }
        });
        //猫咪loading不能点击取消，只能人为取消
//        loadingView.setCancelable(false);
        //初始化上传图片区域
        initImageWidget();
    }

    //发布微博帖子
    private void publishBlog() {
        String content = text.getText().toString();     //获取帖子的文本
        List<LocalMedia> mediaList = adapter.getMediaList();    //获取需要发送的图片
        //内容判空
        if (content.equals("")) {
            toast.showToastInCenter(getApplicationContext(), "帖子内容不能为空！", toast.TOAST_UI_QUEUE);
            return ;
        }
        //封装请求
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("content", content);
        builder.addFormDataPart("uid", findByKey("id"));
        //添加图片资源
        for (int i = 0; i < mediaList.size(); i++) {
            String path = mediaList.get(i).getPath();   //获取第i张图片的路径
            builder = builder.addFormDataPart("pictures", path,
                    RequestBody.create(MediaType.parse("application/octet-stream"), new File(path)));
        }
        //创建requestBody
        RequestBody reqbody = builder.build();
        Api.config(ApiConfig.PUBLISH,reqbody).postRequest(DeliverActivity.this, new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                Log.v("response:",res);
                //通知UI线程
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("result", "ok");
                msg.setData(data);
                mHandler.sendMessage(msg);
            }
            @Override
            public void onFailure(Exception e) {
                showToast("当前网络环境不佳，请稍后再试");
            }
        });
    }

    //初始化上传图片区域
    private void initImageWidget() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "../.../", selectList);
                            PictureSelector.create(DeliverActivity.this).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(DeliverActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(DeliverActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });
    }

    //点击添加图标后弹出选择框popWindow
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            //点击加号时，取消editText的焦点，软键盘落下
            text.clearFocus();
            showPop();
        }
    };

    //弹出选择框popWindow选择拍照/从相册中选/取消，同时处理请求权限
    private void showPop() {
        //生成底部选择框bottomView
        View bottomView = View.inflate(DeliverActivity.this, R.layout.dialog_deliver_bottom, null);
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
                        PictureSelector.create(DeliverActivity.this)
                                //PictureMimeType：全部ofAll()、图片ofImage()、视频ofVideo()、音频ofAudio()
                                .openGallery(PictureMimeType.ofImage())
                                .maxSelectNum(adapter.getRestSelectNum())   // 最大图片选择数量 int
                                .minSelectNum(1)        // 最小选择数量 int
                                .imageSpanCount(4)      // 每行显示个数 int
                                .selectionMode(PictureConfig.MULTIPLE)  //多选/单选：MULTIPLE/SINGLE
                                .forResult(PictureConfig.CHOOSE_REQUEST);   //结果回调onActivityResult code
                        break;
                    case R.id.tv_camera:
                        //请求权限
                        if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            // 权限未被授予
                            System.out.println("permission not granted detected.");
                        }
                        //拍照
                        PictureSelector.create(DeliverActivity.this)
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调

                    images = PictureSelector.obtainMultipleResult(data);
                    selectList.addAll(images);

//                    selectList = PictureSelector.obtainMultipleResult(data);
                    // LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    //关闭选择对话框
    public void closePopupWindow() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }

    //发布结果通信，控制关闭loading窗
    private class deliverHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            //获取返回的结果
            String result = data.getString("result");
            //发布成功弹出成功弹窗，关闭加载框
            if(result.equals("ok")) {
//                loadingView.dismiss();
                toast.showToastInCenter(getApplicationContext(), "发布成功！", toast.TOAST_UI_QUEUE);
                finish();
            }
            //发布成功弹出成功弹窗，关闭加载框
            else {
                String error = data.getString("log", "发布失败！");
//                loadingView.dismiss();
                toast.showToastInCenter(getApplicationContext(), error, toast.TOAST_UI_QUEUE);
            }
        }
    }

}