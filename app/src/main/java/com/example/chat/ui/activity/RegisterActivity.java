package com.example.chat.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.chat.R;
import com.example.chat.bean.User;
import com.example.chat.util.GlideEngine;
import com.example.chat.util.MySqliteOpenHelper;
import com.example.chat.util.Tools;
import com.example.chat.util.Trans2PinYin;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 注册页面
 */
public class RegisterActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private EditText etAccount;//账号
    private EditText etNickName;//昵称
    private EditText etPassword;//密码
    private EditText etPasswordSure;//确认密码
    private ImageView ivPhoto;//头像
    private RadioGroup rgSex;//性别
    private TextView tvLogin;//登录
    private Button btnRegister;//注册按钮
    private String imagePath = "";
    private RequestOptions headerRO = new RequestOptions().circleCrop();//圆角变换

    //字母数组,#代表未知，比如数字开头
    private String[] strChars = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        helper = new MySqliteOpenHelper(this);
        etAccount = findViewById(R.id.et_account);//获取账号
        etNickName = findViewById(R.id.et_nickName);//获取昵称
        etPassword = findViewById(R.id.et_password);//获取密码
        etPasswordSure = findViewById(R.id.et_password_sure);//获取确认密码
        ivPhoto = findViewById(R.id.iv_photo);
        rgSex = findViewById(R.id.rg_sex);
        tvLogin = (TextView) findViewById(R.id.tv_login);//登录
        btnRegister = (Button) findViewById(R.id.btn_register);//获取注册按钮
        //从相册中选择头像
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectClick();
            }
        });
        //返回登录
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到登录页面
                Intent intent = new Intent(RegisterActivity.this, com.example.chat.ui.activity.LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //设置注册点击按钮
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取请求参数
                String account = etAccount.getText().toString();
                String nickName = etNickName.getText().toString();
                String password = etPassword.getText().toString();
                String passwordSure = etPasswordSure.getText().toString();
                if ("".equals(imagePath)) {//请上传头像
                    Toast.makeText(RegisterActivity.this, "请上传头像", Toast.LENGTH_LONG).show();
                    return;
                }

                if ("".equals(nickName)) {
                    Toast.makeText(RegisterActivity.this,"昵称不能为空",Toast.LENGTH_LONG).show();
                    return;
                }

                if ("".equals(password)) {//密码为空
                    Toast.makeText(RegisterActivity.this, "密码为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!password.equals(passwordSure)) {//密码不一致
                    Toast.makeText(RegisterActivity.this, "密码不一致", Toast.LENGTH_LONG).show();
                    return;
                }
                String sex = rgSex.getCheckedRadioButtonId() == R.id.rb_man ? "男" : "女";//性别
                User mUser = null;
                //通过账号查询是否存在
                String sql = "select * from user where account = ? ";
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor cursor = db.rawQuery(sql, new String[]{account});
                if (cursor != null && cursor.getColumnCount() > 0) {
                    while (cursor.moveToNext()) {
                        Integer dbId = cursor.getInt(0);
                        String dbAccount = cursor.getString(1);
                        String dbPassword = cursor.getString(2);
                        String dbName = cursor.getString(3);
                        String dbSex = cursor.getString(4);
                        String dbPhone = cursor.getString(5);
                        mUser = new User(dbId, dbAccount, dbPassword, dbName, dbSex, dbPhone);
                    }
                }
                if (mUser == null) {//用户不存在 注册
                    //转换拼音截取首字母并且大写
                    String pinyin = Trans2PinYin.trans2PinYin(nickName);
                    String letter = "#";
                    if (!"".equals(pinyin)) {
                        letter = pinyin.substring(0, 1).toUpperCase();
                    }
                    if (!Arrays.asList(strChars).contains(letter)) {//不存在
                        letter = "#";
                    }
                    String insertSql = "insert into user(account, password,name,sex,photo,letter) values(?,?,?,?,?,?)";
                    db.execSQL(insertSql, new Object[]{account, password, nickName, sex, imagePath,letter});
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {//用户存在
                    Toast.makeText(RegisterActivity.this, "该账号已存在", Toast.LENGTH_SHORT).show();
                }
                db.close();
            }
        });
    }


    /**
     * 选择图片
     */
    private void selectClick() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(1)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        for (int i = 0; i < result.size(); i++) {
                            // onResult Callback
                            LocalMedia media = result.get(i);
                            String path;
                            // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                            boolean compressPath = media.isCompressed() || (media.isCut() && media.isCompressed());
                            // 裁剪过
                            boolean isCutPath = media.isCut() && !media.isCompressed();

                            if (isCutPath) {
                                path = media.getCutPath();
                            } else if (compressPath) {
                                path = media.getCompressPath();
                            } else if (!TextUtils.isEmpty(media.getAndroidQToPath())) {
                                // AndroidQ特有path
                                path = media.getAndroidQToPath();
                            } else if (!TextUtils.isEmpty(media.getRealPath())) {
                                // 原图
                                path = media.getRealPath();
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    path = PictureFileUtils.getPath(RegisterActivity.this, Uri.parse(media.getPath()));
                                } else {
                                    path = media.getPath();
                                }
                            }
                            imagePath = path;
                        }
                        Glide.with(RegisterActivity.this).load(imagePath).into(ivPhoto);

                    }

                    @Override
                    public void onCancel() {
                        // onCancel Callback
                    }
                });
    }



    public void back(View view) {
        finish();
    }
}
