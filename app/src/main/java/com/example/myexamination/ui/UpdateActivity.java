package com.example.myexamination.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myexamination.MyApplication;
import com.example.myexamination.R;
import com.example.myexamination.dialog.UpdateDialog;
import com.example.myexamination.entity.DaoSession;
import com.example.myexamination.entity.PlanetsDao;

import java.util.Calendar;

public class UpdateActivity extends Activity implements
        View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private Button mBtnUpdate;
    private Button mBtnDelete;
    private EditText mEdtName;
    private EditText mEdtRemark;
    private TextView mTvLightTime;
    private ImageView mIvPlanetIcon;

    //定义Activity退出动画的成员变量
    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;

    private final DaoSession daoSession = MyApplication.daoSession;
    private final PlanetsDao mPlanetDao = daoSession.getPlanetsDao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

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

        mBtnUpdate.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);
        mTvLightTime.setOnClickListener(this);

        Intent intent = getIntent();
        long id = intent.getLongExtra("planetId", 0L);
        mIvPlanetIcon.setImageResource(mPlanetDao.loadByRowId(id).getImageResource());
    }

    private void initView() {
        mBtnUpdate = findViewById(R.id.btn_update);
        mBtnDelete = findViewById(R.id.btn_delete);
        mEdtName = findViewById(R.id.et_name);
        mEdtRemark = findViewById(R.id.remark);
        mTvLightTime = findViewById(R.id.tv_time_update);
        mIvPlanetIcon = findViewById(R.id.iv_planet_icon_up);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_time_update:
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(this, this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                break;
            case R.id.btn_update:
                Intent intent = getIntent();
                long id = intent.getLongExtra("planetId", 0L);
                mPlanetDao.loadByRowId(id).setPlanetName(mEdtName.getText().toString());
                mPlanetDao.loadByRowId(id).setRemark(mEdtRemark.getText().toString());
                mPlanetDao.loadByRowId(id).setLightedTime(mTvLightTime.getText().toString());
                mPlanetDao.update(mPlanetDao.loadByRowId(id));
                Toast.makeText(UpdateActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_delete:
                final UpdateDialog updateDialog = new UpdateDialog(this);

                updateDialog.setOnSureListener(view12 -> {
                    Intent intent1 = getIntent();
                    long id1 = intent1.getLongExtra("planetId", 0L);
                    mPlanetDao.delete(mPlanetDao.loadByRowId(id1));
                    Toast.makeText(UpdateActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    updateDialog.dismiss();
                });

                updateDialog.setOnCancelListener((View.OnClickListener) view1 -> updateDialog.dismiss());
                updateDialog.show();
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        @SuppressLint("DefaultLocale")
        String desc = String.format("%d年%d月%d日", year, monthOfYear + 1, dayOfMonth);
        mTvLightTime.setText(desc);
    }
}