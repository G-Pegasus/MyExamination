package com.example.myexamination.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.myexamination.R;
import com.example.myexamination.ui.TimerFragment;

public class FrontService extends Service {
    public FrontService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("info","前台服务  onCreate");

        // 初始化
        NotificationManager messageNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel("MyService", "前台Service通知", NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true); //是否在桌面icon右上角展示小红点
        channel.setLightColor(Color.GREEN);//小红点颜色
        channel.setShowBadge(false); //是否在久按桌面图标时显示此渠道的通知
        messageNotificationManager.createNotificationChannel(channel);

        Intent notificationIntent = new Intent(this, TimerFragment.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyService");
        builder.setSmallIcon(R.mipmap.planet);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.planet));
        builder.setContentTitle("时间到了哟~");
        builder.setContentText("快去休息吧！");
        builder.setWhen(System.currentTimeMillis());
        builder.setChannelId("MyService");

        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent pt = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentIntent(pt);
        Notification notification = builder.build();

        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}