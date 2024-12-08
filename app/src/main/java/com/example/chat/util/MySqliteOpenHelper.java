package com.example.chat.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqliteOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "chat.db";
    public static final int DB_VERSION = 2;  // 假设我们更新到版本2

    // 创建表 SQL 语句
    private static final String CREATE_USER_TABLE =
            "CREATE TABLE user (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "account TEXT, " +
                    "password TEXT, " +
                    "name TEXT, " +
                    "sex TEXT, " +
                    "photo TEXT, " +
                    "letter TEXT)";

    private static final String CREATE_CONTACTS_TABLE =
            "CREATE TABLE contacts (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "userId INTEGER, " +
                    "toUserId INTEGER)";

    private static final String CREATE_CHAT_TABLE =
            "CREATE TABLE chat (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "fromUserId INTEGER, " +
                    "toUserId INTEGER, " +
                    "content TEXT, " +
                    "date INTEGER, " +
                    "status TEXT)";

    private static final String CREATE_MESSAGE_TABLE =
            "CREATE TABLE message (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "sender_id INTEGER, " +
                    "receiver_id INTEGER, " +
                    "message_content TEXT, " +
                    "timestamp INTEGER, " +
                    "status TEXT)";

    public MySqliteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_CHAT_TABLE);
        db.execSQL(CREATE_MESSAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            // 版本升级时可以进行表结构的更改
            if (oldVersion == 1) {
                db.execSQL("ALTER TABLE user ADD COLUMN email TEXT");  // 添加新字段
            }
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    // 插入数据
    public long insertUser(SQLiteDatabase db, String account, String password, String name, String sex, String photo, String letter) {
        ContentValues values = new ContentValues();
        values.put("account", account);
        values.put("password", password);
        values.put("name", name);
        values.put("sex", sex);
        values.put("photo", photo);
        values.put("letter", letter);
        return db.insert("user", null, values);
    }

    // 查询数据
    public Cursor getUserById(SQLiteDatabase db, int userId) {
        return db.query("user", null, "id = ?", new String[]{String.valueOf(userId)}, null, null, null);
    }

    // 更新数据
    public int updateUser(SQLiteDatabase db, int userId, String name, String sex, String photo) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("sex", sex);
        values.put("photo", photo);
        return db.update("user", values, "id = ?", new String[]{String.valueOf(userId)});
    }

    // 删除数据
    public int deleteUser(SQLiteDatabase db, int userId) {
        return db.delete("user", "id = ?", new String[]{String.valueOf(userId)});
    }

    // 使用事务插入数据
    public void insertUserWithTransaction(SQLiteDatabase db, String account, String password, String name) {
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("account", account);
            values.put("password", password);
            values.put("name", name);
            db.insert("user", null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
