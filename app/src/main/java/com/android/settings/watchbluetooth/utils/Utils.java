package com.android.settings.watchbluetooth.utils;

import android.content.Context;
import android.content.Intent;

public class Utils {
    public static final String ACTION_GATT_STATE_CHANGED = "gatt_status_changed";
    private static final String ACTION_SETTINGS_GLOBAL_PUT_VALUE = "com.hoin.action.SETTINGS_GLOBAL_PUT_VALUE";
    private static final String EXTRA_SETTINGS_VALUE_DATA = "hoin.extra.EXTRA_SETTINGS_VALUE_DATA";
    private static final String EXTRA_SETTINGS_VALUE_KEY = "hoin.extra.EXTRA_SETTINGS_VALUE_KEY";
    private static final String EXTRA_SETTINGS_VALUE_TYPE = "hoin.extra.EXTRA_SETTINGS_VALUE_TYPE";
    public static final String GATT_CONNECTED = "connected";
    public static final String GATT_CONNECT_STATE = "gatt_connect_state";
    public static final String GATT_IPHONE_CONNECTED = "iphoneconnected";
    public static final String GATT_PACKAGE_MAIN = "com.hmct.wearservice.ui.BtConnectWithPhoneActivity";
    public static final String GATT_PACKAGE_NAME = "com.hmct.wearservice";
    public static final String SETTING_HOTKEY_BLE = "_hot_ble";

    public static boolean setHotBleGlobalProper(Context context, boolean isFlag) {
        Intent intent = new Intent(ACTION_SETTINGS_GLOBAL_PUT_VALUE);
        intent.putExtra(EXTRA_SETTINGS_VALUE_KEY, SETTING_HOTKEY_BLE);
        intent.putExtra(EXTRA_SETTINGS_VALUE_DATA, isFlag ? 1 : 0);
        context.sendBroadcast(intent);
        return true;
    }
}
