package com.example.weibo.fragment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.weibo.Api.Api;
import com.example.weibo.Api.ApiConfig;
import com.example.weibo.Api.TtitCallback;
import com.example.weibo.R;
import com.example.weibo.adapter.PostAdapter;
import com.example.weibo.entity.Post;
import com.example.weibo.entity.response.PostListResponse;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

public class ConcernedFragment extends BaseFragment {
    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private List<Post> datas = new ArrayList<>();
    private PostAdapter postAdapter;
    private Integer pageNum = 0;
    private Integer pageSize = 5;
    private LinearLayoutManager linearLayoutManager;
    private String command; //all,search,concerned
    private String info;
    private boolean isSelf=false;

    @Override
    protected int initLayout() {
        return R.layout.fragment_concerned;
    }

    @Override
    protected void initView() {
        recyclerView = mRootView.findViewById(R.id.recycle_view);
        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        getPostList(true);  //获取数据
    }

    @Override
    protected void initData() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        if(command.equals("user")&&info.equals(findByKey("id"))){
            isSelf=true;
        }
        postAdapter = new PostAdapter(getActivity(),datas,isSelf);
        recyclerView.setAdapter(postAdapter);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 0;
                getPostList(true);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                pageNum++;
                getPostList(false);
            }
        });
    }

    public ConcernedFragment(String com,String inf) {
        command = com;
        info = inf;
        Log.v(com,inf);
        if(info.equals("热门")){
            command="all";
        }
    }

    public static ConcernedFragment newInstance(String com,String inf) {

        ConcernedFragment fragment = new ConcernedFragment(com,inf);
        return fragment;
    }

    public void update(String msg){
        info = msg;
        getPostList(true);
        postAdapter.setDatas(datas);
    }

    private void getPostList(final boolean isRefresh) {

        //封装请求
        FormBody formBody = new FormBody.Builder()
                .add("pageIndex", String.valueOf(pageNum))
                .add("pageSize", String.valueOf(pageSize))
                .add("command",command)
                .add("info",info)
                .build();
        Api.config(ApiConfig.POST_LIST, formBody).postRequest(getActivity(), new TtitCallback() {
            @Override
            public void onSuccess(final String res) {
                //调用刷新/加载的动画
                if (isRefresh) {
                    refreshLayout.finishRefresh(true);
                } else {
                    refreshLayout.finishLoadMore(true);
                }
                PostListResponse response = new Gson().fromJson(res, PostListResponse.class);
                System.out.println(res);
                if (response != null) {
                    List<Post> list = response.getContent();
                    if (list != null && list.size() > 0) {
                        if (isRefresh) {
                            datas = list;
                        } else {
                            datas.addAll(list);
                        }
                        mHandler.sendEmptyMessage(0);
                    } else {
                        if (isRefresh) {
                            showToastSync("暂时无数据");
                        } else {
                            showToastSync("没有更多数据");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                if (isRefresh) {
                    refreshLayout.finishRefresh(true);
                } else {
                    refreshLayout.finishLoadMore(true);
                }
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    postAdapter.setDatas(datas);
                    postAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
}