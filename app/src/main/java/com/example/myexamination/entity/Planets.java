package com.example.myexamination.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Planets {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    // 星球名称
    private String planetName;

    @NotNull
    // 预期点亮时间
    private String lightedTime;

    // 备注
    private String remark;

    // 点亮时间
    private String finishedTime;

    // 专注时间
    private int conTime;

    // 未点亮前专注时间
    private int currentTime;

    // 图片路径
    private int imageResource;

    @NotNull
    // 是否点亮
    private boolean isLighted;

    @Generated(hash = 1240899364)
    public Planets(Long id, @NotNull String planetName, @NotNull String lightedTime,
            String remark, String finishedTime, int conTime, int currentTime,
            int imageResource, boolean isLighted) {
        this.id = id;
        this.planetName = planetName;
        this.lightedTime = lightedTime;
        this.remark = remark;
        this.finishedTime = finishedTime;
        this.conTime = conTime;
        this.currentTime = currentTime;
        this.imageResource = imageResource;
        this.isLighted = isLighted;
    }

    @Generated(hash = 1551209653)
    public Planets() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlanetName() {
        return this.planetName;
    }

    public void setPlanetName(String planetName) {
        this.planetName = planetName;
    }

    public String getLightedTime() {
        return this.lightedTime;
    }

    public void setLightedTime(String lightedTime) {
        this.lightedTime = lightedTime;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getConTime() {
        return this.conTime;
    }

    public void setConTime(int conTime) {
        this.conTime = conTime;
    }

    public int getImageResource() {
        return this.imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public boolean getIsLighted() {
        return this.isLighted;
    }

    public void setIsLighted(boolean isLighted) {
        this.isLighted = isLighted;
    }

    public String getFinishedTime() {
        return this.finishedTime;
    }

    public void setFinishedTime(String finishedTime) {
        this.finishedTime = finishedTime;
    }

    public int getCurrentTime() {
        return this.currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }
}
