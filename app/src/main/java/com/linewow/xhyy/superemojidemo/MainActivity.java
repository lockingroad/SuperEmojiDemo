package com.linewow.xhyy.superemojidemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.main_recycler)
    RecyclerView mainRecycler;
    @Bind(R.id.main_scroll)
    BallScrollView mainScroll;
    private List<ChatInfo> infos;
    private MainAdapter mainAdapter;
    private String TAG = "MainActivity";
    private LinearLayoutManager manager;

    private Map<Integer,PointInfo>map;

    private LocationEntity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initList();
    }

    private void initList() {
        map=new HashMap<Integer, PointInfo>();
        infos = new ArrayList<>();
        ChatInfo info1 = new ChatInfo("你的泪光 柔弱中带伤 惨白的月弯弯勾住过往 夜太漫长 夜太漫长", true);
        ChatInfo info2 = new ChatInfo("夜太漫长", true);
        ChatInfo info3 = new ChatInfo("凝结成了霜", false);
        ChatInfo info4 = new ChatInfo("是谁在阁楼上冰冷的绝望", true);
        ChatInfo info5 = new ChatInfo("雨轻轻弹", false);
        ChatInfo info6 = new ChatInfo("朱红色的窗", false);
        ChatInfo info7 = new ChatInfo("我一生在纸上被风吹乱", false);
        ChatInfo info8 = new ChatInfo("梦在远方 化成一缕香 随风飘散你的模样 菊花残 满地伤", true);
        ChatInfo info9 = new ChatInfo("花落人断肠　我心事静静淌", true);
        ChatInfo info10 = new ChatInfo("北风乱　夜未央　你的影子剪不断", true);
        ChatInfo info11 = new ChatInfo("徒留我孤单在湖面　成双", true);
        ChatInfo info12 = new ChatInfo("花　已向晚　飘落了灿烂", true);
        ChatInfo info13 = new ChatInfo("凋谢的世道上　命运不堪", true);
        infos.add(info1);
        infos.add(info2);
        infos.add(info3);
        infos.add(info4);
        infos.add(info5);
        infos.add(info6);
        infos.add(info7);
        infos.add(info8);
        infos.add(info9);
        infos.add(info10);
        infos.add(info11);
        infos.add(info12);
        infos.add(info13);
        mainAdapter.addChatInfos(infos);

    }

    private void initView() {
        manager = new LinearLayoutManager(MainActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mainRecycler.setLayoutManager(manager);
        mainRecycler.setItemAnimator(new DefaultItemAnimator());
        mainAdapter = new MainAdapter(MainActivity.this);
        mainRecycler.setAdapter(mainAdapter);
        mainRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState){
                    case RecyclerView.SCROLL_STATE_DRAGGING:
//                        Log.e(TAG,"SCROLL_STATE_DRAGGING");
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
//                        Log.e(TAG,"SCROLL_STATE_IDLE--");
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
//                        Log.e(TAG,"SCROLL_STATE_SETTLING");
                        break;
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                updatePoints();
                mainScroll.move(-dy);
            }
        });
    }


    public void doClick(View view) {
        initPoints();
    }

    private void initPoints() {
        map.clear();
        int first=manager.findFirstCompletelyVisibleItemPosition();
        int last=manager.findLastCompletelyVisibleItemPosition();//得到暂时可见的
        for(int i=first;i<=last;i++){
            View view=manager.findViewByPosition(i);
            float distance;
            if(i==first){
                distance=0;
            }else{
                View previous=manager.findViewByPosition(i-1);
                distance=view.getY()-previous.getY();
            }
            PointInfo pointInfo=new PointInfo(distance,infos.get(i).isMe);
            map.put(i,pointInfo);
        }
        float startY=manager.findViewByPosition(first).getY();
        entity=new LocationEntity(map,first,startY);//把距离差转换为坐标
        mainScroll.setLocation(entity);
    }
    private void updatePoints(){

        if(mainScroll.getLocationEntity()==null){
            return;
        }
        int first=manager.findFirstCompletelyVisibleItemPosition();
        int last=manager.findLastCompletelyVisibleItemPosition();
        for(int i=first;i<=last;i++){
            if(i>mainScroll.getLocationEntity().firstPosition&&
                    !mainScroll.getLocationEntity().getDistanceMap().containsKey(i)){//只往下获取  上面的点不需要
                View view=manager.findViewByPosition(i);
                View previous=manager.findViewByPosition(i-1);
                float distance=view.getY()-previous.getY();
                PointInfo pointInfo=new PointInfo(distance,infos.get(i).isMe);
                mainScroll.getLocationEntity().updatePoint(i,pointInfo);
            }


        }
    }
}
