package com.android.settings.watchbluetooth.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SPUtils {
    public static final String KEY_AGREE_PERMISSION = "key_agree_permission";
    private static SPUtils mSettings;
    private SharedPreferences sharedPreferences;

    public static SPUtils getInstance(Context context) {
        if (mSettings == null) {
            mSettings = new SPUtils(context);
        }
        return mSettings;
    }

    private SPUtils(Context context) {
        this.sharedPreferences = context.getSharedPreferences("ble_sputil", 0);
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key, int defaultValue) {
        return this.sharedPreferences.getInt(key, defaultValue);
    }

    public void putLong(String key, long value) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public long getLong(String key, long defaultValue) {
        return this.sharedPreferences.getLong(key, defaultValue);
    }

    public void putFloat(String key, float value) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public float getFloat(String key, float defaultValue) {
        return this.sharedPreferences.getFloat(key, defaultValue);
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return this.sharedPreferences.getBoolean(key, defaultValue);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key, String defaultValue) {
        return this.sharedPreferences.getString(key, defaultValue);
    }

    public <T> boolean putObject(String key, T object) {
        if (object == null) {
            SharedPreferences.Editor editor = this.sharedPreferences.edit();
            editor.remove(key);
            return editor.commit();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            try {
                oos.writeObject(object);
                String objectStr = new String(Base64.encode(baos.toByteArray(), 0));
                try {
                    baos.close();
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SharedPreferences.Editor editor2 = this.sharedPreferences.edit();
                editor2.putString(key, objectStr);
                return editor2.commit();
            } catch (IOException e2) {
                //e = e2;
                ObjectOutputStream objectOutputStream = oos;
                e2.printStackTrace();
                return false;
            }
        } catch (IOException e3) {
           // e = e3;
            e3.printStackTrace();
            return false;
        }
    }

    public <T> T getObject(String key) {
        try {
            String wordBase64 = this.sharedPreferences.getString(key, "");
            if (wordBase64 == null || wordBase64.equals("")) {
                return null;
            }
            ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(wordBase64.getBytes(), 0));
            ObjectInputStream ois = new ObjectInputStream(bais);
            T readObject = (T) ois.readObject();
            bais.close();
            ois.close();
            return readObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
