package com.android.settings.watchbluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.settings.watchbluetooth.adapter.CommonAdapter;
import com.android.settings.watchbluetooth.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.Set;

@SuppressLint({"NewApi"})
public class BTHeadsetActivity extends SlidrBaseActivity {
    /* access modifiers changed from: private */
    public CommonAdapter<BluetoothDevice> adaper;
    private BluetoothAdapter btAdapter;
    private BluetoothDevice curDevice;
    /* access modifiers changed from: private */
    public boolean isReScan;
    private ListView listview;
    private View loadding;
    /* access modifiers changed from: private */
    public BluetoothA2dp mA2dpProfile;
    /* access modifiers changed from: private */
    public BluetoothHeadset mHeadsetProfile;
    /* access modifiers changed from: private */
    public ArrayList<BluetoothDevice> mList = new ArrayList<>();
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.bluetooth.device.action.FOUND".equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                if (device.getBondState() == 12) {
                    return;
                }
                if (device.getBluetoothClass().getMajorDeviceClass() == 1028 || device.getBluetoothClass().getMajorDeviceClass() == 1024) {
                    int indexInBonded = BTHeadsetActivity.this.mList.indexOf(device);
                    if (indexInBonded >= 0) {
                        BTHeadsetActivity.this.mList.remove(indexInBonded);
                    }
                    BTHeadsetActivity.this.mList.add(device);
                    BTHeadsetActivity.this.adaper.notifyDataSetChanged();
                }
            } else if ("android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(action)) {
                if (!BTHeadsetActivity.this.isReScan) {
                    BTHeadsetActivity.this.dismissLoadding();
                }
                boolean unused = BTHeadsetActivity.this.isReScan = false;
            } else if ("android.bluetooth.device.action.BOND_STATE_CHANGED".equals(action) || "android.bluetooth.device.action.ACL_CONNECTED".equals(action) || "android.bluetooth.device.action.ACL_DISCONNECTED".equals(action)) {
                BTHeadsetActivity.this.adaper.notifyDataSetChanged();
            }
        }
    };
    private ProgressBar progess;
    private TextView tvNotFound;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt_headset);
        this.btAdapter = BluetoothAdapter.getDefaultAdapter();
        initBluetoothProfiles();
        this.loadding = findViewById(R.id.loadding);
        this.tvNotFound = (TextView) findViewById(R.id.notfound_tv);
        this.progess = (ProgressBar) findViewById(R.id.progess);
        this.listview = (ListView) findViewById(R.id.listview);
        findViewById(R.id.btn_scan).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                BTHeadsetActivity.this.scanning();
            }
        });
        this.loadding.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                BTHeadsetActivity.this.dismissLoadding();
            }
        });
        initListview();
        registerRecever();
        addBondedDvices();
    }

    private void initBluetoothProfiles() {
        this.btAdapter.getProfileProxy(this, new BluetoothProfile.ServiceListener() {
            public void onServiceDisconnected(int arg0) {
            }

            public void onServiceConnected(int arg0, BluetoothProfile arg1) {
                BluetoothA2dp unused = BTHeadsetActivity.this.mA2dpProfile = (BluetoothA2dp) arg1;
            }
        }, 2);
        this.btAdapter.getProfileProxy(this, new BluetoothProfile.ServiceListener() {
            public void onServiceDisconnected(int arg0) {
            }

            public void onServiceConnected(int arg0, BluetoothProfile arg1) {
                BluetoothHeadset unused = BTHeadsetActivity.this.mHeadsetProfile = (BluetoothHeadset) arg1;
            }
        }, 1);
    }

    private void initListview() {
        this.adaper = new CommonAdapter<BluetoothDevice>(this, this.mList, R.layout.item_list) {
            public void convert(ViewHolder holder, final BluetoothDevice item) {
                holder.setTextView((int) R.id.name, item.getName());
                if (item.isConnected()) {
                    holder.setTextView((int) R.id.state, (int) R.string.connected);
                } else if (item.getBondState() == 10) {
                    holder.setTextView((int) R.id.state, (int) R.string.disconnected);
                } else {
                    holder.setTextView((int) R.id.state, (int) R.string.paired);
                }
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        if (item.isConnected()) {
                            BTHeadsetActivity.this.showConnectedPairedDialog(item, 1);
                        } else if (item.getBondState() == 10) {
                            BTHeadsetActivity.this.pair(item);
                        } else {
                            BTHeadsetActivity.this.showConnectedPairedDialog(item, 2);
                        }
                    }
                });
            }
        };
        this.listview.setAdapter(this.adaper);
    }

    private void registerRecever() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.bluetooth.device.action.FOUND");
        filter.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
        filter.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED");
        filter.addAction("android.bluetooth.device.action.ACL_CONNECTED");
        filter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
        registerReceiver(this.mReceiver, filter);
    }

    /* access modifiers changed from: protected */
    public void connectDevice(BluetoothDevice item) {
        if (this.mA2dpProfile != null) {
            //this.mA2dpProfile.connect(item);
            Toast.makeText(BTHeadsetActivity.this, "1", Toast.LENGTH_SHORT).show();
        }
        if (this.mHeadsetProfile != null) {
             this.mHeadsetProfile.connect(item);
        }
    }

    /* access modifiers changed from: protected */
    public void disconnectDevice(BluetoothDevice item) {
        if (this.mA2dpProfile != null) {
            //this.mA2dpProfile.disconnect(item); //断开设备
            Toast.makeText(BTHeadsetActivity.this, "3", Toast.LENGTH_SHORT).show();
        }
        if (this.mHeadsetProfile != null) {
            this.mHeadsetProfile.disconnect(item);
        }
    }

    /* access modifiers changed from: private */
    public void showConnectedPairedDialog(BluetoothDevice device, int state) {
        this.curDevice = device;
        startActivityForResult(new Intent(this, PairConnectDialog.class).putExtra("name", device.getName()).putExtra("state", state), 1);
    }

    /* access modifiers changed from: private */
    public void pair(BluetoothDevice item) {
        if (this.btAdapter.isDiscovering()) {
            this.btAdapter.cancelDiscovery();
        }
        item.createBond();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        Class cls = this.btAdapter.getClass();
        try {
            cls.getMethod("setScanMode", new Class[]{Integer.TYPE, Integer.TYPE}).invoke(this.btAdapter, new Object[]{23, 120});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null && this.curDevice != null) {
            int status = data.getIntExtra("status", -1);
            if (status == 1) {
                connectDevice(this.curDevice);
            } else if (status == 2) {
                disconnectDevice(this.curDevice);
            } else if (status == 3) {
                this.curDevice.removeBond();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        Class cls = this.btAdapter.getClass();
        try {
            int state = ((Integer) cls.getMethod("getConnectionState", new Class[0]).invoke(this.btAdapter, new Object[0])).intValue();
            if (state != 1 && state != 2) {
                cls.getMethod("setScanMode", new Class[]{Integer.TYPE}).invoke(this.btAdapter, new Object[]{20});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.mReceiver);
        this.btAdapter.closeProfileProxy(2, this.mA2dpProfile);
        this.btAdapter.closeProfileProxy(1, this.mHeadsetProfile);
    }

    /* access modifiers changed from: private */
    public void scanning() {
        if (this.btAdapter.isDiscovering()) {
            this.isReScan = true;
            this.btAdapter.cancelDiscovery();
        } else {
            this.isReScan = false;
        }
        this.mList.clear();
        addBondedDvices();
        this.loadding.setVisibility(View.VISIBLE);
        this.progess.setVisibility(View.VISIBLE);
        this.tvNotFound.setVisibility(View.GONE);
        this.btAdapter.startDiscovery();
    }

    private void addBondedDvices() {
        Set<BluetoothDevice> devicese = this.btAdapter.getBondedDevices();
        if (devicese != null) {
            for (BluetoothDevice device : devicese) {
                int deviceType = device.getBluetoothClass().getMajorDeviceClass();
                if (deviceType == 1028 || deviceType == 1024) {
                    this.mList.add(device);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void dismissLoadding() {
        this.isReScan = false;
        this.loadding.setVisibility(View.GONE);
    }
}
