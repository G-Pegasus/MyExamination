package com.example.myexamination.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myexamination.MyApplication;
import com.example.myexamination.R;
import com.example.myexamination.dialog.SetTimeDialog;
import com.example.myexamination.entity.DaoSession;
import com.example.myexamination.entity.PlanetsDao;
import com.example.myexamination.service.FrontService;
import com.example.myexamination.view.CountDownProgress;

import java.util.Timer;
import java.util.TimerTask;

public class TimerFragment extends Fragment implements View.OnClickListener {

    private CountDownProgress countDownProgress;

    private ImageButton ibPlanet;
    private TextView mTvPlanetName;
    private TextView mTvMinTime;
    private TextView mTvSecTime;
    private int flag = 0;

    private long mMinute = 10;
    private long mSecond = 0;

    private Timer timer;
    private int conTime;
    private long id = 1L;

    private final DaoSession daoSession = MyApplication.daoSession;
    private final PlanetsDao mPlanetDao = daoSession.getPlanetsDao();

    private final Handler timeHandler = new Handler(Looper.getMainLooper()) {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                computeTime();
                mTvMinTime.setText(mMinute + "");
                mTvSecTime.setText(getTv(mSecond));
                if (mSecond == 0 && mMinute == 0 ) {
                    timer.cancel();
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

        countDownProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SetTimeDialog setTimeDialog = new SetTimeDialog(requireContext());

                setTimeDialog.setOnSureListener(view12 -> {
                    countDownProgress.setCountdownTime(Long.parseLong(setTimeDialog.getEditText()) * 60 * 1000);
                    mTvMinTime.setText(setTimeDialog.getEditText());
                    timer = new Timer();
                    mMinute = Integer.parseInt(mTvMinTime.getText().toString());
                    conTime = (int) mMinute;
                    startRun();
                    flag = 1;
                    countDownProgress.startCountDownTime(new CountDownProgress.OnCountdownFinishListener() {
                        @Override
                        public void countdownFinished() {
                            Toast.makeText(requireActivity(), "倒计时结束了", Toast.LENGTH_SHORT).show();
                            flag = 0;
                            Intent intent = new Intent(requireContext(), FrontService.class);
                            requireActivity().startService(intent);
                        }
                    });
                    setTimeDialog.dismiss();
                });

                setTimeDialog.setOnCancelListener((View.OnClickListener) view1 -> setTimeDialog.dismiss());
                setTimeDialog.show();
            }
        });

        ibPlanet.setOnClickListener(this);
    }

    private void initView() {
        ibPlanet = requireActivity().findViewById(R.id.planet);
        mTvPlanetName = requireActivity().findViewById(R.id.planet_name);
        countDownProgress = requireActivity().findViewById(R.id.countdownProgress);
        mTvMinTime = requireActivity().findViewById(R.id.minute_time);
        mTvSecTime = requireActivity().findViewById(R.id.second_time);
    }

    private String getTv(long l){
        flag = 1;
        if(l >= 10){
            return l + "";
        }else{
            return "0" + l;  //小于10,,前面补位一个"0"
        }
    }

    // 倒计时计算
    private void computeTime() {
        mSecond--;
        if (mSecond < 0) {
            mMinute--;
            mSecond = 59;
            if (mMinute < 0) {
                mMinute = 0;
                mSecond = 0;
            }
        }
    }

    private void startRun() {
        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = Message.obtain();
                message.what = 1;
                timeHandler.sendMessage(message);
            }
        };
        timer.schedule(mTimerTask,0,1000);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.planet) {
            Intent intent = new Intent(requireActivity(), PlanetInfoActivity.class);
            intent.putExtra("conTime", mTvMinTime.getText().toString());
            if (flag == 0)
                startActivityForResult(intent, 0);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent != null && requestCode == 0 && resultCode == Activity.RESULT_OK) {
            id = intent.getLongExtra("planetId", 0L);
            ibPlanet.setImageResource(mPlanetDao.loadByRowId(id).getImageResource());
            mTvPlanetName.setText(mPlanetDao.loadByRowId(id).getPlanetName());
            mPlanetDao.loadByRowId(id).setCurrentTime(Integer.parseInt(mTvMinTime.getText().toString()));
            mPlanetDao.loadByRowId(id).setConTime(conTime);
            mPlanetDao.update(mPlanetDao.loadByRowId(id));
            flag = 1;
        }
    }
}
