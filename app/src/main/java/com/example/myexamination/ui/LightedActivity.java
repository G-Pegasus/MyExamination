package com.example.myexamination.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myexamination.MyApplication;
import com.example.myexamination.R;
import com.example.myexamination.entity.DaoSession;
import com.example.myexamination.entity.PlanetsDao;

import java.util.Calendar;

public class LightedActivity extends Activity {

    //定义Activity退出动画的成员变量
    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;

    private final DaoSession daoSession = MyApplication.daoSession;
    private final PlanetsDao mPlanetDao = daoSession.getPlanetsDao();

    private ImageView mIvPlanetIcon;
    private TextView mTvPlanetName;
    private TextView mTvLightedDate;
    private TextView mTvConTime;
    private TextView mTvRemark;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lighted);

        TypedArray activityStyle = getTheme().obtainStyledAttributes(new int[] {android.R.attr.windowAnimationStyle});
        int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);
        activityStyle.recycle();
        activityStyle = getTheme().obtainStyledAttributes(windowAnimationStyleResId, new int[] {android.R.attr.activityCloseEnterAnimation, android.R.attr.activityCloseExitAnimation});
        activityCloseEnterAnimation = activityStyle.getResourceId(0, 0);
        activityCloseExitAnimation = activityStyle.getResourceId(1, 0);
        activityStyle.recycle();

        //设置布局在底部
        getWindow().setGravity(Gravity.BOTTOM);
        //设置布局填充满宽度
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(layoutParams);

        initView();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Intent intent = getIntent();
        long id = intent.getLongExtra("planetId", 0L);
        mIvPlanetIcon.setImageResource(mPlanetDao.loadByRowId(id).getImageResource());
        mTvPlanetName.setText(mPlanetDao.loadByRowId(id).getPlanetName());
        mPlanetDao.loadByRowId(id).setFinishedTime(year + "年" + month + "月" + day + "日");
        mPlanetDao.update(mPlanetDao.loadByRowId(id));
        mTvConTime.setText(1 + "min");
        mTvLightedDate.setText(mPlanetDao.loadByRowId(id).getFinishedTime());
        mTvRemark.setText(mPlanetDao.loadByRowId(id).getRemark());
    }

    private void initView() {
        mIvPlanetIcon = findViewById(R.id.iv_planet_icon_lighted);
        mTvPlanetName = findViewById(R.id.tv_planet_name_lighted);
        mTvLightedDate = findViewById(R.id.tv_lightedTime);
        mTvConTime = findViewById(R.id.tv_conTime);
        mTvRemark = findViewById(R.id.tv_remark);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }
}