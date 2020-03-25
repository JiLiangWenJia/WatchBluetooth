package com.android.settings.watchbluetooth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class BindBTDialog extends SlidrBaseActivity {
    public static final int DIALOG_UNBIND = 1;
    public static final int DIALOG_UNBIND_SURE = 2;
    View btn_unbind1;
    View btn_unbind2;
    TextView tvName;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bind_bt);
        int type = getIntent().getIntExtra("dialog_type", 1);
        String name = getIntent().getStringExtra("name");
        this.tvName = (TextView) findViewById(R.id.name);
        this.btn_unbind1 = findViewById(R.id.btn_unbind1);
        this.btn_unbind2 = findViewById(R.id.btn_unbind2);
        this.tvName.setText(name);
        if (type == 1) {
            this.btn_unbind1.setVisibility(View.VISIBLE);
            this.btn_unbind2.setVisibility(View.GONE);
            findViewById(R.id.btn_unbind1).setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    BindBTDialog.this.startActivity(new Intent(BindBTDialog.this, BindBTDialog.class).putExtra("dialog_type", 2).putExtra("name", BindBTDialog.this.getString(R.string.unbind_content_msg).toString()));
                }
            });
        } else if (type == 2) {
            this.btn_unbind1.setVisibility(View.GONE);
            this.btn_unbind2.setVisibility(View.VISIBLE);
        }
    }
}
