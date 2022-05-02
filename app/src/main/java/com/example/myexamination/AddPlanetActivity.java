package com.example.myexamination;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myexamination.adapter.PlanetAppearanceAdapter;
import com.example.myexamination.entity.DaoSession;
import com.example.myexamination.entity.Planets;
import com.example.myexamination.entity.PlanetsDao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddPlanetActivity extends Activity implements
        View.OnClickListener, DatePickerDialog.OnDateSetListener {

    //定义Activity退出动画的成员变量
    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;

    private final DaoSession daoSession = MyApplication.daoSession;
    private final PlanetsDao mPlanetDao = daoSession.getPlanetsDao();

    private TextView mTvData;
    private RecyclerView mRecyclerView;
    private EditText mEdtName;
    private EditText mEdtRemark;
    private Button mBtnCreate;
    private final List<Integer> mList = new ArrayList<>();
    private int arg = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_planet);

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
        initData();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        PlanetAppearanceAdapter mAdapter = new PlanetAppearanceAdapter(getApplicationContext(), mList);
        mAdapter.setOnItemClickListener(new PlanetAppearanceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, List<Integer> dataSource) {
                if (position == 0) arg = R.mipmap.planet_1;
                else if (position == 1) arg = R.mipmap.planet_2;
                else if (position == 2) arg = R.mipmap.planet_3;
                else arg = R.mipmap.planet_4;
                Toast.makeText(getApplicationContext(), "您选择了第" + (position + 1) + "个星球", Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mTvData.setOnClickListener(this);
        mBtnCreate.setOnClickListener(this);
    }

    private void initData() {
        mList.add(R.mipmap.planet_1);
        mList.add(R.mipmap.planet_2);
        mList.add(R.mipmap.planet_3);
        mList.add(R.mipmap.planet_4);
    }

    private void initView() {
        mTvData = findViewById(R.id.tv_time);
        mRecyclerView = findViewById(R.id.recyclerView_appearance);
        mEdtName = findViewById(R.id.et_name);
        mEdtRemark = findViewById(R.id.remark);
        mBtnCreate = findViewById(R.id.btn_create);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_time) {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(this, this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        }

        if (view.getId() == R.id.btn_create) {
            Planets planets = new Planets();
            planets.setPlanetName(mEdtName.getText().toString());
            planets.setImageResource(arg);
            planets.setConTime(0);
            planets.setFinishedTime("");
            planets.setCurrentTime(0);
            planets.setIsLighted(false);
            planets.setLightedTime(mTvData.getText().toString());
            planets.setRemark(mEdtRemark.getText().toString());
            mPlanetDao.insert(planets);
            Toast.makeText(AddPlanetActivity.this, "创建成功！", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        @SuppressLint("DefaultLocale")
        String desc = String.format("%d年%d月%d日", year, monthOfYear + 1, dayOfMonth);
        mTvData.setText(desc);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }
}