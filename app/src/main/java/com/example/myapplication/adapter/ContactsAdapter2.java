package com.example.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.chat.R;
import com.example.chat.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器
 */
public class ContactsAdapter2 extends RecyclerView.Adapter<ContactsAdapter2.ViewHolder> {
    private List<User> list = new ArrayList<>();
    private Context mActivity;
    private ItemListener mItemListener;
    private RequestOptions headerRO = new RequestOptions().circleCrop();//圆角变换
    public void setItemListener(ItemListener itemListener) {
        this.mItemListener = itemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mActivity = parent.getContext();
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_rv_contacts_list2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User contacts = list.get(position);
        if (contacts != null) {
            holder.name.setText(contacts.getName());
            holder.phone.setText(contacts.getAccount());
            Glide.with(mActivity)
                    .load(contacts.getPhoto())
                    .apply(headerRO.error(R.drawable.ic_default_man))
                    .into(holder.ivPhoto);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemListener != null) {
                        mItemListener.ItemClick(contacts);
                    }
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mItemListener != null) {
                        mItemListener.ItemLongClick(contacts);
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(List<User> listAdd) {
        //如果是加载第一页，需要先清空数据列表
        this.list.clear();
        if (listAdd != null) {
            //添加数据
            this.list.addAll(listAdd);
        }
        //通知RecyclerView进行改变--整体
        notifyDataSetChanged();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView phone;
        private ImageView ivPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
        }
    }

    public interface ItemListener {
        void ItemClick(User user);

        void ItemLongClick(User user);
    }
}
