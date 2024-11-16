package com.example.chat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.chat.R;
import com.example.chat.bean.PersonChat;
import com.example.chat.util.SPUtils;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private List<PersonChat> mMsgList;
    private int userId;
    private RequestOptions headerRO = new RequestOptions().circleCrop();//圆角变换
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
        ImageView iv_left_icon;
        ImageView iv_right_icon;

        ViewHolder(View view) {
            super(view);
            leftLayout = view.findViewById(R.id.left_layout);
            rightLayout = view.findViewById(R.id.right_layout);
            leftMsg = view.findViewById(R.id.left_msg);
            rightMsg = view.findViewById(R.id.right_msg);
            iv_left_icon = view.findViewById(R.id.iv_left_icon);
            iv_right_icon = view.findViewById(R.id.iv_right_icon);
        }
    }

    public MsgAdapter(List<PersonChat> msgList) {
        mMsgList = msgList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item, parent, false);//加载了msg_item布局
        context = parent.getContext();
        userId =(Integer) com.example.chat.util.SPUtils.get(context, SPUtils.USER_ID,0);
        final ViewHolder holder = new ViewHolder(view);
        holder.rightMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();//getAdapterPosition()返回点击数据在Adapter中的位置
                PersonChat msg = mMsgList.get(position);
                Toast.makeText(view.getContext(), "you clicked view" + msg.getContent(), Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PersonChat chat = mMsgList.get(position);
        if (chat.getFromUserId().intValue() == userId) {
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightMsg.setText(chat.getContent());
            Glide.with(holder.itemView.getContext())
                    .asBitmap()
                    .load(chat.getFromUserPhoto())
                    .apply(headerRO.error(R.drawable.ic_default_man))
                    .into(holder.iv_right_icon);
            holder.iv_right_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(chat.getContent());
            Glide.with(holder.itemView.getContext())
                    .asBitmap()
                    .load(chat.getToUserPhoto())
                    .apply(headerRO.error(R.drawable.ic_default_woman))
                    .into(holder.iv_left_icon);
            holder.iv_left_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }
}
