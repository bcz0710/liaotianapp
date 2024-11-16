package com.example.chat.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.chat.R;
import com.example.chat.bean.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 适配器
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
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
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_rv_contacts_list, parent, false);
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
            //获取首字母显示人
            //第一个
            int index = getPositionForName(contacts.getLetter().charAt(0));
            //需要显示字母
            if (index == position) {
                holder.tvLetters.setVisibility(View.VISIBLE);
                holder.tvLetters.setText(contacts.getLetter());
            } else {
                holder.tvLetters.setVisibility(View.GONE);
            }
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

    /**
     * 通过首字母获取该首字母要显示的第一个人的下标
     *
     * @param position
     * @return
     */
    public int getPositionForName(int position) {
        for (int i = 0; i < list.size(); i++) {
            String letter = list.get(i).getLetter();
            //首字母显示
            char firstChar = letter.toUpperCase().charAt(0);
            if (firstChar == position) {
                return i;
            }
        }
        return -1;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView phone;
        private ImageView ivPhoto;
        private TextView tvLetters;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            tvLetters = itemView.findViewById(R.id.tvLetters);
        }
    }

    public interface ItemListener {
        void ItemClick(User user);

        void ItemLongClick(User user);
    }
}
