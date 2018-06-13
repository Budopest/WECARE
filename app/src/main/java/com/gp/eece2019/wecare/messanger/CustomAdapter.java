package com.gp.eece2019.wecare.messanger;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gp.eece2019.wecare.R;

public class CustomAdapter extends ArrayAdapter {

    public static final int TYPE_send = 0;
    public static final int TYPE_rec = 1;

    private ListViewItem[] objects;

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return objects[position].getType();
    }

    public CustomAdapter(Context context, int resource, ListViewItem[] objects) {
        super(context, resource, objects);
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        ListViewItem listViewItem = objects[position];
        int listViewItemType = getItemViewType(position);


        if (convertView == null) {

            if (listViewItemType == TYPE_send) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_send, null);
            } else if (listViewItemType == TYPE_rec) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_rec, null);
            }

            TextView textView = (TextView) convertView.findViewById(R.id.message_container);
            viewHolder = new ViewHolder(textView);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.getText().setText(listViewItem.getText());

        return convertView;
    }

}