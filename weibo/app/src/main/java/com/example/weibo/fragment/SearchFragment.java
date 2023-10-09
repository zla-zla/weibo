package com.example.weibo.fragment;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.weibo.R;
import com.example.weibo.utils.StringUtils;


public class SearchFragment extends BaseFragment {
    private SearchView mSearchView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ConcernedFragment fragment;

    public SearchFragment() {
    }


    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView() {
        mSearchView = (SearchView) mRootView.findViewById(R.id.searchView);
        fragmentManager=getFragmentManager();
        /*FragmentManager要管理fragment（添加，替换以及其他的执行动作）
         *的一系列的事务变化，需要通过fragmentTransaction来操作执行
         */
        fragmentTransaction = fragmentManager.beginTransaction();
        //实例化要管理的fragment
        fragment = new ConcernedFragment("search","");
        //通过添加（事务处理的方式）将fragment加到对应的布局中
        fragmentTransaction.add(R.id.fragment, fragment);
        //事务处理完需要提交
        fragmentTransaction.commit();
    }

    @Override
    protected void initData() {
        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (StringUtils.isEmpty(query)){
                    Log.v("第一种",query);
                    fragment.update(query);
                }else{
                    Log.v("第一种",query);
                    fragment.update(query);
                }
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (StringUtils.isEmpty(newText)){
                    Log.v("第二种",newText);
                }else{
                    Log.v("第二种",newText);
                }
                return false;
            }
        });
    }


}