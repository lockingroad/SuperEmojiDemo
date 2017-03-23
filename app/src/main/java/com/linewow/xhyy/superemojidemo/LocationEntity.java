package com.linewow.xhyy.superemojidemo;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by LXR on 2017/3/22.
 */

public class LocationEntity  {
    public Map<Integer,PointInfo>distanceMap;
    public int firstPosition;
    public float firstY;

    public List<Point>points;

    private String TAG="LocationEntity";

    public LocationEntity(Map<Integer, PointInfo> distanceMap, int firstPosition, float firstY) {
        this.distanceMap = distanceMap;
        this.firstPosition = firstPosition;
        this.firstY = firstY;
        this.points=generatePoints();
    }


    public List<Point> getPoints() {
        return points;
    }

    public Map<Integer, PointInfo> getDistanceMap() {
        return distanceMap;
    }


    public List<Point> generatePoints(){
        List<Point>list=new ArrayList<>();
        List<Integer>indexs=new ArrayList<>(distanceMap.keySet());
        Collections.sort(indexs);
        float y=0;
        for(int i=0;i<indexs.size();i++){
            Point point=new Point();
            PointInfo pointInfo=distanceMap.get(indexs.get(i));
            if(pointInfo.isIme){
                point.x=500;
            }else{
                point.x=100;
            }
            y=y+pointInfo.distance;
            point.y= (int)(firstY+y);
            list.add(point);
        }
        return list;
    }


    public void updatePoint(int i,PointInfo info){
        if(i>firstPosition) {
            distanceMap.put(i, info);

            Point point = new Point();
            Point lastPoint = points.get(points.size() - 1);
            if (info.isIme) {
                point.x = 500;
            } else {
                point.x = 100;
            }
            point.y = (int) (info.distance + lastPoint.y);
            points.add(point);
        }
    }





}
