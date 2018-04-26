package com.gp.eece2019.wecare.staticfragments;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gp.eece2019.wecare.R;
import com.gp.eece2019.wecare.login.USERsqllitehandler;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class Userinfo extends Fragment {


    //USERsqllitehandler usql;
    String Fname,Lname,Uname,phone,Bdate;
    TextView info;
    Doctordetails D;

    public Userinfo(String f,String l,String u,String p,String d) {
        Fname = f;
        Lname = l;
        Uname = u;
        phone = p;
        Bdate = d;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_userinfo, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        info =  getView().findViewById(R.id.Personal_info);



    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        D=new Doctordetails(getActivity());
        D.execute(Uname);

        info.setText("First name: " + Fname + "\n" +
                     "Lastname: "+ Lname + "\n" +
                     "User name: " + Uname + "\n" +
                     "PHONE number: "+ phone + "\n" +
                     "Birth Date: " + Bdate + "\n\n"
        );

        super.onActivityCreated(savedInstanceState);
    }


}
