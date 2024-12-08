package com.example.chat.ui.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chat.R;
import com.example.chat.bean.User;
import com.example.chat.util.MySqliteOpenHelper;
import com.example.chat.util.Tools;


/**
 * 个人信息
 */
public class PersonActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity mActivity;
    private TextView tvAccount;
    private EditText etNickName;
    private RadioGroup rgSex;//性别
    private Button btnSave;//保存
    User mUser = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        mActivity = this;
        helper = new MySqliteOpenHelper(mActivity);
        tvAccount = findViewById(R.id.tv_account);
        etNickName = findViewById(R.id.et_nickName);//获取昵称
        rgSex = findViewById(R.id.rg_sex);
        btnSave = findViewById(R.id.btn_save);
        initView();
    }

    private void initView() {
        Integer userId =getIntent().getIntExtra("userId",0);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from user where id = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userId)});
        if (cursor != null && cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {
                Integer dbId = cursor.getInt(0);
                String dbAccount = cursor.getString(1);
                String dbPassword = cursor.getString(2);
                String dbName = cursor.getString(3);
                String dbSex = cursor.getString(4);
                String dbPhone = cursor.getString(5);
                mUser = new User(dbId, dbAccount, dbPassword, dbName, dbSex, dbPhone);
                tvAccount.setText(dbAccount);
                etNickName.setText(dbName);
                rgSex.check("男".equals(dbSex)?R.id.rb_man:R.id.rb_woman);
            }
        }
        db.close();

        //保存
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = helper.getWritableDatabase();
                String nickName= etNickName.getText().toString();
                if ("".equals(nickName)) {//昵称不能为空
                    Toast.makeText(mActivity,"昵称不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }


                String sex = rgSex.getCheckedRadioButtonId() == R.id.rb_man ? "男" : "女";//性别
                db.execSQL("update user set name = ?,sex=? where id = ?", new Object[]{nickName, sex,mUser.getId()});
                Toast.makeText(PersonActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
                db.close();
                finish();
            }
        });

    }
    public void back(View view){
        finish();
    }
}
