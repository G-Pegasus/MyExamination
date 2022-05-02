package com.example.myexamination.ui;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.myexamination.AddPlanetActivity;
import com.example.myexamination.MyApplication;
import com.example.myexamination.R;
import com.example.myexamination.adapter.PlanetAdapter;
import com.example.myexamination.entity.DaoSession;
import com.example.myexamination.entity.Planets;
import com.example.myexamination.entity.PlanetsDao;

import java.util.List;

public class PlanetInfoActivity extends Activity implements View.OnClickListener {

    //定义Activity退出动画的成员变量
    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;

    private final DaoSession daoSession = MyApplication.daoSession;
    private final PlanetsDao mPlanetDao = daoSession.getPlanetsDao();

    private RecyclerView mRecyclerView;
    private Button mBtnChange;
    private TextView mTvPlanetName;
    private TextView mTvExpectedTime;
    private TextView mTvConTime;

    private int positionT = 0;
    private final List<Planets> mList = mPlanetDao.queryBuilder().where(PlanetsDao.Properties.IsLighted.eq("false")).list();

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet_info);
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        PlanetAdapter mAdapter = new PlanetAdapter(this, mList);
        mAdapter.setOnItemClickListener(new PlanetAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, int arg, List<Planets> dataSource) {
                if (arg == 1) {
                    Intent intent = new Intent(PlanetInfoActivity.this, AddPlanetActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    mTvPlanetName.setText(mPlanetDao.loadByRowId(dataSource.get(position).getId()).getPlanetName());
                    mTvExpectedTime.setText(mPlanetDao.loadByRowId(dataSource.get(position).getId()).getLightedTime());
                    mTvConTime.setText(String.valueOf(mPlanetDao.loadByRowId(dataSource.get(position).getId()).getCurrentTime()));
                    positionT = position;
                }
            }
        });

        Intent intent = getIntent();
        mTvConTime.setText(intent.getStringExtra("conTime") + "min");

        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);

        mBtnChange.setOnClickListener(this);
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.rv_planetInfo);
        mBtnChange = findViewById(R.id.btn_change);
        mTvPlanetName = findViewById(R.id.tv_first_name);
        mTvExpectedTime = findViewById(R.id.tv_second_date);
        mTvConTime = findViewById(R.id.tv_third_conTime);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_change) {
            long id = mList.get(positionT).getId();
            Intent intent = new Intent();
            intent.putExtra("planetId", id);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }
}