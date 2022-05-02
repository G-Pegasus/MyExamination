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

public class LightedInfoActivity extends Activity {

    //定义Activity退出动画的成员变量
    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;

    private final DaoSession daoSession = MyApplication.daoSession;
    private final PlanetsDao mPlanetDao = daoSession.getPlanetsDao();

    private ImageView mIvPlanetIcon;
    private TextView mTvPlanetName;
    private TextView mTvPlanetConTime;
    private TextView mTvPlanetLightedDate;
    private TextView mTvPlanetRemark;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lighted_info);

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
        layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(layoutParams);
        initView();

        Intent intent = getIntent();
        long id = intent.getLongExtra("planetId", 0L);
        mIvPlanetIcon.setImageResource(mPlanetDao.loadByRowId(id).getImageResource());
        mTvPlanetName.setText(mPlanetDao.loadByRowId(id).getPlanetName());
        mTvPlanetLightedDate.setText(mPlanetDao.loadByRowId(id).getFinishedTime());
        mTvPlanetConTime.setText(1 + "min");
        mTvPlanetRemark.setText(mPlanetDao.loadByRowId(id).getRemark());
    }

    private void initView() {
        mIvPlanetIcon = findViewById(R.id.planet_img_lighted);
        mTvPlanetName = findViewById(R.id.planet_name_lighted);
        mTvPlanetConTime = findViewById(R.id.planet_conTime);
        mTvPlanetRemark = findViewById(R.id.planet_remark);
        mTvPlanetLightedDate = findViewById(R.id.planet_lighted_date);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }
}