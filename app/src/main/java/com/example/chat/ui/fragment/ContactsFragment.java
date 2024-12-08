package com.example.chat.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chat.R;
import com.example.chat.adapter.ContactsAdapter;
import com.example.chat.bean.Contacts;
import com.example.chat.bean.PersonChat;
import com.example.chat.bean.User;
import com.example.chat.ui.activity.ChatActivity;
import com.example.chat.util.LettersSorting;
import com.example.chat.util.MySqliteOpenHelper;
import com.example.chat.util.SPUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 首页
 */
public class ContactsFragment extends Fragment {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;
    private RecyclerView rvList;
    private ContactsAdapter contactsAdapter;
    private LinearLayout llEmpty;
    private EditText etQuery;//搜索内容
    private ImageView ivSearch;//搜索图标
    private List<User> userList;
    private Integer userId;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        helper = new MySqliteOpenHelper(myActivity);
        rvList = view.findViewById(R.id.rv_contacts_list);
        llEmpty = view.findViewById(R.id.ll_empty);
        etQuery =view.findViewById(R.id.et_query);
        ivSearch = view.findViewById(R.id.iv_search);
        userId =(Integer) SPUtils.get(myActivity,SPUtils.USER_ID,0);
        initView();
        setViewListener();
        return view;
    }

    private void setViewListener() {
        //软键盘搜索
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();//加载数据
            }
        });
        //点击软键盘中的搜索
        etQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    loadData();//加载数据

                    return true;
                }
                return false;
            }
        });
    }
    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(myActivity);
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //=1.3、设置recyclerView的布局管理器
        rvList.setLayoutManager(layoutManager);
        //=2.1、初始化适配器
        contactsAdapter = new ContactsAdapter();
        //=2.3、设置recyclerView的适配器
        rvList.setAdapter(contactsAdapter);
        contactsAdapter.setItemListener(new ContactsAdapter.ItemListener() {
            @Override
            public void ItemClick(User user) {
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor cursor1 = db.rawQuery("select name,photo from user where id =" + userId, null);
                cursor1.moveToFirst();
                String fromUserName = cursor1.getString(0);
                String fromUserPhoto = cursor1.getString(1);
                PersonChat personChat = new PersonChat(userId, fromUserName, fromUserPhoto, user.getId(), user.getName(), user.getPhoto());
                Intent intent = new Intent(myActivity, ChatActivity.class);
                intent.putExtra("personChat", personChat);
                startActivity(intent);
            }

            @Override
            public void ItemLongClick(User user) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(myActivity);
                dialog.setMessage("确认要删除该联系人吗");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = helper.getWritableDatabase();
                        if (db.isOpen()) {
                            db.execSQL("delete from contacts where userId = " + user.getId()+" and toUserId ="+userId);
                            db.execSQL("delete from contacts where toUserId = " + user.getId()+" and userId ="+userId);
                            db.execSQL("delete from chat where fromUserId = " + user.getId()+" and toUserId ="+userId);
                            db.execSQL("delete from chat where toUserId = " + user.getId()+" and fromUserId ="+userId);
                            db.close();
                        }
                        Toast.makeText(myActivity, "删除成功", Toast.LENGTH_LONG).show();
                        loadData();
                    }
                });
                dialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        loadData();
    }

    private void loadData() {
        userList = new ArrayList<>();
        Integer userId = (Integer) SPUtils.get(myActivity, SPUtils.USER_ID, 0);
        SQLiteDatabase db = helper.getWritableDatabase();
        String content = etQuery.getText().toString();
        Cursor cursor;
        if ("".equals(content)) {
            String sql = "select u.* from user u,contacts c where c.userId = u.id and c.toUserId =" + userId;
            cursor = db.rawQuery(sql, null);
        } else {
            String sql = "select u.* from user u,contacts c where c.userId = u.id and c.toUserId =" + userId
                    + " and account like ?";
            cursor = db.rawQuery(sql, new String[]{"%" + content + "%"});
        }
        if (cursor != null && cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {
                Integer dbId = cursor.getInt(0);
                String dbAccount = cursor.getString(1);
                String dbPassword = cursor.getString(2);
                String dbName = cursor.getString(3);
                String dbSex = cursor.getString(4);
                String dbPhoto = cursor.getString(5);
                String letter = cursor.getString(6);
                User user = new User(dbId, dbAccount, dbPassword, dbName, dbSex, dbPhoto,letter);
                userList.add(user);
            }
        }

        db.close();
        if (userList.size() > 0) {
            //对字母进行排序A-Z #
            Collections.sort(userList, new LettersSorting());
            //把#放在最后
            List<User> list = new ArrayList<>();
            for (User mContact : userList) {
                if ("#".equals(mContact.getLetter())) {
                    list.add(mContact);

                }
            }
            userList.removeAll(list);
            userList.addAll(list);
            contactsAdapter.addItem(userList);
            llEmpty.setVisibility(View.GONE);
            rvList.setVisibility(View.VISIBLE);
        } else {
            llEmpty.setVisibility(View.VISIBLE);
            rvList.setVisibility(View.GONE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
}
