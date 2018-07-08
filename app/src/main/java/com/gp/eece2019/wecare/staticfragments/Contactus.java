package com.gp.eece2019.wecare.staticfragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public class Contactus extends Fragment {

    String uname;

    public Contactus(String uname) {
        // Required empty public constructor
        this.uname = uname;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contactus, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        Button sendfeedback = getView().findViewById(R.id.feedbacksend);
        final TextView feedback   = getView().findViewById(R.id.feedback);

        sendfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedbackSender feedbackSender = new FeedbackSender(getContext());
                feedbackSender.execute(uname,feedback.getText().toString());
            }
        });

        super.onActivityCreated(savedInstanceState);
    }
}
