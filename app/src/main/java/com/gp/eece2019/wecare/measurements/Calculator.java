package com.gp.eece2019.wecare.measurements;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gp.eece2019.wecare.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Calculator extends Fragment {


    public Calculator() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_calculator, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.measuretype,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.Calc_temp:

                Toast.makeText(getActivity(), "TEMP", Toast.LENGTH_LONG).show();

                return true;

            case R.id.Calc_HRate:

                Toast.makeText(getActivity(), "Heart Rate", Toast.LENGTH_LONG).show();

                return true;

            case R.id.Calc_Bpressure:

                Toast.makeText(getActivity(), "Blood Pressure", Toast.LENGTH_LONG).show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
