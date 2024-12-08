package com.example.chat.ui.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chat.R;
import com.example.chat.util.MySqliteOpenHelper;
import com.example.chat.util.SPUtils;
import com.example.chat.util.Trans2PinYin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


/**
 * 欢迎页
 */
public class OpenActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Button in;
    private Boolean isFirst;
    private Integer userId;
    private Integer userType;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //字母数组,#代表未知，比如数字开头
    private String[] strChars = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        helper = new MySqliteOpenHelper(this);
        in = findViewById(R.id.in);
        userId = (Integer) SPUtils.get(OpenActivity.this, SPUtils.USER_ID, 0);
        userType = (Integer) SPUtils.get(OpenActivity.this, SPUtils.USER_TYPE, 0);
        CountDownTimer timer = new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                in.setText(millisUntilFinished / 1000 + "秒");
            }
            public void onFinish() {
                finishView();
            }
        };
        timer.start();
        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishView();
                timer.cancel();
                finish();
            }
        });
    }

    private void finishView() {
        isFirst = (Boolean) SPUtils.get(OpenActivity.this, SPUtils.IF_FIRST, true);
        if (isFirst){//第一次进来  初始化本地数据
            SQLiteDatabase db = helper.getWritableDatabase();
            SPUtils.put(OpenActivity.this,SPUtils.IF_FIRST,false);//第一次
            //初始化数据
            //获取json数据
            String rewardJson = "";
            String rewardJsonLine;
            //assets文件夹下db.json文件的路径->打开db.json文件
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(OpenActivity.this.getAssets().open("db.json")));
                while (true) {
                    if (!((rewardJsonLine = bufferedReader.readLine()) != null)) break;
                    rewardJson += rewardJsonLine;
                }
                JSONObject jsonObject = new JSONObject(rewardJson);
                JSONArray userList = jsonObject.getJSONArray("user");//
                for (int i = 0, length = userList.length(); i < length; i++) {//
                    JSONObject o = userList.getJSONObject(i);
                    String account = o.getString("account");
                    String password = o.getString("password");
                    String name = o.getString("name");
                    String sex = o.getString("sex");
                    String photo = o.getString("photo");
                    //转换拼音截取首字母并且大写
                    String pinyin = Trans2PinYin.trans2PinYin(name);
                    String letter = "#";
                    if (!"".equals(pinyin)) {
                        letter = pinyin.substring(0, 1).toUpperCase();
                    }
                    if (!Arrays.asList(strChars).contains(letter)) {//不存在
                        letter = "#";
                    }
                    String insertSql = "insert into user(account, password,name,sex,photo,letter) values(?,?,?,?,?,?)";
                    db.execSQL(insertSql,new Object[]{account, password,name,sex,photo,letter});
                }
                db.close();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
        //两秒后跳转到主页面
        Intent intent = new Intent();
        if (userId > 0) {//已登录
             intent = new Intent(OpenActivity.this, MainActivity.class);
        } else {
            intent.setClass(OpenActivity.this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }
}