package com.linewow.xhyy.superemojidemo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.List;
import java.util.Map;

/**
 * Created by LXR on 2017/3/21.
 */

public class BallScrollView extends ScrollView {
    private String TAG="BallScrollView";
    private BallSurfaceView ballsurfaceView;
    private LocationEntity locationEntity;

    private List<Point>points;


    public BallScrollView(Context context) {
        this(context,null);
    }

    public BallScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LocationEntity getLocationEntity() {
        return locationEntity;
    }

    public BallScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }


    private void init(Context context) {
        LinearLayout linearLayout=new LinearLayout(context);
        LayoutParams lineLp=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(lineLp);
        ballsurfaceView=new BallSurfaceView(context);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.height=6000;
        ballsurfaceView.setLayoutParams(lp);
        linearLayout.addView(ballsurfaceView);
        addView(linearLayout);
        ballsurfaceView.setZOrderOnTop(true);
        ballsurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);//设置为透明
        ballsurfaceView.setBallSurfaceListener(new BallSurfaceListener() {
            @Override
            public void start() {
                Log.e(TAG,"开始了");
            }

            @Override
            public void end() {
                locationEntity=null;
                handler.sendEmptyMessage(0);
            }
        });
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ballsurfaceView.requestLayout();
        }
    };




    public void setLocation(LocationEntity locationEntity){
        if(ballsurfaceView.isBalling()){
            return;
        }else{
            this.locationEntity=locationEntity;
            this.points=this.locationEntity.getPoints();
//            showPoints(points);
            ballsurfaceView.setPoints(points);
            ballsurfaceView.startMove();
        }
    }


    public void showPoints(List<Point>points){
        for(Point temp:points){
            Log.e(TAG,"x-->"+temp.x+"y-->"+temp.y);
        }
    }

    public void move(int dy){
        if(ballsurfaceView.isBalling()){
            ballsurfaceView.offsetTopAndBottom(dy);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
