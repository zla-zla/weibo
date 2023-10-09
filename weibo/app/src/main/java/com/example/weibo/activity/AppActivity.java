package com.example.weibo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.weibo.R;
import com.example.weibo.adapter.MyPagerAdapter;
import com.example.weibo.entity.TabEntity;
import com.example.weibo.fragment.HomeFragment;
import com.example.weibo.fragment.MineFragment;
import com.example.weibo.fragment.SearchFragment;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

public class AppActivity extends BaseActivity {

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = {"首页", "发现", "我的"};
    private int[] mIconUnselectIds = {
            R.drawable.home_unselect, R.drawable.search_unselect,
            R.drawable.mine_unselect};
    private int[] mIconSelectIds = {
            R.drawable.home_select, R.drawable.search_select,
            R.drawable.mine_select};

    private ViewPager viewPager;
    private CommonTabLayout commonTabLayout;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    protected int initLayout() {
        return R.layout.activity_app;
    }

    @Override
    protected void initView() {
        viewPager = findViewById(R.id.view_pager);
        commonTabLayout = findViewById(R.id.common_tab);
    }

    @Override
    protected void initData() {
        mFragments.add(HomeFragment.newInstance());
        mFragments.add(SearchFragment.newInstance());
        mFragments.add(MineFragment.newInstance());
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        commonTabLayout.setTabData(mTabEntities);
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        viewPager.setOffscreenPageLimit(mFragments.size());//启动预加载，防止闪退
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                commonTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mTitles, mFragments));
    }

}