package com.android.settings.watchbluetooth.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ViewHolder {
    private View mConvertView;
    private SparseArray<View> mViews = new SparseArray<>();

    public ViewHolder(Context context, int layoutId, ViewGroup parent, int position) {
        this.mConvertView = LayoutInflater.from(context).inflate(layoutId, (ViewGroup) null);
        this.mConvertView.setTag(this);
    }

    public static ViewHolder get(Context context, View convertView, int layoutId, ViewGroup parent, int position) {
        if (convertView == null) {
            return new ViewHolder(context, layoutId, parent, position);
        }
        return (ViewHolder) convertView.getTag();
    }

    public <T extends View> T getView(int viewId) {
        View view = this.mViews.get(viewId);
        if (view != null) {
            return (T) view;
        }
        View view2 = this.mConvertView.findViewById(viewId);
        this.mViews.put(viewId, view2);
        return (T) view2;
    }

    public View getConvertView() {
        return this.mConvertView;
    }

    public TextView getTextView(int viewId) {
        return (TextView) getView(viewId);
    }

    public Button getButton(int viewId) {
        return (Button) getView(viewId);
    }

    public ImageView getImageView(int viewId) {
        return (ImageView) getView(viewId);
    }

    public CheckBox getCheckBox(int viewId) {
        return (CheckBox) getView(viewId);
    }

    public ViewHolder setTextView(int viewId, String text) {
        ((TextView) getView(viewId)).setText(text);
        return this;
    }

    public ViewHolder setTextView(int viewId, int resId) {
        ((TextView) getView(viewId)).setText(resId);
        return this;
    }

    public ViewHolder setButton(int viewId, String text) {
        ((Button) getView(viewId)).setText(text);
        return this;
    }

    public ViewHolder setButton(int viewId, int resId) {
        ((Button) getView(viewId)).setText(resId);
        return this;
    }

    public ViewHolder setImageView(int viewId, int resId) {
        ((ImageView) getView(viewId)).setImageResource(resId);
        return this;
    }

    public ViewHolder setImageView(int viewId, Drawable drawable) {
        ((ImageView) getView(viewId)).setImageDrawable(drawable);
        return this;
    }

    public ViewHolder setImageView(int viewId, Bitmap bm) {
        ((ImageView) getView(viewId)).setImageBitmap(bm);
        return this;
    }

    public ViewHolder setProgress(int viewId, int progress) {
        ((ProgressBar) getView(viewId)).setProgress(progress);
        return this;
    }
}
