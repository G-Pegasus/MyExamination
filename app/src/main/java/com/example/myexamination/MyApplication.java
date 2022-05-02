package com.example.myexamination;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.myexamination.entity.DaoMaster;
import com.example.myexamination.entity.DaoSession;
import com.example.myexamination.entity.Planet;
import com.example.myexamination.entity.Planets;
import com.example.myexamination.entity.PlanetsDao;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "planet_info.db");
        SQLiteDatabase db = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

        PlanetsDao mPlanetDao = daoSession.getPlanetsDao();
        //mPlanetDao.deleteAll();
        if (mPlanetDao.loadAll().size() == 0) {
            Planets planets = new Planets();
            planets.setPlanetName("新增星球");
            planets.setImageResource(R.mipmap.add);
            planets.setConTime(0);
            planets.setCurrentTime(0);
            planets.setFinishedTime("");
            planets.setIsLighted(false);
            planets.setLightedTime("");
            planets.setRemark("");
            mPlanetDao.insert(planets);
        }
    }

    public static Context getContext() {
        return context;
    }
}
