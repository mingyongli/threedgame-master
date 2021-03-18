package com.ws3dm.app.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.ws3dm.app.MyApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * SharedPreference 方法封装
 */
public class SharedUtil {

    /**
     * 保存在手机里面的文件名
     */
    public static String FILE_NAME = "Reader_Config";

    public static void makeSPName(String spName) {
        FILE_NAME = spName;
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void put(Context context, String key, Object object) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     *
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     *
     * @return
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     *
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {

        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }
    /***************************************************************************
     * 作用: 读配置文件 返回内容
     **************************************************************************/
    public static final String readPreferences(Context context1, String dataName) {
        String data = context1.getSharedPreferences("MYDATA",
                Context.MODE_PRIVATE).getString(dataName, null);
        return data;
    }

    /***************************************************************************
     * 作用: 写配置文件
     **************************************************************************/
    public static final void writePreferences(Context context1,
                                              String dataName, String data) {
        SharedPreferences settings = context1.getSharedPreferences("MYDATA",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(dataName, data);
        editor.commit();

    }

    /**
     * 根据设置标识进行用户设置
     */
    public static final String YES = "yes";
    public static final String NO = "no";
    public static void setSetting(boolean b, String code) {
        SharedPreferences userInfo = MyApplication.getInstance().getSharedPreferences("user_info", 0);
        if (b) {
            userInfo.edit().putString(code, YES).commit();
        } else {
            userInfo.edit().putString(code, NO).commit();
        }
    }

    /**
     * 根据设置标识获取设置
     */
    public static boolean getSetting(String code) {
        SharedPreferences userInfo = MyApplication.getInstance().getSharedPreferences("user_info", 0);
        String isHas = userInfo.getString(code, "");
        if (isHas == null || isHas.trim().equals("") || isHas.trim().equals(NO)) {
            userInfo.edit().putString(code, NO).commit();
            return false;
        } else {
            return true;
        }
    }

    /**
     * 根据设置标识获取设置
     */
    public static String getSharedPreferencesData(String key) {
        SharedPreferences userInfo = MyApplication.getInstance().getSharedPreferences("user_info", 0);
        String value = userInfo.getString(key, "0");
        return value;
    }

    /**
     * 根据设置标识获取设置
     */
    public static void setSharedPreferencesData(String key, String value) {
        SharedPreferences userInfo = MyApplication.getInstance().getSharedPreferences("user_info", 0);
        userInfo.edit().putString(key, value).apply();
    }


    // 获取String
    public static String getSharedPreferencesDataArray(String key) {
        SharedPreferences userInfo = MyApplication.getInstance().getSharedPreferences("user_info", 0);
        String value = userInfo.getString(key, "0");
        return value;
    }

    // 设置String数组
    public static void setSharedPreferencesDataArray(String key, String[] value) {
        String str = "";
        int forSize=value.length;
        for (int i = 0; i < forSize; i++) {
            if (i == forSize - 1)
                str += value[i];
            else
                str += value[i] + ",";
        }
        SharedPreferences userInfo = MyApplication.getInstance().getSharedPreferences("user_info", 0);
        userInfo.edit().putString(key, str).commit();
    }
}
