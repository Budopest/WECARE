package com.gp.eece2019.wecare.measurements;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.gp.eece2019.wecare.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class MeasurementsChart extends Fragment {

    String username;

    public MeasurementsChart(String usename) {
        // Required empty public constructor
        this.username=usename;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_measurements_chart, container, false);
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.display_type,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        LineChart chart = (LineChart) getView().findViewById(R.id.chart);

        List<Entry> Temperature_entries = new ArrayList<Entry>();

        List<Entry> Rate_entries = new ArrayList<Entry>();

        MeasureSQLiteHandler MLITE = new MeasureSQLiteHandler(getActivity());
        Cursor res =MLITE.getAllData();

        if(res.getCount()!=0)
        {
            while(res.moveToNext())
            {
                Temperature_entries.add(new Entry(res.getFloat(0),res.getFloat(1)));
                Rate_entries.add(new Entry(res.getFloat(0),res.getFloat(3)));
            }

            LineDataSet Temperature_dataSet = new LineDataSet(Temperature_entries, "Temperature"); // add entries to dataset
            Temperature_dataSet.setColor(Color.RED);

            LineDataSet Rate_dataSet = new LineDataSet(Rate_entries, "Heart Rate"); // add entries to dataset
            Rate_dataSet.setColor(Color.BLUE);

            LineData lineData = new LineData(Temperature_dataSet,Rate_dataSet);
            chart.setData(lineData);
            chart.invalidate(); // refresh
        }
        else{
           Toast toast = Toast.makeText(getActivity(),"No measurement are received yet",Toast.LENGTH_LONG);
            toast.show();
        }




        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.listdisplay:
                Measurements m= new Measurements(username);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.Fragment_container,m)
                        .addToBackStack(null)
                        .commit();

                return true;

            case R.id.tabledisplay:

                MeasurementsTable t= new MeasurementsTable(username);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.Fragment_container, t)
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
