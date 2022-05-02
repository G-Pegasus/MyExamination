package com.example.myexamination.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.myexamination.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class UniverseFragment extends Fragment {
    TabLayout mTableLayout;
    private ViewPager mViewPager;
    private MyAdapter adapter;
    private List<String> mTitle;
    private List<Fragment> mFragment;
    private boolean isGetData = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //绑定一下fragment资源文件以及生成fragment对象
        View view = inflater.inflate(R.layout.fragment_universe, container, false);
        mTableLayout = view.findViewById(R.id.discover_tab);
        mViewPager = view.findViewById(R.id.discover_pager);
        //标题栏数组
        mTitle = new ArrayList<>();
        mTitle.add("universe");
        mTitle.add("待点亮");
        //fragment集合
        mFragment = new ArrayList<>();
        mFragment.add(new FragmentA());
        mFragment.add(new FragmentB());

        adapter = new MyAdapter(getParentFragmentManager());
        mViewPager.setAdapter(adapter);
        //将TabLayout和ViewPager绑定在一起，一个动另一个也会跟着动
        mTableLayout.setupWithViewPager(mViewPager);
        //返回视图
        return view;
    }

    //创建Fragment的适配器
    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        //获得每个页面的下标
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragment.get(position);
        }

        //获得List的大小
        @Override
        public int getCount() {
            return mFragment.size();
        }

        //获取title的下标
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitle.get(position);
        }
    }

    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        isGetData = enter && !isGetData;
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onPause() {
        super.onPause();
        isGetData = true;
    }

    @Override
    public void onResume() {
        if (!isGetData) {
            isGetData = true;
        }
        super.onResume();
    }
}
