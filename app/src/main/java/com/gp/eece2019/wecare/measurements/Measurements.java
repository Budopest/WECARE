package com.gp.eece2019.wecare.measurements;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gp.eece2019.wecare.R;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class Measurements extends Fragment {

    TextView temp,hrate;
    MeasureSQLhandler MSQL;
    String username;

    public Measurements(String username) {
        this.username = username;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_measurements, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        MSQL = new MeasureSQLhandler(getActivity());
        MSQL.execute(username); // user name is passed to func do in backgroung in MeasuseSQLhandler Class


        super.onActivityCreated(savedInstanceState);
    }




}
