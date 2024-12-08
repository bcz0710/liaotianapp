package com.example.chat.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chat.R;
import com.example.chat.adapter.MsgAdapter;
import com.example.chat.bean.PersonChat;
import com.example.chat.util.MySqliteOpenHelper;
import com.example.chat.util.SPUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 聊天
 */
public class ChatActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;
    private TextView tv_nickName;
    private List<PersonChat> chatList = new ArrayList<>();
    private Button btnSend;
    private EditText etMessage;
    private Activity mActivity;
    @SuppressLint("StaticFieldLeak")
    private static ChatActivity instance;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
    private PersonChat personChat;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SQLiteDatabase db;
    private int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        instance = this;
        mActivity = this;
        userId =(Integer) SPUtils.get(mActivity,SPUtils.USER_ID,0);
        initView();
        initData();
        initAdapter();
        initEvent();
        etMessage.requestFocus();
    }

    private void initData() {
        tv_nickName.setText(personChat.getToUserName());
    }

    private String getMessage() {
        return etMessage.getText().toString();
    }

    private void initEvent() {
        /**
         * 发送按钮的点击事件
         */
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendMessage();
            }
        });
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    int length = s.length();
                    if (length > 499) {
                        Toast.makeText(mActivity, "最多输入500个字", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendMessage();
                    return true;
                }
                return false;
            }
        });

    }

    private void sendMessage() {
        hintKeyBoard();
        if (TextUtils.isEmpty(getMessage())) {
            Toast.makeText(mActivity, "发送内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String insertSql = "insert into chat(fromUserId,toUserId,content,date,status) values(?,?,?,?,?)";
        if (userId==personChat.getFromUserId().intValue()){
            db.execSQL(insertSql,new Object[]{personChat.getFromUserId(),personChat.getToUserId(),getMessage(),sf.format(new Date()),0});
        }else {
            db.execSQL(insertSql,new Object[]{personChat.getToUserId(),personChat.getFromUserId(),getMessage(),sf.format(new Date()),0});
            personChat.setFromUserId(personChat.getToUserId());
            personChat.setFromUserName(personChat.getToUserName());
            personChat.setFromUserPhoto(personChat.getToUserPhoto());
            personChat.setToUserId(personChat.getFromUserId());
            personChat.setToUserName(personChat.getFromUserName());
            personChat.setToUserPhoto(personChat.getFromUserPhoto());
        }
        personChat.setDate(sf.format(new Date()));
        personChat.setContent(getMessage());
        personChat.setStatus(0);
        chatList.add(personChat);
        adapter.notifyItemInserted(chatList.size() - 1);//调用适配器的notifyItemInserted()方法,用于通知适配器有新的数据插入
        msgRecyclerView.scrollToPosition(chatList.size() - 1);//将显示的数据定位到最后一行
        etMessage.setText("");
    }

    /**
     * 隐藏软键盘
     */
    public void hintKeyBoard() {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (imm.isActive() && getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (getCurrentFocus().getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }



    private void initAdapter() {
        String sql = "select * from chat c where  (c.fromUserId =" + personChat.getFromUserId() +" and c.toUserId ="+personChat.getToUserId()+") " +
                "or  (c.fromUserId =" + personChat.getToUserId() +" and c.toUserId ="+personChat.getFromUserId()+")";

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {
                Integer fromUserId = cursor.getInt(1);
                Integer toUserId = cursor.getInt(2);
                String content = cursor.getString(3);
                String date = cursor.getString(4);
                Integer status = cursor.getInt(5);

                Cursor cursor1 = db.rawQuery("select name,photo from user where id =" + fromUserId, null);
                cursor1.moveToFirst();
                String fromUserName = cursor1.getString(0);
                String fromUserPhoto = cursor1.getString(1);

                Cursor cursor2 = db.rawQuery("select name,photo from user where id =" + toUserId, null);
                cursor2.moveToFirst();
                String toUserName = cursor2.getString(0);
                String toUserPhoto = cursor2.getString(1);

                personChat = new PersonChat(fromUserId, fromUserName, fromUserPhoto, toUserId, toUserName, toUserPhoto, content, date, status);
                chatList.add(personChat);
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //msgRecyclerView.addItemDecoration(new SpaceItemDecoration(4));//这里设置了RecyclerView子项的间隔
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(chatList);
        msgRecyclerView.setAdapter(adapter);
    }

    private void initView() {
        msgRecyclerView = findViewById(R.id.chat_recyclerView);
        btnSend = findViewById(R.id.btn_chat_message_send);
        etMessage = findViewById(R.id.et_chat_message);
        tv_nickName = findViewById(R.id.tv_nickName);

        personChat =(PersonChat)getIntent().getSerializableExtra("personChat");
        helper = new MySqliteOpenHelper(mActivity);
        db = helper.getWritableDatabase();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    public void back(View view){
        finish();
    }
}
