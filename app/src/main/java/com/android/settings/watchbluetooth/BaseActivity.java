package com.android.settings.watchbluetooth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

public class BaseActivity extends SlidrBaseActivity {
    private final int JUMP_ACTIVITY = 77;
    private final int JUMP_ACTIVITY_FOR_RESULT = 78;
    private final int JUMP_DELAY = 300;
    public String TAG = BaseActivity.class.getSimpleName();
    protected Context mContext;
    /* access modifiers changed from: private */
    public boolean mDispatch = false;
    protected Handler mJumpHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 77:
                    boolean unused = BaseActivity.this.mDispatch = true;
                    BaseActivity.this.startActivity((Intent) msg.obj);
                    return;
                case 78:
                    boolean unused2 = BaseActivity.this.mDispatch = true;
                    int requestCode = msg.arg1;
                    BaseActivity.this.startActivityForResult((Intent) msg.obj, requestCode);
                    return;
                default:
                    return;
            }
        }
    };

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void jumpActivity(Intent intent) {
        this.mJumpHandler.removeMessages(77);
        Message msg = this.mJumpHandler.obtainMessage();
        msg.what = 77;
        msg.obj = intent;
        this.mJumpHandler.sendMessageDelayed(msg, 300);
    }

    /* access modifiers changed from: protected */
    public void jumpActivityForResult(Intent intent, int requestCode) {
        this.mJumpHandler.removeMessages(78);
        Message msg = this.mJumpHandler.obtainMessage();
        msg.what = 78;
        msg.obj = intent;
        msg.arg1 = requestCode;
        this.mJumpHandler.sendMessageDelayed(msg, 300);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() != 0 || !this.mDispatch) {
            return super.dispatchTouchEvent(ev);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        this.mDispatch = false;
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        this.mDispatch = false;
        super.onPause();
    }
}
