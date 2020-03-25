package com.android.settings.watchbluetooth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PairConnectDialog extends SlidrBaseActivity {
    private ImageView conn_icon;
    /* access modifiers changed from: private */
    public int state;
    private TextView tvName;
    private TextView tvState;
    private TextView value_tv;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pair_conn);
        this.state = getIntent().getIntExtra("state", 0);
        String name = getIntent().getStringExtra("name");
        this.tvName = (TextView) findViewById(R.id.name);
        this.tvState = (TextView) findViewById(R.id.state);
        this.conn_icon = (ImageView) findViewById(R.id.conn_icon);
        this.value_tv = (TextView) findViewById(R.id.value_tv);
        this.tvName.setText(name);
        if (this.state == 1) {
            this.conn_icon.setImageResource(R.drawable.btn_bt_disconn_s);
            this.value_tv.setText(R.string.disconn_device);
            this.tvState.setText(R.string.connected);
        } else if (this.state == 2) {
            this.conn_icon.setImageResource(R.drawable.btn_bt_conn_s);
            this.value_tv.setText(R.string.conn_device);
            this.tvState.setText(R.string.paired);
        }
        findViewById(R.id.disconnected_ll).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (PairConnectDialog.this.state == 1) {
                    PairConnectDialog.this.disConnect();
                } else if (PairConnectDialog.this.state == 2) {
                    PairConnectDialog.this.connect();
                }
            }
        });
        findViewById(R.id.dispair_ll).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Toast.makeText(PairConnectDialog.this, "取消配对", Toast.LENGTH_SHORT).show();
                PairConnectDialog.this.disPair();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void connect() {
        Intent data = new Intent();
        data.putExtra("status", 1);
        setResult(-1, data);
        finish();
    }

    /* access modifiers changed from: private */
    public void disConnect() {
        Intent data = new Intent();
        data.putExtra("status", 2);
        setResult(-1, data);
        finish();
    }

    /* access modifiers changed from: protected */
    public void disPair() {
        Intent data = new Intent();
        data.putExtra("status", 3);
        setResult(-1, data);
        finish();
    }
}
