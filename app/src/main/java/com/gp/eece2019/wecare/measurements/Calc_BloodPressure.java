package com.gp.eece2019.wecare.measurements;


import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gp.eece2019.wecare.R;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class Calc_BloodPressure extends Fragment {

    String uname;
    public Calc_BloodPressure(String uname) {
        // Required empty public constructor
        this.uname = uname;
    }

    EditText HP, LP;
    TextView MM_Hg;
    TextView Title;
    TextView Checkcondition;
    Button check;
    Button sendtodoc;


    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        HP = (EditText) getView().findViewById(R.id.High_pressure);
        LP = (EditText) getView().findViewById(R.id.low_pressure);
        MM_Hg = (TextView) getView().findViewById(R.id.mm_Hg);
        Title = (TextView) getView().findViewById(R.id.Blood_pressure);
        Checkcondition = (TextView) getView().findViewById(R.id.Condition_description);
        check = (Button) getView().findViewById(R.id.Check_your_condition);
        sendtodoc = (Button) getView().findViewById(R.id.Send_to_doctor);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_calculator, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        check.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String HPMeasure= HP.getText().toString();
                        String LPMeasure = LP.getText().toString();
                        if(HPMeasure.equals("")|| LPMeasure.equals("")) {
                            Toast.makeText(getActivity(), "Data is incorrect", Toast.LENGTH_LONG).show();
                            return;
                        }
                        int HPmeasurement = Integer.parseInt(HPMeasure);
                        int LPmeasurement = Integer.parseInt(LPMeasure);
                        if(120 > HPmeasurement && LPmeasurement < 80)
                        {   Checkcondition.setText("normal");
                            Checkcondition.setTextColor(Color.GREEN);
                        }
                        else if (120 <= HPmeasurement && HPmeasurement <= 129 && LPmeasurement < 80){
                            Checkcondition.setText("ELEVATED");
                            Checkcondition.setTextColor(Color.YELLOW);
                        }
                        else if (130 <= HPmeasurement && HPmeasurement <= 139 || LPmeasurement >= 80 && LPmeasurement <= 89){
                            Checkcondition.setText(String.format("HIGH BLOOD PRESSURE\nSTAGE 1"));
                            Checkcondition.setTextColor(Color.parseColor("#eeb400"));
                        }
                        else if (140 <= HPmeasurement && HPmeasurement <= 180 || LPmeasurement >= 90 && LPmeasurement <= 120){
                            Checkcondition.setText(String.format("HIGH BLOOD PRESSURE\nSTAGE 2"));
                            Checkcondition.setTextColor(Color.parseColor("#F06D2F"));
                        }
                        else if (180 < HPmeasurement || 120 < LPmeasurement){
                            Checkcondition.setText(String.format("HYPERTENSIVE CRISIS \nconsult your doctor immediately"));
                            Checkcondition.setTextColor(Color.RED);
                        }
                    }
                }
        );
        sendtodoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExtraMeasuresSQLhandler extraMeasuresSQLhandler = new ExtraMeasuresSQLhandler(getContext());
                String HighOVRLow = String.format("%s/%s", HP.getText().toString(), LP.getText().toString());
                extraMeasuresSQLhandler.execute("p",uname,HighOVRLow);

            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.measuretype,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.Calc_Bpressure:
                Calc_BloodPressure Cal= new Calc_BloodPressure(uname);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.Fragment_container, Cal)
                        .addToBackStack(null)
                        .commit();

                return true;

            case R.id.Calc_Diabetes:
                Calc_Diabetes Dia= new Calc_Diabetes(uname);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.Fragment_container, Dia)
                        .addToBackStack(null)
                        .commit();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}