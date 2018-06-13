package com.gp.eece2019.wecare.messanger;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.gp.eece2019.wecare.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Messanger extends Fragment {


    public Messanger() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messanger, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        ListView mesaage_list = getView().findViewById(R.id.messages_list);

        //testing to be changerd later.
        boolean turn = false;
        final ListViewItem[] items = new ListViewItem[20];
        for (int i = 0; i < items.length; i++){

            if(turn)  items[i] = new ListViewItem("sent message " + i, CustomAdapter.TYPE_send);
            else      items[i] = new ListViewItem("Rec  message " + i, CustomAdapter.TYPE_rec);
            turn = !turn;

        }
        // the prev part to be modified.
        CustomAdapter customAdapter = new CustomAdapter(getContext(), R.id.message_container, items);
        mesaage_list.setAdapter(customAdapter);



        super.onActivityCreated(savedInstanceState);
    }
}
