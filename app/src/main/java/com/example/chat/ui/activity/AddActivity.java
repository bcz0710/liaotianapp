package com.example.chat.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.R;
import com.example.chat.adapter.ContactsAdapter;
import com.example.chat.adapter.ContactsAdapter2;
import com.example.chat.bean.User;
import com.example.chat.util.MySqliteOpenHelper;
import com.example.chat.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加
 */
public class AddActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;
    private EditText etQuery;//搜索内容
    private ImageView ivSearch;//搜索图标
    private LinearLayout llEmpty;
    private RecyclerView rvList;
    private ContactsAdapter2 mContactsAdapter;
    private List<User> userList;
    private Integer userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = this;
        setContentView(R.layout.activity_add);
        helper = new MySqliteOpenHelper(myActivity);
        rvList = findViewById(R.id.rv_contacts_list);
        llEmpty = findViewById(R.id.ll_empty);
        etQuery = findViewById(R.id.et_query);
        ivSearch = findViewById(R.id.iv_search);
        userId = (Integer) SPUtils.get(myActivity, SPUtils.USER_ID, 0);
        initView();
        setViewListener();
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

    public void back(View view) {
       finish();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(myActivity);
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //=1.3、设置recyclerView的布局管理器
        rvList.setLayoutManager(layoutManager);
        //=2.1、初始化适配器
        mContactsAdapter = new ContactsAdapter2();
        //=2.3、设置recyclerView的适配器
        rvList.setAdapter(mContactsAdapter);
        mContactsAdapter.setItemListener(new ContactsAdapter2.ItemListener() {
            @Override
            public void ItemClick(User user) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(myActivity);
                dialog.setMessage("确认要添加该联系人吗");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = helper.getWritableDatabase();
                        String sql = "select * from contacts where userId = "+user.getId()+" and toUserId ="+userId;
                        Cursor cursor = db.rawQuery(sql, null);
                        if (cursor != null && cursor.getColumnCount() > 0) {
                            boolean b = cursor.moveToFirst();
                            if (b){
                                Toast.makeText(AddActivity.this, "你已添加该好友", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        String insertSql = "insert into contacts(userId,toUserId)values(?,?)";
                        db.execSQL(insertSql,new Object[]{user.getId(),userId});
                        db.execSQL(insertSql,new Object[]{userId,user.getId()});
                        Toast.makeText(myActivity, "添加成功", Toast.LENGTH_LONG).show();
                        finish();
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

            @Override
            public void ItemLongClick(User user) {

            }
        });
    }

    private void loadData() {
        userList = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        String content = etQuery.getText().toString();
        if ("".equals(content)) {
            Toast.makeText(myActivity, "请输入账号", Toast.LENGTH_SHORT).show();
            return;
        }
        Cursor cursor;
        String sql = "select * from user where account = ? and id !="+userId;
        cursor = db.rawQuery(sql, new String[]{content});
        if (cursor != null && cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {
                Integer dbId = cursor.getInt(0);
                String dbAccount = cursor.getString(1);
                String dbPassword = cursor.getString(2);
                String dbName = cursor.getString(3);
                String dbSex = cursor.getString(4);
                String dbPhone = cursor.getString(5);
                User mUser = new User(dbId, dbAccount, dbPassword, dbName, dbSex, dbPhone);
                userList.add(mUser);
            }
        }
        db.close();

        if (userList !=null && userList.size()>0){
            rvList.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            mContactsAdapter.addItem(userList);
        }else {
            Toast.makeText(myActivity, "暂无联系人", Toast.LENGTH_SHORT).show();
            rvList.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
        }
    }
}