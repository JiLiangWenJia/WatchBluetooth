package com.android.settings.watchbluetooth;

import android.app.Activity;
import android.os.Bundle;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;

import androidx.annotation.Nullable;

public class SlidrBaseActivity extends Activity {
    protected SlidrInterface slidr;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.slidr = Slidr.attach(this, new SlidrConfig.Builder().sensitivity(0.2f).velocityThreshold(250.0f).scrimStartAlpha(0.0f).build());
    }
}
