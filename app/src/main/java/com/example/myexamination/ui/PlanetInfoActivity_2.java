package com.example.myexamination.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myexamination.MyApplication;
import com.example.myexamination.R;
import com.example.myexamination.dialog.LightedDialog;
import com.example.myexamination.entity.DaoSession;
import com.example.myexamination.entity.PlanetsDao;

public class PlanetInfoActivity_2 extends Activity implements View.OnClickListener {

    //定义Activity退出动画的成员变量
    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;

    private final DaoSession daoSession = MyApplication.daoSession;
    private final PlanetsDao mPlanetDao = daoSession.getPlanetsDao();

    private Button btn_light;
    private Button btn_update;
    private TextView tv_lightTime;
    private TextView tv_conTime;
    private TextView tv_remark;
    private TextView tv_planet_name;
    private ImageView iv_planet_icon;

    private long id = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet_info2);

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

        Intent intent = getIntent();
        id = intent.getLongExtra("planetId", 0L);
        tv_lightTime.setText(mPlanetDao.loadByRowId(id).getLightedTime());
        tv_remark.setText(mPlanetDao.loadByRowId(id).getRemark());
        tv_conTime.setText(String.valueOf(mPlanetDao.loadByRowId(id).getConTime()));
        tv_planet_name.setText(mPlanetDao.loadByRowId(id).getPlanetName());
        iv_planet_icon.setImageResource(mPlanetDao.loadByRowId(id).getImageResource());

        btn_light.setOnClickListener(this);
        btn_update.setOnClickListener(this);
    }

    private void initView() {
        btn_light = findViewById(R.id.btn_light);
        btn_update = findViewById(R.id.btn_update_info2);
        tv_lightTime = findViewById(R.id.tv_lightTime);
        tv_conTime = findViewById(R.id.tv_conTime_info2);
        tv_remark = findViewById(R.id.tv_remark_info2);
        tv_planet_name = findViewById(R.id.tv_planet_name_info2);
        iv_planet_icon = findViewById(R.id.iv_planet_icon_info2);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_light:
                final LightedDialog lightedDialog = new LightedDialog(this);

                lightedDialog.setOnSureListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(PlanetInfoActivity_2.this,
                                "恭喜你又点亮了一颗星！快去你的universe看看吧~", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PlanetInfoActivity_2.this, LightedActivity.class);
                        mPlanetDao.loadByRowId(id).setIsLighted(true);
                        mPlanetDao.update(mPlanetDao.loadByRowId(id));
                        intent.putExtra("planetId", id);
                        startActivity(intent);
                        lightedDialog.dismiss();
                        finish();
                    }
                });
                lightedDialog.setOnCancelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lightedDialog.dismiss();
                    }
                });

                lightedDialog.show();
                break;
            case R.id.btn_update_info2:
                Intent intent = new Intent(PlanetInfoActivity_2.this, UpdateActivity.class);
                intent.putExtra("planetId", id);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }
}