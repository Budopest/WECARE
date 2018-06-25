package com.gp.eece2019.wecare.staticfragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gp.eece2019.wecare.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {

    Button e1,e2,e3;
    TextView phone1,phone2,phone3;

    public Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

            e1 = getView().findViewById(R.id.e1);
            e2 = getView().findViewById(R.id.e2);
            e3 = getView().findViewById(R.id.e3);
        phone1 = getView().findViewById(R.id.ph1);
        phone2 = getView().findViewById(R.id.ph2);
        phone3 = getView().findViewById(R.id.ph3);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        phone1.setHint(getContext().getSharedPreferences("STATENUMBERS", MODE_PRIVATE).getString("state1",""));
        phone2.setHint(getContext().getSharedPreferences("STATENUMBERS", MODE_PRIVATE).getString("state2",""));
        phone3.setHint(getContext().getSharedPreferences("STATENUMBERS", MODE_PRIVATE).getString("state3",""));

        e1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(phone1.getText().toString().equals("") || phone1.getText().toString().length()!=11)
                {
                        phone1.setText("");
                        phone1.setHint("Enter a valid number then select edit");
                }
                else getContext().getSharedPreferences("STATENUMBERS", MODE_PRIVATE).edit()
                        .putString("state1",phone1.getText().toString()).apply();
            }
        });

        e2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(phone2.getText().toString().equals("") || phone2.getText().toString().length()!=11)
                {   phone2.setText("");
                    phone2.setHint("Enter a valid number then select edit");
                }
                else getContext().getSharedPreferences("STATENUMBERS", MODE_PRIVATE).edit()
                        .putString("state2",phone2.getText().toString()).apply();

            }
        });
        e3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phone3.getText().toString().equals("") || phone3.getText().toString().length()!=11)
                {   phone3.setText("");
                    phone3.setHint("Enter a valid number then select edit");
                }
                else getContext().getSharedPreferences("STATENUMBERS", MODE_PRIVATE).edit()
                        .putString("state3",phone3.getText().toString()).apply();
            }
        });



        super.onActivityCreated(savedInstanceState);
    }
}
