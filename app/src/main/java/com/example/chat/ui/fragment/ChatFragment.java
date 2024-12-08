package com.example.chat.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.R;
import com.example.chat.adapter.ChatAdapter;
import com.example.chat.bean.PersonChat;
import com.example.chat.ui.activity.ChatActivity;
import com.example.chat.util.MySqliteOpenHelper;
import com.example.chat.util.SPUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 聊天
 */

public class ChatFragment extends Fragment {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;//上下文
    private RecyclerView rvList;
    private ChatAdapter mNewsAdapter;
    private LinearLayout llEmpty;
    private List<PersonChat> personChats;
    private Integer userId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myActivity = (Activity) context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        helper = new MySqliteOpenHelper(myActivity);
        rvList = (RecyclerView) view.findViewById(R.id.rv_news_list);
        llEmpty = view.findViewById(R.id.ll_empty);
        //获取控件
        initView();
        return view;
    }

    /**
     * 初始化页面
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initView() {
        userId = (Integer) SPUtils.get(myActivity, SPUtils.USER_ID, 0);
        LinearLayoutManager layoutManager = new LinearLayoutManager(myActivity);
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //=1.3、设置recyclerView的布局管理器
        rvList.setLayoutManager(layoutManager);
        //==2、实例化适配器
        //=2.1、初始化适配器
        mNewsAdapter = new ChatAdapter();
        //=2.3、设置recyclerView的适配器
        rvList.setAdapter(mNewsAdapter);
        loadData();
        mNewsAdapter.setItemListener(new ChatAdapter.ItemListener() {
            @Override
            public void ItemClick(PersonChat personChat) {
                Intent intent = new Intent(myActivity, ChatActivity.class);
                intent.putExtra("personChat", personChat);
                startActivity(intent);
            }

            @Override
            public void Delete(PersonChat personChat) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadData() {
        personChats = new ArrayList<>();
        PersonChat personChat = null;
        String sql = "select DISTINCT c.fromUserId,c.toUserId from chat c " +
                " where (c.fromUserId =" + userId +" or c.toUserId ="+userId+") ";
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {
                Integer fromUserId = cursor.getInt(0);
                Integer toUserId = cursor.getInt(1);

                if (fromUserId.intValue()==userId.intValue()){//自己发送的
                    Cursor cursor1 = db.rawQuery("select name,photo from user where id =" + fromUserId, null);
                    cursor1.moveToFirst();
                    String fromUserName = cursor1.getString(0);
                    String fromUserPhoto = cursor1.getString(1);

                    Cursor cursor2 = db.rawQuery("select name,photo from user where id =" + toUserId, null);
                    cursor2.moveToFirst();
                    String toUserName = cursor2.getString(0);
                    String toUserPhoto = cursor2.getString(1);

                    Cursor cursor3 = db.rawQuery("select content,date,status from chat " +
                            "where (fromUserId =" + fromUserId + " and toUserId=" + toUserId+") or " +
                            "(fromUserId =" + toUserId + " and fromUserId=" + toUserId+")", null);
                    cursor3.moveToLast();
                    String content = cursor3.getString(0);
                    String date = cursor3.getString(1);
                    Integer status = cursor3.getInt(2);
                    personChat = new PersonChat(fromUserId, fromUserName, fromUserPhoto, toUserId, toUserName, toUserPhoto, content, date, status);
                    personChats.add(personChat);
                }else {//别人发送
                    Cursor cursor1 = db.rawQuery("select name,photo from user where id =" + toUserId, null);
                    cursor1.moveToFirst();
                    String fromUserName = cursor1.getString(0);
                    String fromUserPhoto = cursor1.getString(1);

                    Cursor cursor2 = db.rawQuery("select name,photo from user where id =" + fromUserId, null);
                    cursor2.moveToFirst();
                    String toUserName = cursor2.getString(0);
                    String toUserPhoto = cursor2.getString(1);

                    Cursor cursor3 = db.rawQuery("select content,date,status from chat " +
                            "where (fromUserId =" + fromUserId + " and toUserId=" + toUserId+") or " +
                            "(fromUserId =" + toUserId + " and fromUserId=" + toUserId+")", null);
                    cursor3.moveToLast();
                    String content = cursor3.getString(0);
                    String date = cursor3.getString(1);
                    Integer status = cursor3.getInt(2);
                    personChat = new PersonChat(toUserId, fromUserName, fromUserPhoto, fromUserId, toUserName, toUserPhoto, content, date, status);
                    personChats.add(personChat);
                }

            }
        }
        personChats=personChats.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(PersonChat::getToUserId))), ArrayList::new));
        if (personChats != null && personChats.size() > 0) {
            rvList.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            mNewsAdapter.addItem(personChats);
        } else {
            rvList.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        loadData();//加载数据
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        loadData();
    }
}
