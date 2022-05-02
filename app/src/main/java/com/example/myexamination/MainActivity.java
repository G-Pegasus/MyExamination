package com.example.myexamination;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.myexamination.ui.MeFragment;
import com.example.myexamination.ui.TimerFragment;
import com.example.myexamination.ui.UniverseFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected LinearLayout mMenuTimer;
    protected LinearLayout mMenuUniverse;
    protected LinearLayout mMenuMe;
    protected TimerFragment mTimerFragment = new TimerFragment();
    protected UniverseFragment mUniverseFragment = new UniverseFragment();
    protected MeFragment mMeFragment = new MeFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        // 获取管理类
        this.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container_content, mTimerFragment)
                .add(R.id.container_content, mUniverseFragment)
                .hide(mUniverseFragment)
                .add(R.id.container_content, mMeFragment)
                .hide(mMeFragment)
                // 事物添加 默认：显示首页  其他页面：隐藏
                // 提交
                .commit();
    }

    // 初始化视图
    public void initView() {
        mMenuTimer = this.findViewById(R.id.menu_timer);
        mMenuUniverse = this.findViewById(R.id.menu_universe);
        mMenuMe = this.findViewById(R.id.menu_me);

        mMenuTimer.setOnClickListener(this);
        mMenuUniverse.setOnClickListener(this);
        mMenuMe.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_timer: // 计时器
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .show(mTimerFragment)
                        .hide(mUniverseFragment)
                        .hide(mMeFragment)
                        .commit();
                break;
            case R.id.menu_universe: // 宇宙
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .hide(mTimerFragment)
                        .show(mUniverseFragment)
                        .hide(mMeFragment)
                        .commit();
                break;
            case R.id.menu_me: // 我的
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .hide(mTimerFragment)
                        .hide(mUniverseFragment)
                        .show(mMeFragment)
                        .commit();
                break;
        }
    }
}