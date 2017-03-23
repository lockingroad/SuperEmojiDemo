package com.linewow.xhyy.superemojidemo;

/**
 * Created by LXR on 2017/3/22.
 */

public class PointInfo {

    public float distance;
    public boolean isIme;

    public PointInfo(float distance, boolean isIme) {
        this.distance = distance;
        this.isIme = isIme;
    }

    @Override
    public String toString() {
        return "PointInfo{" +
                "distance=" + distance +
                ", isIme=" + isIme +
                '}';
    }
}
