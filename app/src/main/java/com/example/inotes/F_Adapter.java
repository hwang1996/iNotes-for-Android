package com.example.inotes;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by 王浩啊 on 2017/2/21.
 */
public class F_Adapter extends BaseAdapter {

    private Context context;
    private Cursor cursor;
    private LinearLayout layout;

    public F_Adapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        layout = (LinearLayout) inflater.inflate(R.layout.cell2, null);
        TextView contenttv;
        TextView timetv;

        contenttv = (TextView) layout.findViewById(R.id.list_content);
        timetv = (TextView) layout.findViewById(R.id.list_time);

        cursor.moveToPosition(position);
        String content = cursor.getString(cursor.getColumnIndex("time"));
        String time = cursor.getString(cursor.getColumnIndex("location"));

        contenttv.setText(content);
        timetv.setText(time);

        return layout;
    }
}