package com.example.weibo.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weibo.R;
import com.example.weibo.adapter.HomeAdapter;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

public class RecommendFragment extends Fragment {

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "热门","世界杯","同城","榜单","明星","抗疫","搞笑","情感","社会"
    };
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;

    public RecommendFragment() {
    }

    public static RecommendFragment newInstance() {
        RecommendFragment fragment = new RecommendFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recommend, container, false);
        viewPager = v.findViewById(R.id.recommend_view_pager);
        slidingTabLayout = v.findViewById(R.id.recommend_sliding_tab);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for(String title:mTitles){
            mFragments.add(ConcernedFragment.newInstance("search",title));
        }
        viewPager.setOffscreenPageLimit(mFragments.size());
        viewPager.setAdapter(new HomeAdapter(getFragmentManager(),mTitles,mFragments));
        slidingTabLayout.setViewPager(viewPager);
    }

}