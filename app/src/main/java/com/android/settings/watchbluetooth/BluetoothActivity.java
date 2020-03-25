package com.android.settings.watchbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.settings.watchbluetooth.utils.SPUtils;
import com.android.settings.watchbluetooth.utils.Utils;
import java.util.Set;

import androidx.core.content.ContextCompat;

public class BluetoothActivity extends BaseActivity implements View.OnClickListener {
    private String[] authComArr = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.BLUETOOTH", "android.permission.BLUETOOTH_ADMIN", "android.permission.KILL_BACKGROUND_PROCESSES"};
    /* access modifiers changed from: private */
    public BluetoothAdapter btAdapter;
    private TextView btHeadsetName;
    /* access modifiers changed from: private */
    public TextView btHeadsetState;
    private TextView btPhoneName;
    /* access modifiers changed from: private */
    public TextView btPhoneState;
    private View btnHeadsetBluetooth;
    private View btnPhoneBluetooth;
    private CheckBox btnSwitch;
    private boolean isOpened;
    /* access modifiers changed from: private */
    public View loadding;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
            if ("android.bluetooth.device.action.ACL_CONNECTED".equals(action)) {
                if (BluetoothActivity.this.isHeadsetBt(device)) {
                    BluetoothActivity.this.btHeadsetState.setText(R.string.connected);
                }
            } else if ("android.bluetooth.device.action.ACL_DISCONNECTED".equals(action)) {
                if (BluetoothActivity.this.isHeadsetBt(device)) {
                    BluetoothActivity.this.btHeadsetState.setText(R.string.disconnected);
                }
            } else if (Utils.ACTION_GATT_STATE_CHANGED.equals(action)) {
                BluetoothActivity.this.changPhoneStateView();
            } else if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(action)) {
                int blueState = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", 0);
                if (blueState == 13 || blueState == 10) {
                    BluetoothActivity.this.btHeadsetState.setText(R.string.disconnected);
                    BluetoothActivity.this.btPhoneState.setText(R.string.disconnected);
                    BluetoothActivity.this.changeCheckedView(false);
                } else if (blueState == 12) {
                    Log.d(BluetoothActivity.this.TAG, "changeCheckedView  action state=BluetoothAdapter.STATE_ON");
                    BluetoothActivity.this.changeCheckedView(true);
                    BluetoothActivity.this.loadding.setVisibility(View.GONE);
                    BluetoothActivity.this.changPhoneStateView();
                }
            }
        }
    };
    private View vPermission;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        int i;
        this.btAdapter = BluetoothAdapter.getDefaultAdapter();
        this.btnPhoneBluetooth = findViewById(R.id.bt_phone_ll);
        this.btnHeadsetBluetooth = findViewById(R.id.bt_headset_ll);
        this.btPhoneName = (TextView) findViewById(R.id.bt_phone_name);
        this.btPhoneState = (TextView) findViewById(R.id.bt_phone_state);
        this.btHeadsetName = (TextView) findViewById(R.id.bt_headset_name);
        this.btHeadsetState = (TextView) findViewById(R.id.bt_headset_state);
        this.btnSwitch = (CheckBox) findViewById(R.id.switch_bt_btn);
        this.loadding = findViewById(R.id.loadding_01);
        this.vPermission = findViewById(R.id.permission_v);
        boolean isAgree = SPUtils.getInstance(this).getBoolean(SPUtils.KEY_AGREE_PERMISSION, false);
        View view = this.vPermission;
        if (isAgree) {
            i = 8;
        } else {
            i = 0;
        }
        view.setVisibility(i);
        this.btnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean checked) {
                if (!checked) {
                    BluetoothActivity.this.changeCheckedView(checked);
                } else if (BluetoothActivity.this.btAdapter.getState() == BluetoothAdapter.STATE_ON) {
                    BluetoothActivity.this.changeCheckedView(checked);
                }
            }
        });
        this.btnSwitch.setChecked(isBlueToothOn());
        findViewById(R.id.switch_wifi_rl).setOnClickListener(this);
        findViewById(R.id.cancel_ll).setOnClickListener(this);
        findViewById(R.id.submit_ll).setOnClickListener(this);
        this.btnHeadsetBluetooth.setOnClickListener(this);
        this.btnPhoneBluetooth.setOnClickListener(this);
        this.btHeadsetState.setText(getHeadsetStatus());
        changPhoneStateView();
        registerRecever();
    }

    public static boolean isBlueToothOn() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            return false;
        }
        switch (mBluetoothAdapter.getState()) {
            case 11:
                return true;
            case 12:
                return true;
            default:
                return false;
        }
    }

    private int getHeadsetStatus() {
        Set<BluetoothDevice> devicese = this.btAdapter.getBondedDevices();
        if (devicese != null) {
            for (BluetoothDevice device : devicese) {
                if (device.isConnected() && isHeadsetBt(device)) {
                    return R.string.connected;
                }
            }
        }
        return R.string.disconnected;
    }

    /* access modifiers changed from: private */
    public void changeCheckedView(boolean checked) {
        Log.d(this.TAG, "changeCheckedView  checked=" + checked);
        this.btnSwitch.setChecked(checked);
        this.btnPhoneBluetooth.setClickable(checked);
        this.btnHeadsetBluetooth.setClickable(checked);
        if (checked) {
            //this.btPhoneName.setTextColor(getResources().getColor(R.color.selected_color));
           // this.btPhoneState.setTextColor(getResources().getColor(R.color.selected_color));
           // this.btHeadsetName.setTextColor(getResources().getColor(R.color.selected_color));
           // this.btHeadsetState.setTextColor(getResources().getColor(R.color.selected_color));
            this.btPhoneName.setTextColor(ContextCompat.getColor(this, R.color.selected_color));
            this.btPhoneState.setTextColor(ContextCompat.getColor(this, R.color.selected_color));
            this.btHeadsetName.setTextColor(ContextCompat.getColor(this, R.color.selected_color));
            this.btHeadsetState.setTextColor(ContextCompat.getColor(this, R.color.selected_color));
        } else {
           // this.btPhoneName.setTextColor(getResources().getColor(R.color.unselected_color));
           // this.btPhoneState.setTextColor(getResources().getColor(R.color.unselected_color));
           // this.btHeadsetName.setTextColor(getResources().getColor(R.color.unselected_color));
           // this.btHeadsetState.setTextColor(getResources().getColor(R.color.unselected_color));
            this.btPhoneName.setTextColor(ContextCompat.getColor(this, R.color.unselected_color));
            this.btPhoneState.setTextColor(ContextCompat.getColor(this, R.color.unselected_color));
            this.btHeadsetName.setTextColor(ContextCompat.getColor(this, R.color.unselected_color));
            this.btHeadsetState.setTextColor(ContextCompat.getColor(this, R.color.unselected_color));
        }
        this.isOpened = checked;
    }

    private boolean hasCompletePhoneAuth() {
        PackageManager pm = getPackageManager();
        for (String auth : this.authComArr) {
            if (pm.checkPermission(auth, getPackageName()) != PackageManager.PERMISSION_GRANTED) {//0
                return false;
            }
        }
        return true;
    }

    private void requstPermissions() {
        if (Build.VERSION.SDK_INT >= 23 && !hasCompletePhoneAuth()) {
            requestPermissions(this.authComArr, 0);
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.mReceiver);
    }

    private void registerRecever() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.bluetooth.device.action.ACL_CONNECTED");
        filter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
        filter.addAction(Utils.ACTION_GATT_STATE_CHANGED);
        filter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        registerReceiver(this.mReceiver, filter);
    }

    /* access modifiers changed from: private */
    public void changPhoneStateView() {
        if (isGattConnected(false)) {
            this.btPhoneState.setText(R.string.connected);
        } else {
            this.btPhoneState.setText(R.string.disconnected);
        }
    }

    private boolean isGattConnected(boolean isAndroid) {
        String connectState = Settings.Global.getString(getContentResolver(), Utils.GATT_CONNECT_STATE);
        boolean isGattConnected = false;
        if (connectState != null) {
            if (isAndroid) {
                isGattConnected = Utils.GATT_CONNECTED.equals(connectState);
            } else {
                isGattConnected = Utils.GATT_CONNECTED.equals(connectState) || Utils.GATT_IPHONE_CONNECTED.equals(connectState);
            }
        }
        if (!isGattConnected || !isBlueToothOn()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public boolean isHeadsetBt(BluetoothDevice device) {
        if (device == null || (device.getBluetoothClass().getMajorDeviceClass() != 1028 && device.getBluetoothClass().getMajorDeviceClass() != 1024)) {
            return false;
        }
        return true;
    }

    public void onClick(View v) {
        boolean checked = true;
        switch (v.getId()) {
            case R.id.bt_headset_ll:
                if (this.isOpened) {
                    jumpActivity(new Intent(this, BTHeadsetActivity.class));
                    return;
                }
                return;
            case R.id.bt_phone_ll:
                if (this.isOpened && !isGattConnected(true)) {
                    jumpActivity(new Intent().setClassName(Utils.GATT_PACKAGE_NAME, Utils.GATT_PACKAGE_MAIN));
                    return;
                }
                return;
            case R.id.cancel_ll:
                finish();
                return;
            case R.id.submit_ll:
                SPUtils.getInstance(this).putBoolean(SPUtils.KEY_AGREE_PERMISSION, true);
                this.vPermission.setVisibility(View.GONE);
                requstPermissions();
                return;
            case R.id.switch_wifi_rl:
                if (this.btnSwitch.isChecked()) {
                    checked = false;
                }
                this.btnSwitch.setChecked(checked);
                if (checked) {
                    this.loadding.setVisibility(View.VISIBLE);
                    BluetoothAdapter.getDefaultAdapter().enable();
                } else {
                    BluetoothAdapter.getDefaultAdapter().disable();
                }
                Utils.setHotBleGlobalProper(getApplicationContext(), checked);
                return;
            default:
                return;
        }
    }

    @Override
    public synchronized ComponentName startForegroundServiceAsUser(Intent service, UserHandle user) {
        return null;
    }

}
