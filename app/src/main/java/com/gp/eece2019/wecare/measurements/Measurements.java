package com.gp.eece2019.wecare.measurements;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gp.eece2019.wecare.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class Measurements extends Fragment {

    TextView temp,hrate;
    MeasureMySqlHandler MSQL;
    MeasureSQLiteHandler MLITE;
    String username;
    Button showall;

    public Measurements(String username) {
        this.username = username;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_measurements, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        MSQL  = new MeasureMySqlHandler(getActivity());
        MLITE = new MeasureSQLiteHandler(getActivity());

        ArrayList<Measurementitem> alldata = new ArrayList<Measurementitem>();

        Cursor res =MLITE.getAllData();
        if(res.getCount()!=0)
        {
            res.moveToLast(); boolean last=true;
            while(res.moveToPrevious())
            {
                if(last) {res.moveToLast(); last=false;}
                String T_c  = "Normal";
                String HR_c = "Normal";
                if(res.getString(2).equals("3"))  T_c="UP NORMAL";
                else if(res.getString(2).equals("4") || res.getString(2).equals("5")) T_c="DANGEROUS";

                if(res.getString(4).equals("3"))  HR_c="UP NORMAL";
                else if(res.getString(4).equals("4") || res.getString(4).equals("5")) HR_c="DANGEROUS";

                alldata.add(new Measurementitem(res.getString(1),T_c,res.getString(3),HR_c,res.getString(0)));
            }
        }
        else{
            Toast toast = Toast.makeText(getActivity(),"No measurement are received yet",Toast.LENGTH_LONG);
            toast.show();
        }

        MeasurementsAdapter a = new MeasurementsAdapter(getActivity(), alldata);

        // Get a reference to the ListView, and attach the adapter to the listView.
        ListView listView = (ListView) getView().findViewById(R.id.measure_list);
        listView.setAdapter(a);

        MSQL.execute(username); // user name is passed to func do in backgroung in MeasuseSQLhandler Class

        super.onActivityCreated(savedInstanceState);
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.display_type,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.listdisplay:


                return true;

            case R.id.tabledisplay:

                MeasurementsTable m= new MeasurementsTable(username);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.Fragment_container, m)
                        .addToBackStack(null)
                        .commit();

                return true;

            case R.id.graphdisplay:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
