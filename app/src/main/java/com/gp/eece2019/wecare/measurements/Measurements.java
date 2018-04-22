package com.gp.eece2019.wecare.measurements;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gp.eece2019.wecare.R;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class Measurements extends Fragment implements View.OnClickListener{

    TextView temp,hrate;
    MeasureSQLhandler MSQL;
    MeasureSQLLITE MLITE;
    String username;
    Button showall;

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

        showall = (Button) getView().findViewById(R.id.showall);


    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        MSQL  = new MeasureSQLhandler(getActivity());
        showall.setOnClickListener(this);
        MLITE = new MeasureSQLLITE(getActivity());
        MSQL.execute(username); // user name is passed to func do in backgroung in MeasuseSQLhandler Class
        super.onActivityCreated(savedInstanceState);
    }
    public void onClick (View v)
    {

        Cursor res = MLITE.getAllData();
        if(res.getCount() == 0) {
            // show message
            showMessage("Error","Nothing found");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("Id :"+ res.getString(0)+"\n");
            buffer.append("T :"+ res.getString(1)+"\n");
            buffer.append("TF :"+ res.getString(2)+"\n");
            buffer.append("P :"+ res.getString(3)+"\n");
            buffer.append("PF :"+ res.getString(4)+"\n\n");
        }

        // Show all data
        showMessage("Data",buffer.toString());

    }
    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }





}
