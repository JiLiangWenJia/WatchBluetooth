package com.android.settings.watchbluetooth.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {
    private Context context;
    private int layoutId;
    private List<T> list;

    public abstract void convert(ViewHolder viewHolder, T t);

    public CommonAdapter(Context context2, List<T> list2, int layoutId2) {
        this.context = context2;
        this.list = list2;
        this.layoutId = layoutId2;
    }

    public int getCount() {
        if (this.list != null) {
            return this.list.size();
        }
        return 0;
    }

    public T getItem(int position) {
        if (this.list != null) {
            return this.list.get(position);
        }
        return null;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(this.context, convertView, this.layoutId, parent, position);
        convert(holder, getItem(position));
        return holder.getConvertView();
    }
}
