package com.example.weibo.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.weibo.Api.Api;
import com.example.weibo.Api.ApiConfig;
import com.example.weibo.Api.TtitCallback;
import com.example.weibo.R;
import com.example.weibo.adapter.CommentDialogMutiAdapter;
import com.example.weibo.entity.Comment;
import com.example.weibo.entity.CommentEntity;
import com.example.weibo.entity.CommentMore;
import com.example.weibo.entity.Post;
import com.example.weibo.entity.Reply;
import com.example.weibo.fragment.CommentFragment;
import com.example.weibo.utils.AppConfig;
import com.example.weibo.view.CircleTransform;
import com.example.weibo.view.InputTextMsgDialog;
import com.example.weibo.view.RecyclerViewUtil;
import com.example.weibo.view.SoftKeyBoardListener;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jidcoo.android.widget.commentview.CommentView;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class PostDetailActivity extends BaseActivity {
    //帖子内容相关控件
    private Post post;
    private TextView tvName;
    private TextView tvtime;
    private TextView tvtext;
    private TextView tvComment;
    private TextView tvCollect;
    private TextView tvLike;
    private ImageView profile;
    private NineGridView nineGrid;
//    private String content = "我听见你的声音，有种特别的感觉。让我不断想，不敢再忘记你。如果真的有一天，爱情理想会实现，我会加倍努力好好对你，永远不改变";

    //评论内容相关控件
    private List<MultiItemEntity> data = new ArrayList<>(); //用于绑定的数据
    private List<Comment> datas = new ArrayList<>();    //顶级评论列表，请求的数据
    private InputTextMsgDialog inputTextMsgDialog;
    private CommentDialogMutiAdapter commentAdapter;    //adapter
    private RecyclerView rv_dialog_lists;   //循环视图
    private RecyclerViewUtil mRecyclerViewUtil;
    private SoftKeyBoardListener mKeyBoardListener;
    private int offsetY;
    private int positionCount = 0;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                dataSort(0);
                commentAdapter.setNewData(data);
            }

        }
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_post_detail;
    }

    @Override
    protected void initView() {
        //绑定帖子内容相关控件
        tvName = findViewById(R.id.name_detail);
        tvtime = findViewById(R.id.time_detail);
        tvtext = findViewById(R.id.text_detail);
        tvComment = findViewById(R.id.comment_detail);
        tvCollect = findViewById(R.id.collect_detail);
        tvLike = findViewById(R.id.like_detail);
        profile = findViewById(R.id.profile_detail);
        nineGrid = findViewById(R.id.nineGrid_detail);
        mRecyclerViewUtil = new RecyclerViewUtil();

        rv_dialog_lists = findViewById(R.id.dialog_bottomsheet_rv_lists);


//        commentAdapter.setOnLoadMoreListener(this, rv_dialog_lists);

    }

    @Override
    protected void initData() {
        //加载帖子内容
        Intent intent = getIntent();
        post = (Post)intent.getSerializableExtra("post");
        tvName.setText(post.getName());
        tvtime.setText(post.getTime());
        tvtext.setText(post.getText());
        tvComment.setText(String.valueOf(post.getCommentCount()));
        tvCollect.setText(String.valueOf(post.getCollectCount()));
        tvLike.setText(String.valueOf(post.getLikeCount()));
        //加载头像
        Picasso.with(this)
                .load(ApiConfig.PROFILE +post.getProfileURL())
                .transform(new CircleTransform())
                .into(profile);
        //加载九宫格图
        if(!post.getPics().equals("")){
            String[] urls = post.getPics().split(";");
            ArrayList<ImageInfo> imageInfo = new ArrayList<>();
            for(int i=0;i<urls.length;i++){
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(ApiConfig.POST_IMG + urls[i]);
                info.setBigImageUrl(ApiConfig.POST_IMG + urls[i]);
                imageInfo.add(info);
                System.out.println("绑定图片:"+ApiConfig.POST_IMG + urls[i]);
            }
            nineGrid.setAdapter(new NineGridViewClickAdapter(this, imageInfo));
        }

        commentAdapter = new CommentDialogMutiAdapter(data);
        //评论输入框
        RelativeLayout rl_comment = findViewById(R.id.rl_comment);
        rl_comment.setOnClickListener(v -> {//点击后启动输入对话框
            initInputTextMsgDialog(null, false, null, -1);
        });
        rv_dialog_lists.setLayoutManager(new LinearLayoutManager(this));    //设置布局
        rv_dialog_lists.setAdapter(commentAdapter);     //设置adapter
        initListener();

        initRefresh();
    }

    //向服务器请求评论数据
    void getDate(){
        FormBody formBody = new FormBody.Builder()
                .add("pid", String.valueOf(post.getId()))
                .add("pageSize", String.valueOf(40))
                .add("pageIndex", String.valueOf(0))
                .build();
        Api.config(ApiConfig.GET_COMMENTS, formBody).postRequest(this,new TtitCallback() {
            @Override
            public void onSuccess(final String res) {
                System.out.println(res);
                datas = new Gson().fromJson(res, new TypeToken<List<Comment>>() {}.getType());
                System.out.println(datas.toString());
                Message message=handler.obtainMessage();
                message.what=1;
                handler.sendMessage(message);
            }
            @Override
            public void onFailure(Exception e) {
            }
        });
//        int size = 10;
//        for (int i = 1; i < size; i++) {
//            Comment comment = new Comment();
//            comment.setId(i);
//            comment.setPid(post.getId());
//            comment.setUid(Integer.valueOf(findByKey("id")));
//            comment.setText("第" + (i + 1) + "人评论内容" + (i % 3 == 0 ? content + (i + 1) + "次" : ""));
//            comment.setTime("2022.2.27 18:45");
//            comment.setProfileURL("1.png");
//            comment.setIsLike(0);
//            comment.setLikeCount(i);
//            comment.setName(findByKey("name"));
//            comment.setTotalCount(i + size);
//
//            List<Reply> beans = new ArrayList<>();
//            for (int j = 0; j < 10; j++) {
//                Reply reply = new Reply();
//                reply.setFid(comment.getId());
//                reply.setUid(Integer.valueOf(findByKey("id")));
//                reply.setText("一级第" + (i + 1) + "人 二级第" + (j + 1) + "人评论内容" + (j % 3 == 0 ? content + (j + 1) + "次" : ""));
//                reply.setTime("2022.2.27 18:45");
//                reply.setProfileURL("1.png");
//                reply.setIsLike(0);
//                reply.setLikeCount(j);
//                reply.setName(findByKey("name"));
//                reply.setIsReply(j % 5 == 0 ? 1 : 0);
//                reply.setFname(j % 5 == 0 ? "闭嘴家族" + j : "");   //父评论的用户
//                reply.setTotalCount(comment.getTotalCount());
//                beans.add(reply);
//                comment.setReplies(beans);
//                String param = new Gson().toJson(reply);
//                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);
//                Api.config(ApiConfig.POST_REPLY, requestBody).postRequest(this,new TtitCallback() {
//                    @Override
//                    public void onSuccess(final String res) {
//                        System.out.println(res);
//                    }
//                    @Override
//                    public void onFailure(Exception e) {
//                    }
//                });
//            }
//            datas.add(comment);
//            String param = new Gson().toJson(comment);
//            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);
//            Api.config(ApiConfig.POST_COMMENT, requestBody).postRequest(this,new TtitCallback() {
//                @Override
//                public void onSuccess(final String res) {
//                    System.out.println(res);
//                }
//                @Override
//                public void onFailure(Exception e) {
//                }
//            });
//        }
    }

    private void initRefresh() {
        datas.clear();
        getDate();
    }

    //对数据重新进行排列，让一级评论和二级评论同为item
    private void dataSort(int position) {
        //如果数据为空，添加空评论对象
        if (datas.isEmpty()) {
            data.add(new MultiItemEntity() {
                @Override
                public int getItemType() {
                    return CommentEntity.TYPE_COMMENT_EMPTY;
                }
            });
            return;
        }

        if (position <= 0) data.clear();
        int posCount = data.size();//记录位置，赋值给comment的positionCount
        int count = datas.size();//记录评论总数
        for (int i = 0; i < count; i++) {
            if (i < position) continue;

            //先获取一个一级评论
            Comment comment = datas.get(i);
            if (comment == null) continue;
            comment.setPosition(i);//该评论的位置
            posCount += 2;
            //获取该评论的所有回复
            List<Reply> secondLevelBeans = comment.getReplies();
            //子评论为空，添加，跳过
            if (secondLevelBeans == null || secondLevelBeans.isEmpty()) {
                comment.setPositionCount(posCount);//在总data中下条评论插入的位置
                data.add(comment);
                continue;
            }
            int beanSize = secondLevelBeans.size(); //回复数量
            posCount += beanSize;
            comment.setPositionCount(posCount);
            data.add(comment);

            //二级评论
            for (int j = 0; j < beanSize; j++) {
                Reply reply = secondLevelBeans.get(j);
                reply.setChildPosition(j);//一级评论的第j条回复
                reply.setPosition(i);//第i个一级评论
                reply.setPositionCount(posCount);//在总data中下条评论插入的位置
                data.add(reply);
            }

//            //展示更多的item
//            if (beanSize <= 18) {
//                CommentMore moreBean = new CommentMore();
//                moreBean.setPosition(i);
//                moreBean.setPositionCount(posCount);
//                moreBean.setTotalCount(comment.getTotalCount());
//                data.add(moreBean);
//            }

        }
    }

    private void initListener() {
        // 评论的点击事件
        commentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view1, int position) {
                switch ((int) view1.getTag()) {
                    //点击了父级评论
                    case CommentEntity.TYPE_COMMENT_PARENT:
                        //点击了评论，启动键盘进行回复
                        if (view1.getId() == R.id.rl_group) {
                            //添加二级评论
                            PostDetailActivity.this.initInputTextMsgDialog((View) view1.getParent(), false, commentAdapter.getData().get(position), position);
                        }
                        else if (view1.getId() == R.id.ll_like) {   //点赞

                            //一级评论点赞 项目中还得通知服务器 成功才可以修改
                            Comment bean = (Comment) commentAdapter.getData().get(position);
                            bean.setLikeCount(bean.getLikeCount() + (bean.getIsLike() == 0 ? 1 : -1));
                            bean.setIsLike(bean.getIsLike() == 0 ? 1 : 0);
                            datas.set(bean.getPosition(), bean);
                            PostDetailActivity.this.dataSort(0);
                            commentAdapter.notifyDataSetChanged();
                        }
                        break;
                    //点击回复，进行回复
                    case CommentEntity.TYPE_COMMENT_CHILD:
                        if (view1.getId() == R.id.rl_group) {
                            //添加二级评论（回复）
                            PostDetailActivity.this.initInputTextMsgDialog(view1, true, commentAdapter.getData().get(position), position);
                        } else if (view1.getId() == R.id.ll_like) {
                            //二级评论点赞 项目中还得通知服务器 成功才可以修改
                            Reply bean = (Reply) commentAdapter.getData().get(position);
                            bean.setLikeCount(bean.getLikeCount() + (bean.getIsLike() == 0 ? 1 : -1));
                            bean.setIsLike(bean.getIsLike() == 0 ? 1 : 0);

                            List<Reply> secondLevelBeans = datas.get((int) bean.getPosition()).getReplies();
                            secondLevelBeans.set(bean.getChildPosition(), bean);
//                            PostDetailActivity.this.dataSort(0);
                            commentAdapter.notifyDataSetChanged();
                        }

                        break;
                    case CommentEntity.TYPE_COMMENT_MORE:
                        //在项目中是从服务器获取数据，其实就是二级评论分页获取
                        CommentMore moreBean = (CommentMore) commentAdapter.getData().get(position);
                        Reply reply = new Reply();
                        reply.setText("more comment" + 1);
                        reply.setTime(System.currentTimeMillis());
                        reply.setProfileURL("2.png");
                        reply.setId(1);
                        reply.setIsLike(0);
                        reply.setLikeCount(0);
                        reply.setName("星梦缘" + 1);
                        reply.setIsReply(0);
                        reply.setFname("闭嘴家族" + 1);
                        reply.setTotalCount(moreBean.getTotalCount() + 1);

                        datas.get((int) moreBean.getPosition()).getReplies().add(reply);
                        PostDetailActivity.this.dataSort(0);
                        commentAdapter.notifyDataSetChanged();

                        break;
                    case CommentEntity.TYPE_COMMENT_EMPTY:
                        PostDetailActivity.this.initRefresh();
                        break;

                }

            }
        });
        //滚动事件
        if (mRecyclerViewUtil != null) mRecyclerViewUtil.initScrollListener(rv_dialog_lists);

        mKeyBoardListener = new SoftKeyBoardListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
            }

            @Override//软键盘收齐时销毁对话输入框
            public void keyBoardHide(int height) {
                dismissInputDialog();
            }
        });
    }

    //传入组件view，是否为回复，item实体对象，位置
    private void initInputTextMsgDialog(View view, final boolean isReply, final MultiItemEntity item, final int position) {
        dismissInputDialog();
        if (view != null) {
            offsetY = view.getTop();//获取该view的顶部
            scrollLocation(offsetY);//滚动条一道该view的顶部位置
        }
        if (inputTextMsgDialog == null) {
            inputTextMsgDialog = new InputTextMsgDialog(this, R.style.dialog);
            inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                @Override
                public void onTextSend(String msg) {
                    //发送回复
                    if (position >= 0){
                        //添加二级评论(分为回复 "评论" 和回复 "回复" )
                        String replyUserName = "未知";
                        Integer fid=1;
                        int pos = 0;
                        //回复评论/回复，获取回复对象的名字
                        if (item instanceof Comment) {
                            Comment tmp = (Comment) item;
                            replyUserName = tmp.getName();
                            fid = tmp.getId();
                            positionCount = (int) (tmp.getPositionCount() + 1);
                            pos = (int) tmp.getPosition();
                        } else if (item instanceof Reply) {
                            Reply tmp = (Reply) item;
                            replyUserName = tmp.getName();
                            fid = tmp.getFid();
                            positionCount = (int) (tmp.getPositionCount() + 1);
                            pos = (int) tmp.getPosition();
                        }
                        Reply reply = new Reply();
                        reply.setFname(replyUserName);
                        reply.setFid(fid);
                        reply.setUid(Integer.valueOf(findByKey("id")));
                        reply.setIsReply(isReply ? 1 : 0);
                        reply.setText(msg);
                        reply.setProfileURL(findByKey("profile"));
                        reply.setTime(System.currentTimeMillis());
                        reply.setIsLike(0);
                        reply.setName(findByKey("name"));
                        //发送服务器
                        String param = new Gson().toJson(reply);
                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);
                        Api.config(ApiConfig.POST_REPLY, requestBody).postRequest(PostDetailActivity.this,new TtitCallback() {
                            @Override
                            public void onSuccess(final String res) {
                            }
                            @Override
                            public void onFailure(Exception e) {
                            }
                        });
                        //更新ui（找到对应的第几个评论插入进它的回复列表中）
                        datas.get(pos).getReplies().add(reply);
                        PostDetailActivity.this.dataSort(0);
                        commentAdapter.notifyDataSetChanged();
                        rv_dialog_lists.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ((LinearLayoutManager) rv_dialog_lists.getLayoutManager())
                                        .scrollToPositionWithOffset(positionCount >= data.size() - 1 ? data.size() - 1
                                                : positionCount, positionCount >= data.size() - 1 ? Integer.MIN_VALUE : rv_dialog_lists.getHeight());
                            }
                        }, 100);
                    }
                    //发送一级评论
                    else{
                        Comment comment = new Comment();
                        comment.setName(findByKey("name"));//用户名
                        comment.setProfileURL(findByKey("profile"));//头像
                        comment.setTime(System.currentTimeMillis());//发送时间
                        comment.setText(msg);//评论内容
                        comment.setLikeCount(0);//点赞数
                        comment.setPid(post.getId());//帖子id
                        comment.setUid(Integer.valueOf(findByKey("id")));//评论用户id
                        //发送服务器
                        String param = new Gson().toJson(comment);
                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);
                        Api.config(ApiConfig.POST_COMMENT, requestBody).postRequest(PostDetailActivity.this,new TtitCallback() {
                            @Override
                            public void onSuccess(final String res) {
                            }
                            @Override
                            public void onFailure(Exception e) {
                            }
                        });
                        //更新ui
                        datas.add(0, comment);
                        PostDetailActivity.this.dataSort(0);
                        commentAdapter.notifyDataSetChanged();
                        rv_dialog_lists.scrollToPosition(0);
                    }
                }

                @Override
                public void dismiss() {
                    //item滑动到原位
                    scrollLocation(-offsetY);
                }
            });
        }
        showInputTextMsgDialog();
    }


    //关闭输入对话框
    private void dismissInputDialog() {
        if (inputTextMsgDialog != null) {
            if (inputTextMsgDialog.isShowing()) inputTextMsgDialog.dismiss();
            inputTextMsgDialog.cancel();
            inputTextMsgDialog = null;
        }
    }

    //展示输入对话框
    private void showInputTextMsgDialog() {
        inputTextMsgDialog.show();
    }

    //循环列表的滚动条滚到offsetY的位置
    public void scrollLocation(int offsetY) {
        try {
            rv_dialog_lists.smoothScrollBy(0, offsetY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}