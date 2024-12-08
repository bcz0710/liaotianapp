package com.example.chat.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 数据持久化工具类
 */
public class SPUtils {

    public static final String FILE_NAME = "share_data";

    // 是否第一次进入
    public static final String IF_FIRST = "is_first";
    // 是否为管理员
    public static final String IF_ADMIN = "account";
    // 用户ID
    public static final String USER_ID = "user_id";
    // 用户类型
    public static final String USER_TYPE = "user_type";

    private SPUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 保存数据的方法，我们根据数据类型自动调用相应的保存方法
     *
     * @param context 上下文
     * @param key     key值
     * @param value   数据
     * @param <T>     泛型类型
     */
    public static <T> void put(Context context, String key, T value) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (value == null) {
            return;
        }

        // 保存不同类型的数据
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else {
            editor.putString(key, value.toString());
        }

        // 使用apply()方法异步保存
        apply(editor);
    }

    /**
     * 获取数据，根据存储时的类型获取相应的值
     *
     * @param context       上下文
     * @param key           key值
     * @param defaultValue 默认值
     * @param <T>           泛型类型
     * @return 对应类型的值
     */
    public static <T> T get(Context context, String key, T defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if (defaultValue == null) {
            return null;
        }

        // 获取不同类型的数据
        if (defaultValue instanceof String) {
            return (T) sp.getString(key, (String) defaultValue);
        } else if (defaultValue instanceof Integer) {
            return (T) Integer.valueOf(sp.getInt(key, (Integer) defaultValue));
        } else if (defaultValue instanceof Boolean) {
            return (T) Boolean.valueOf(sp.getBoolean(key, (Boolean) defaultValue));
        } else if (defaultValue instanceof Float) {
            return (T) Float.valueOf(sp.getFloat(key, (Float) defaultValue));
        } else if (defaultValue instanceof Long) {
            return (T) Long.valueOf(sp.getLong(key, (Long) defaultValue));
        }

        return null;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context 上下文
     * @param key     key值
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context 上下文
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context 上下文
     * @param key     key值
     * @return 是否存在
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context 上下文
     * @return 所有的键值对
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 使用apply方法异步保存数据，如果反射不到apply方法则回退到commit
     *
     * @param editor SharedPreferences.Editor对象
     */
    private static void apply(SharedPreferences.Editor editor) {
        try {
            Method applyMethod = SharedPreferences.Editor.class.getMethod("apply");
            applyMethod.invoke(editor);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // 如果反射失败，使用commit()来保证数据保存
            editor.commit();
            Log.e("SPUtils", "Failed to invoke apply, using commit instead.");
        }
    }
}
