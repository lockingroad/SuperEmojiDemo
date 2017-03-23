package com.linewow.xhyy.superemojidemo;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LXR on 2017/3/21.
 */

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ChatInfo> infos = new ArrayList<ChatInfo>();
    private Context context;

    public MainAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View rightView = View.inflate(context, R.layout.item_chat_right, null);
            return new RightHolder(rightView);
        }else {
            View leftView=View.inflate(context,R.layout.item_chat_left,null);
            return new LeftHolder(leftView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ChatInfo info=infos.get(position);
        if(holder instanceof RightHolder){
            ((RightHolder) holder).rightContent.setText(info.content);
        }
        if(holder instanceof LeftHolder){
            ((LeftHolder) holder).leftContent.setText(info.content);
        }
    }


    @Override
    public int getItemCount() {
        return infos.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (infos.get(position).isMe) {
            return 0;
        } else {
            return 1;
        }
    }

    class LeftHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.left_content)
        TextView leftContent;
        public LeftHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    class RightHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.right_content)
        TextView rightContent;
        public RightHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void addChatInfos(List<ChatInfo>addInfo){
        infos.addAll(addInfo);
        notifyDataSetChanged();
    }

}
