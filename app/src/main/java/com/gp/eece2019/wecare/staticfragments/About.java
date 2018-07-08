package com.gp.eece2019.wecare.staticfragments;


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
public class About extends Fragment {

    String uname;
    public About(String uname) {
        // Required empty public constructor
        this.uname=uname;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        TextView textView = getView().findViewById(R.id.abouttocontactus);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contactus contactus = new Contactus(uname);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.Fragment_container, contactus)
                        .addToBackStack(null)
                        .commit();
            }
        });

        super.onActivityCreated(savedInstanceState);
    }
}
