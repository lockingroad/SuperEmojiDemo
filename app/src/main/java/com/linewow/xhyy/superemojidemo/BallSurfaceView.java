package com.linewow.xhyy.superemojidemo;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

/**
 * Created by LXR on 2017/3/20.
 */

public class BallSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

    private float startVx;
    private float startVy=-1200;
    private float currentVx;
    private float currentVy;
    private int g=4000;
    private int currentX=120;
    private int currentY=100;
    private int startX;
    private int startY;
    private List<Point>points;
    private Context context;
    private int currentIndex;
    private double timeX;
    private long timeY;
    private double timeNow;

    private boolean firstCalculate=true;
    private float decayY=0.5f;
    private float decayX=0.1f;
    private Bitmap bitmap;
    private int bitmapHei;
    private static final float MAX_VY=80f;
    private static final float START_VY=-1200;

    private Paint paint;

    private boolean pointFlag=true;
    private String TAG="BallSurfaceView";

    private SurfaceHolder holder;

    private boolean balling;

    private BallSurfaceListener ballSurfaceListener;

    public boolean isBalling() {
        return balling;
    }

    public void setBallSurfaceListener(BallSurfaceListener ballSurfaceListener) {
        this.ballSurfaceListener = ballSurfaceListener;
    }

    public BallSurfaceView(Context context) {
        this(context,null);
    }

    public BallSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public BallSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context,attrs);
    }

    private void init(Context context) {
        holder=getHolder();
        holder.addCallback(this);

        bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        bitmapHei=bitmap.getHeight();
        paint=new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
    }



    public void setPoints(List<Point>points){
        this.points=points;
    }

    public void startMove(){
        timeX=System.nanoTime();
        new Thread(new DrawRunnable()).start();
        balling=true;
        ballSurfaceListener.start();
    }




    public class DrawRunnable implements Runnable{
        @Override
        public void run() {
            while(currentIndex<points.size()-1){
                drawUI();
                Point currentPoint=points.get(currentIndex);
                Point nextPoint=points.get(currentIndex+1);
                if(firstCalculate){//第一次自由落体
                    startX=currentPoint.x;
                    startY=currentPoint.y;
                    if(currentIndex!=0){//虽然是第一次自由落体 但是起点已经不能从points里面选了 而是衰减修正后的值
                        startX=currentX;
                        startY=currentY;
                    }
                    int distanceX=nextPoint.x-startX;
                    int distanceY=nextPoint.y-startY;
                    float singleTime= (float) ((-startVy+Math.sqrt(startVy*startVy+2*g*distanceY))/g);
                    startVx=distanceX/singleTime;
                    currentVx=startVx;
                    float s=singleTime*startVy+g*singleTime*singleTime/2;
                    firstCalculate=false;
                }
                timeNow=System.nanoTime();
                double period1=(timeNow-timeX)/1000/1000/1000;

                currentX= (int) (startX+currentVx*period1);
                currentVy= (float) (startVy+g*period1);
                currentY= (int) (startY+startVy*period1+g*period1*period1/2);

                if(currentY+30>nextPoint.y&&currentVy>0){
                    currentVy=-currentVy*decayY;
                    currentVx=currentVx*decayX;
                    if(Math.abs(currentVy)<MAX_VY){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        timeX=System.nanoTime();

                        if(currentIndex==points.size()-2){
                            //结束了
                           resetBall();
                            break;
                        }
                        currentIndex++;
                        startVy=START_VY;
                        firstCalculate=true;

                    }else{
                        timeX=System.nanoTime();
                        startVy=currentVy;
                        startY=currentY;
                        startX=currentX;
                    }
                }
            }
        }
    }

    private void drawUI() {
        Canvas canvas=holder.lockCanvas();
        try{
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawPaint(paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            canvas.drawBitmap(bitmap,currentX,currentY-bitmapHei,paint);
            if(points!=null){
                for(Point temp:points){
                    canvas.drawCircle(temp.x,temp.y,10,paint);
                }
            }
        }catch (Exception e){
            Log.e(TAG,"出错了");
        }finally{
            holder.unlockCanvasAndPost(canvas);
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    public void resetBall(){
        currentIndex=0;
        firstCalculate=true;


        ballSurfaceListener.end();
        balling=false;
        points=null;
        Canvas canvas=holder.lockCanvas();
        try{
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawPaint(paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        }catch (Exception e){
            Log.e(TAG,"出错了");
        }finally{
            holder.unlockCanvasAndPost(canvas);
        }
    }

}
