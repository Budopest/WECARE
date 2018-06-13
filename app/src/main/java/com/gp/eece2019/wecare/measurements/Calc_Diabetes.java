package com.gp.eece2019.wecare.measurements;


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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gp.eece2019.wecare.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Calc_Diabetes extends Fragment {


    public Calc_Diabetes() {
        // Required empty public constructor
    }
    RadioGroup RG;
    RadioButton RB;
    EditText HDia;
    // EditText LDia;
    TextView CheckCondition;
    Button Check;
    Button sendtodoc;
    String test_type;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diabetes, container, false);
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RG= getView().findViewById(R.id.states);
        //LDia = (EditText) getView().findViewById(R.id.low_dia);
        HDia = (EditText) getView().findViewById(R.id.high_dia);
        Check = (Button) getView().findViewById(R.id.Check_your_condition);
        CheckCondition = (TextView) getView().findViewById(R.id.Condition_description);
        sendtodoc = (Button) getView().findViewById(R.id.Send_to_doctor);

    }


    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Check.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String HDiaMeasure = HDia.getText().toString();
                        // String LDiaMeasure = LDia.getText().toString();
                        if (HDiaMeasure.equals("")) {
                            Toast.makeText(getActivity(), "Data is incorrect", Toast.LENGTH_LONG).show();
                            return;
                        }
                        int HDiameasurement = Integer.parseInt(HDiaMeasure);

                        int radioId = RG.getCheckedRadioButtonId();
                        RB =(RadioButton) getView().findViewById(radioId);
                        if (radioId==R.id.Random) test_type = "R";
                        else if (radioId==R.id.Fasting) test_type = "F";
                        else if (radioId==R.id.two_hour_post_prandial) test_type = "P";



                        if ((HDiameasurement < 200 && test_type == "R") || (HDiameasurement < 140 && test_type == "P") || (HDiameasurement < 100 && test_type == "F")) {
                            CheckCondition.setText("normal");
                            CheckCondition.setTextColor(Color.GREEN);
                        } else if ((HDiameasurement >= 140 && HDiameasurement <= 199 && test_type == "P") || (HDiameasurement >= 100 && HDiameasurement <= 125 && test_type == "F")) {
                            CheckCondition.setText("Prediabetes");
                            CheckCondition.setTextColor(Color.YELLOW);
                        } else if ((HDiameasurement >= 200 && test_type == "R") || (HDiameasurement >= 126 && test_type == "F") || (HDiameasurement >= 200 && test_type == "P"))
                        { CheckCondition.setText("Diabetes");
                            CheckCondition.setTextColor(Color.RED);
                        }}}
        );
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
                Calc_BloodPressure Cal= new Calc_BloodPressure();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.Fragment_container, Cal)
                        .addToBackStack(null)
                        .commit();

                return true;

            case R.id.Calc_Diabetes:
                Calc_Diabetes Dia= new Calc_Diabetes();
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