package com.hackathon.headsupguys;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackathon.headsupguys.R;
import com.hackathon.headsupguys.OnMsgClickListener;
import com.hackathon.headsupguys.Message;

import java.util.List;


public class MsgListAdapter extends RecyclerView.Adapter<MsgListAdapter.MsgViewHolder> {

    private List<Message> messages;
    private Context context;
    private OnMsgClickListener onMsgClickListener;

    public MsgListAdapter(List<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public MsgViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);
        MsgViewHolder viewHolder = new MsgViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MsgViewHolder msgViewHolder, int i) {
        final Message msg = messages.get(i);
        //Setting text view title
        msgViewHolder.msg_title.setText(msg.getAuthor() + ": " + msg.getTitle());
        msgViewHolder.msg_status.setText(msg.getStatus());
        // bind listener
        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMsgClickListener.onMsgClick(msg);
            }
        };
        msgViewHolder.msg_title.setOnClickListener(l);
    }

    @Override
    public int getItemCount() {
        return (null != messages ? messages.size() : 0);
    }

    public OnMsgClickListener getOnMsgClickListener() {
        return onMsgClickListener;
    }

    public void setOnMsgClickListener(OnMsgClickListener onMsgClickListener) {
        this.onMsgClickListener = onMsgClickListener;
    }

    class MsgViewHolder extends RecyclerView.ViewHolder {
        protected TextView msg_title;
        protected TextView msg_status;

        public MsgViewHolder(View view) {
            super(view);
            this.msg_title = (TextView) view.findViewById(R.id.msg_title);
            this.msg_status = view.findViewById(R.id.msg_status);
        }
    }
}
