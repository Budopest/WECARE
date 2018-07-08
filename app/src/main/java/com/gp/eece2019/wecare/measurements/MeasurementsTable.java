package com.gp.eece2019.wecare.measurements;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gp.eece2019.wecare.R;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class MeasurementsTable extends Fragment {

    String username;
    MeasureSQLiteHandler M;

    public MeasurementsTable(String username) {
        // Required empty public constructor
        this.username=username;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_measurements_table, container, false);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.display_type,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        TextView t1 = getView().findViewById(R.id.T11);
        TextView t2 = getView().findViewById(R.id.T12);
        TextView t3 = getView().findViewById(R.id.T13);
        TextView t4 = getView().findViewById(R.id.T14);
        TextView t5 = getView().findViewById(R.id.T21);
        TextView t6 = getView().findViewById(R.id.T22);
        TextView t7 = getView().findViewById(R.id.T23);
        TextView t8 = getView().findViewById(R.id.T24);
        TextView t9 = getView().findViewById(R.id.T31);
        TextView t10 = getView().findViewById(R.id.T32);
        TextView t11 = getView().findViewById(R.id.T33);
        TextView t12 = getView().findViewById(R.id.T34);
        TextView t13 = getView().findViewById(R.id.T41);
        TextView t14 = getView().findViewById(R.id.T42);
        TextView t15 = getView().findViewById(R.id.T43);
        TextView t16 = getView().findViewById(R.id.T44);
        TextView t17 = getView().findViewById(R.id.T51);
        TextView t18 = getView().findViewById(R.id.T52);
        TextView t19 = getView().findViewById(R.id.T53);
        TextView t20= getView().findViewById(R.id.T54);
        TextView t21 = getView().findViewById(R.id.T61);
        TextView t22 = getView().findViewById(R.id.T62);
        TextView t23 = getView().findViewById(R.id.T63);
        TextView t24 = getView().findViewById(R.id.T64);
        TextView t25 = getView().findViewById(R.id.T71);
        TextView t26 = getView().findViewById(R.id.T72);
        TextView t27 = getView().findViewById(R.id.T73);
        TextView t28 = getView().findViewById(R.id.T74);
        TextView t29 = getView().findViewById(R.id.T81);
        TextView t30 = getView().findViewById(R.id.T82);
        TextView t31 = getView().findViewById(R.id.T83);
        TextView t32 = getView().findViewById(R.id.T84);
        TextView t33 = getView().findViewById(R.id.T91);
        TextView t34 = getView().findViewById(R.id.T92);
        TextView t35 = getView().findViewById(R.id.T93);
        TextView t36 = getView().findViewById(R.id.T94);
        TextView t37 = getView().findViewById(R.id.T101);
        TextView t38 = getView().findViewById(R.id.T102);
        TextView t39 = getView().findViewById(R.id.T103);
        TextView t40 = getView().findViewById(R.id.T104);
        TextView tdate = getView().findViewById(R.id.updateddate);
        Button b= getView().findViewById(R.id.showall);


        M = new MeasureSQLiteHandler(getActivity());
        MeasureSQLiteHandler MLITE = new MeasureSQLiteHandler(getActivity());
        Cursor res =MLITE.getAllData();
        int counter=10;
        if(res.getCount()!=0)
        {
            res.moveToLast();
            while(res.moveToPrevious() && counter>0)
            {

                if(counter==10){
                    res.moveToLast();
                    String T_c  = "Normal";
                    String HR_c = "Normal";
                    if(res.getString(2).equals("3"))  T_c="UP NORMAL";
                    else if(res.getString(2).equals("4") || res.getString(2).equals("5")) T_c="DANGEROUS";
                    if(res.getString(4).equals("3"))  HR_c="UP NORMAL";
                    else if(res.getString(4).equals("4") || res.getString(4).equals("5")) HR_c="DANGEROUS";

                    t1.setText(res.getString(1));
                    if(T_c.equals("UP NORMAL")) t2.setTextColor(Color.GRAY);
                    if(T_c.equals("DANGEROUS")) t2.setTextColor(Color.RED);
                    t2.setText(T_c);
                    t3.setText(res.getString(3));
                    if(HR_c.equals("UP NORMAL")) t4.setTextColor(Color.GRAY);
                    if(HR_c.equals("DANGEROUS")) t4.setTextColor(Color.RED);
                    t4.setText(HR_c);
                    counter--;
                }
                else if(counter==9){
                    String T_c  = "Normal";
                    String HR_c = "Normal";
                    if(res.getString(2).equals("3"))  T_c="UP NORMAL";
                    else if(res.getString(2).equals("4") || res.getString(2).equals("5")) T_c="DANGEROUS";
                    if(res.getString(4).equals("3"))  HR_c="UP NORMAL";
                    else if(res.getString(4).equals("4") || res.getString(4).equals("5")) HR_c="DANGEROUS";

                    t5.setText(res.getString(1));
                    if(T_c.equals("UP NORMAL")) t6.setTextColor(Color.GRAY);
                    if(T_c.equals("DANGEROUS")) t6.setTextColor(Color.RED);
                    t6.setText(T_c);
                    t7.setText(res.getString(3));
                    if(HR_c.equals("UP NORMAL")) t8.setTextColor(Color.GRAY);
                    if(HR_c.equals("DANGEROUS")) t8.setTextColor(Color.RED);
                    t8.setText(HR_c);
                    counter--;
                }
                else if(counter==8){
                    String T_c  = "Normal";
                    String HR_c = "Normal";
                    if(res.getString(2).equals("3"))  T_c="UP NORMAL";
                    else if(res.getString(2).equals("4") || res.getString(2).equals("5")) T_c="DANGEROUS";
                    if(res.getString(4).equals("3"))  HR_c="UP NORMAL";
                    else if(res.getString(4).equals("4") || res.getString(4).equals("5")) HR_c="DANGEROUS";

                    t9.setText(res.getString(1));
                    if(T_c.equals("UP NORMAL")) t10.setTextColor(Color.GRAY);
                    if(T_c.equals("DANGEROUS")) t10.setTextColor(Color.RED);
                    t10.setText(T_c);
                    t11.setText(res.getString(3));
                    if(HR_c.equals("UP NORMAL")) t12.setTextColor(Color.GRAY);
                    if(HR_c.equals("DANGEROUS")) t12.setTextColor(Color.RED);
                    t12.setText(HR_c);
                    counter--;
                }
                else if(counter==7){
                    String T_c  = "Normal";
                    String HR_c = "Normal";
                    if(res.getString(2).equals("3"))  T_c="UP NORMAL";
                    else if(res.getString(2).equals("4") || res.getString(2).equals("5")) T_c="DANGEROUS";
                    if(res.getString(4).equals("3"))  HR_c="UP NORMAL";
                    else if(res.getString(4).equals("4") || res.getString(4).equals("5")) HR_c="DANGEROUS";

                    t13.setText(res.getString(1));
                    if(T_c.equals("UP NORMAL")) t14.setTextColor(Color.GRAY);
                    if(T_c.equals("DANGEROUS")) t14.setTextColor(Color.RED);
                    t14.setText(T_c);
                    t15.setText(res.getString(3));
                    if(HR_c.equals("UP NORMAL")) t16.setTextColor(Color.GRAY);
                    if(HR_c.equals("DANGEROUS")) t16.setTextColor(Color.RED);
                    t16.setText(HR_c);
                    counter--;
                }
                else if(counter==6){
                    String T_c  = "Normal";
                    String HR_c = "Normal";
                    if(res.getString(2).equals("3"))  T_c="UP NORMAL";
                    else if(res.getString(2).equals("4") || res.getString(2).equals("5")) T_c="DANGEROUS";
                    if(res.getString(4).equals("3"))  HR_c="UP NORMAL";
                    else if(res.getString(4).equals("4") || res.getString(4).equals("5")) HR_c="DANGEROUS";

                    t17.setText(res.getString(1));
                    if(T_c.equals("UP NORMAL")) t18.setTextColor(Color.GRAY);
                    if(T_c.equals("DANGEROUS")) t18.setTextColor(Color.RED);
                    t18.setText(T_c);
                    t19.setText(res.getString(3));
                    if(HR_c.equals("UP NORMAL")) t20.setTextColor(Color.GRAY);
                    if(HR_c.equals("DANGEROUS")) t20.setTextColor(Color.RED);
                    t20.setText(HR_c);
                    counter--;
                }
                else if(counter==5){
                    String T_c  = "Normal";
                    String HR_c = "Normal";
                    if(res.getString(2).equals("3"))  T_c="UP NORMAL";
                    else if(res.getString(2).equals("4") || res.getString(2).equals("5")) T_c="DANGEROUS";
                    if(res.getString(4).equals("3"))  HR_c="UP NORMAL";
                    else if(res.getString(4).equals("4") || res.getString(4).equals("5")) HR_c="DANGEROUS";

                    t21.setText(res.getString(1));
                    if(T_c.equals("UP NORMAL")) t22.setTextColor(Color.GRAY);
                    if(T_c.equals("DANGEROUS")) t22.setTextColor(Color.RED);
                    t22.setText(T_c);
                    t23.setText(res.getString(3));
                    if(HR_c.equals("UP NORMAL")) t24.setTextColor(Color.GRAY);
                    if(HR_c.equals("DANGEROUS")) t24.setTextColor(Color.RED);
                    t24.setText(HR_c);
                    counter--;
                }
                else if(counter==4){
                    String T_c  = "Normal";
                    String HR_c = "Normal";
                    if(res.getString(2).equals("3"))  T_c="UP NORMAL";
                    else if(res.getString(2).equals("4") || res.getString(2).equals("5")) T_c="DANGEROUS";
                    if(res.getString(4).equals("3"))  HR_c="UP NORMAL";
                    else if(res.getString(4).equals("4") || res.getString(4).equals("5")) HR_c="DANGEROUS";

                    t25.setText(res.getString(1));
                    if(T_c.equals("UP NORMAL")) t26.setTextColor(Color.GRAY);
                    if(T_c.equals("DANGEROUS")) t26.setTextColor(Color.RED);
                    t26.setText(T_c);
                    t27.setText(res.getString(3));
                    if(HR_c.equals("UP NORMAL")) t28.setTextColor(Color.GRAY);
                    if(HR_c.equals("DANGEROUS")) t28.setTextColor(Color.RED);
                    t28.setText(HR_c);
                    counter--;
                }
                else if(counter==3){
                    String T_c  = "Normal";
                    String HR_c = "Normal";
                    if(res.getString(2).equals("3"))  T_c="UP NORMAL";
                    else if(res.getString(2).equals("4") || res.getString(2).equals("5")) T_c="DANGEROUS";
                    if(res.getString(4).equals("3"))  HR_c="UP NORMAL";
                    else if(res.getString(4).equals("4") || res.getString(4).equals("5")) HR_c="DANGEROUS";

                    t29.setText(res.getString(1));
                    if(T_c.equals("UP NORMAL")) t30.setTextColor(Color.GRAY);
                    if(T_c.equals("DANGEROUS")) t30.setTextColor(Color.RED);
                    t30.setText(T_c);
                    t31.setText(res.getString(3));
                    if(HR_c.equals("UP NORMAL")) t32.setTextColor(Color.GRAY);
                    if(HR_c.equals("DANGEROUS")) t32.setTextColor(Color.RED);
                    t32.setText(HR_c);
                    counter--;
                }
                else if(counter==3){
                    String T_c  = "Normal";
                    String HR_c = "Normal";
                    if(res.getString(2).equals("3"))  T_c="UP NORMAL";
                    else if(res.getString(2).equals("4") || res.getString(2).equals("5")) T_c="DANGEROUS";
                    if(res.getString(4).equals("3"))  HR_c="UP NORMAL";
                    else if(res.getString(4).equals("4") || res.getString(4).equals("5")) HR_c="DANGEROUS";

                    t29.setText(res.getString(1));
                    if(T_c.equals("UP NORMAL")) t30.setTextColor(Color.GRAY);
                    if(T_c.equals("DANGEROUS")) t30.setTextColor(Color.RED);
                    t30.setText(T_c);
                    t31.setText(res.getString(3));
                    if(HR_c.equals("UP NORMAL")) t32.setTextColor(Color.GRAY);
                    if(HR_c.equals("DANGEROUS")) t32.setTextColor(Color.RED);
                    t32.setText(HR_c);
                    counter--;
                }
                else if(counter==2){
                    String T_c  = "Normal";
                    String HR_c = "Normal";
                    if(res.getString(2).equals("3"))  T_c="UP NORMAL";
                    else if(res.getString(2).equals("4") || res.getString(2).equals("5")) T_c="DANGEROUS";
                    if(res.getString(4).equals("3"))  HR_c="UP NORMAL";
                    else if(res.getString(4).equals("4") || res.getString(4).equals("5")) HR_c="DANGEROUS";

                    t33.setText(res.getString(1));
                    if(T_c.equals("UP NORMAL")) t34.setTextColor(Color.GRAY);
                    if(T_c.equals("DANGEROUS")) t34.setTextColor(Color.RED);
                    t34.setText(T_c);
                    t35.setText(res.getString(3));
                    if(HR_c.equals("UP NORMAL")) t36.setTextColor(Color.GRAY);
                    if(HR_c.equals("DANGEROUS")) t36.setTextColor(Color.RED);
                    t36.setText(HR_c);
                    counter--;
                }
                else if(counter==1){
                    String T_c  = "Normal";
                    String HR_c = "Normal";
                    if(res.getString(2).equals("3"))  T_c="UP NORMAL";
                    else if(res.getString(2).equals("4") || res.getString(2).equals("5")) T_c="DANGEROUS";
                    if(res.getString(4).equals("3"))  HR_c="UP NORMAL";
                    else if(res.getString(4).equals("4") || res.getString(4).equals("5")) HR_c="DANGEROUS";

                    t37.setText(res.getString(1));
                    if(T_c.equals("UP NORMAL")) t38.setTextColor(Color.GRAY);
                    if(T_c.equals("DANGEROUS")) t38.setTextColor(Color.RED);
                    t38.setText(T_c);
                    t39.setText(res.getString(3));
                    if(HR_c.equals("UP NORMAL")) t40.setTextColor(Color.GRAY);
                    if(HR_c.equals("DANGEROUS")) t40.setTextColor(Color.RED);
                    t40.setText(HR_c);
                    counter--;
                }



            }
        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = M.getAllData();

                //M.insertData(40,4,88,1);
                if(res.getCount() == 0) {
                    // show message
                    showMessage("Error","Nothing found");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    buffer.append("Measure number :"+ res.getString(0)+"\n");
                    buffer.append("Temperature :"+ res.getString(1)+"\n");
                    buffer.append("Temperature state :"+ res.getString(2)+"\n");
                    buffer.append("Heart rate :"+ res.getString(3)+"\n");
                    buffer.append("Heart rate state :"+ res.getString(4)+"\n\n");
                }

                // Show all data
                showMessage("Data",buffer.toString());
            }
        });



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

                return true;

            case R.id.graphdisplay:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }


}
