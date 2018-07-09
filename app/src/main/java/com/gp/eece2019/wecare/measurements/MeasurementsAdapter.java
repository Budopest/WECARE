package com.gp.eece2019.wecare.measurements;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gp.eece2019.wecare.R;

import java.util.ArrayList;

public class MeasurementsAdapter extends ArrayAdapter<Measurementitem> {

    public MeasurementsAdapter(@NonNull Context context, @NonNull ArrayList<Measurementitem> mitems) {
        super(context,0, mitems);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_measure, parent, false);
        }

        Measurementitem m = getItem(position);

        TextView T = (TextView) listItemView.findViewById(R.id.temp);
        T.setText("Temperature: "+m.getTemperature());

        TextView TF = (TextView) listItemView.findViewById(R.id.tempstate);

        if(m.getT_condition().equals("DANGEROUS")) TF.setTextColor(Color.RED);
        if(m.getT_condition().equals("UP NORMAL")) TF.setTextColor(Color.GRAY);
        TF.setText("Condition: "+m.getT_condition());


        TextView HR = (TextView) listItemView.findViewById(R.id.rate);
        HR.setText("Heart Rate: "+m.getHeartrate());

        TextView HRF = (TextView) listItemView.findViewById(R.id.ratestate);

        if(m.getHr_condition().equals("DANGEROUS")) HRF.setTextColor(Color.RED);
        if(m.getHr_condition().equals("UP NORMAL")) HRF.setTextColor(Color.GRAY);
        HRF.setText("Condition: "+m.getHr_condition());

        TextView D = (TextView) listItemView.findViewById(R.id.measuredate);
        D.setText("Measure number "+m.getID()+ "    Date:"+m.getDATE());

        return listItemView;

    }
}
