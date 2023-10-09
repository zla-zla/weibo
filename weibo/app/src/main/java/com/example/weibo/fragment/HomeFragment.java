package com.example.weibo.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.weibo.R;
import com.example.weibo.activity.DeliverActivity;
import com.example.weibo.adapter.HomeAdapter;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;


public class HomeFragment extends BaseFragment {
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "关注","推荐"
    };
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;
    private ImageView add;
    public HomeFragment() {

    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        viewPager = mRootView.findViewById(R.id.home_view_pager);
        slidingTabLayout = mRootView.findViewById(R.id.home_sliding_tab);
        add = mRootView.findViewById(R.id.add);
    }

    @Override
    protected void initData() {
        mFragments.add(ConcernedFragment.newInstance("concerned",""));
        mFragments.add(RecommendFragment.newInstance());
        viewPager.setOffscreenPageLimit(mFragments.size());
        viewPager.setAdapter(new HomeAdapter(getFragmentManager(),mTitles,mFragments));
        slidingTabLayout.setViewPager(viewPager);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(DeliverActivity.class);
            }
        });
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}