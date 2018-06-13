package com.gp.eece2019.wecare.messanger;

import android.widget.TextView;

public class ViewHolder {
    TextView text;

    public ViewHolder(TextView text) {
        this.text = text;
    }

    public TextView getText() {
        return text;
    }

    public void setText(TextView text) {
        this.text = text;
    }

}
