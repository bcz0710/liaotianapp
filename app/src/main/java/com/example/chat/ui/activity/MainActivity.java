package com.example.chat.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.chat.R;
import com.example.chat.ui.fragment.ChatFragment;
import com.example.chat.ui.fragment.ContactsFragment;
import com.example.chat.ui.fragment.UserFragment;

/**
 * 主页面
 */
public class MainActivity extends AppCompatActivity {
    private Activity myActivity;
    private TextView tvTitle;
    private LinearLayout llContent;
    private RadioButton rbContacts;
    private RadioButton rbMessage;
    private RadioButton rbUser;
    private Fragment[] fragments = new Fragment[]{null, null, null};//存放Fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = this;
        setContentView(R.layout.activity_main);
        tvTitle = (TextView) findViewById(R.id.title);
        llContent = (LinearLayout) findViewById(R.id.ll_main_content);
        rbContacts = (RadioButton) findViewById(R.id.rb_main_contacts);
        rbMessage = (RadioButton) findViewById(R.id.rb_main_message);
        rbUser = (RadioButton) findViewById(R.id.rb_main_user);
        initView();
        setViewListener();
    }

    private void setViewListener() {

        rbMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTitle.setText("消息");
                switchFragment(0);
            }
        });
        rbContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTitle.setText("通讯录");
                switchFragment(1);
            }
        });
        rbUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTitle.setText("我的");
                switchFragment(2);
            }
        });
    }

    private void initView() {
        //设置导航栏图标样式
        Drawable iconMessage = getResources().getDrawable(R.drawable.selector_main_rb_message);//设置主页图标样式
        iconMessage.setBounds(0, 0, 68, 68);//设置图标边距 大小
        rbMessage.setCompoundDrawables(null, iconMessage, null, null);//设置图标位置
        rbMessage.setCompoundDrawablePadding(5);//设置文字与图片的间距
        Drawable iconContacts = getResources().getDrawable(R.drawable.selector_main_rb_contacts);//设置主页图标样式
        iconContacts.setBounds(0, 0, 68, 68);//设置图标边距 大小
        rbContacts.setCompoundDrawables(null, iconContacts, null, null);//设置图标位置
        rbContacts.setCompoundDrawablePadding(5);//设置文字与图片的间距
        Drawable iconUser = getResources().getDrawable(R.drawable.selector_main_rb_user);//设置主页图标样式
        iconUser.setBounds(0, 0, 68, 68);//设置图标边距 大小
        rbUser.setCompoundDrawables(null, iconUser, null, null);//设置图标位置
        rbUser.setCompoundDrawablePadding(5);//设置文字与图片的间距
        switchFragment(0);
        rbMessage.setChecked(true);
    }

    public void add(View view){
        Intent intent = new Intent(myActivity,AddActivity.class);
        startActivity(intent);
    }

    /**
     * 方法 - 切换Fragment
     *
     * @param fragmentIndex 要显示Fragment的索引
     */
    private void switchFragment(int fragmentIndex) {
        //在Activity中显示Fragment
        //1、获取Fragment管理器 FragmentManager
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        //2、开启fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //懒加载 - 如果需要显示的Fragment为null，就new。并添加到Fragment事务中
        if (fragments[fragmentIndex] == null) {
            switch (fragmentIndex) {
                case 0:
                    fragments[fragmentIndex] = new ChatFragment();
                    break;
                case 1:
                    fragments[fragmentIndex] = new ContactsFragment();
                    break;
                case 2:
                    fragments[fragmentIndex] = new UserFragment();
                    break;
            }
            //==添加Fragment对象到Fragment事务中
            //参数：显示Fragment的容器的ID，Fragment对象
            transaction.add(R.id.ll_main_content, fragments[fragmentIndex]);
        }

        //隐藏其他的Fragment
        for (int i = 0; i < fragments.length; i++) {
            if (fragmentIndex != i && fragments[i] != null) {
                //隐藏指定的Fragment
                transaction.hide(fragments[i]);
            }
        }
        //4、显示Fragment
        transaction.show(fragments[fragmentIndex]);

        //5、提交事务
        transaction.commit();
    }

    /**
     * 双击退出
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
        }

        return false;
    }

    private long time = 0;

    public void exit() {
        if (System.currentTimeMillis() - time > 2000) {
            time = System.currentTimeMillis();
            Toast.makeText(myActivity, "再点击一次退出应用程序", Toast.LENGTH_LONG).show();
        } else {
            finish();
        }
    }
}
