package com.gp.eece2019.wecare.messanger;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gp.eece2019.wecare.R;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class Messanger extends Fragment {

    MessagesSqlLitehandler MSQLLITE;

    String Uname,LatestID;
    public Messanger(String Uname) {
        // Required empty public constructor
        this.Uname = Uname;
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
        Button   send_message = getView().findViewById(R.id.send_button);
        final TextView message_area = getView().findViewById(R.id.messanger_area);

        MSQLLITE = new MessagesSqlLitehandler(getContext());

        Cursor res =MSQLLITE.getAllData();

        final ListViewItem[] items = new ListViewItem[res.getCount()];

        if(res.getCount() == 0) {
            // show message
            showMessage("Error","Nothing found");

        }
        else{
        int i=0;
        while (res.moveToNext()) {

            if(res.getString(4).equals("send"))
            {
                items[i] = new ListViewItem(res.getString(1), CustomAdapter.TYPE_send);
            }
            else
            {
                items[i] = new ListViewItem(res.getString(1)+ i, CustomAdapter.TYPE_rec);
            }
            i++; LatestID=res.getString(3);
        }

        //Collections.reverse(Arrays.asList(items)); 
        CustomAdapter customAdapter = new CustomAdapter(getContext(), R.id.message_container, items);
        mesaage_list.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();

        }


        RecMessageSQLhandler RecCheck = new RecMessageSQLhandler(getContext());
        RecCheck.execute(Uname,LatestID);


        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg  = message_area.getText().toString();
                if(!msg.equals("")) {
                    //String time = DateFormat.getDateTimeInstance().format(new Date());
                    Date cal = Calendar.getInstance().getTime();
                    SimpleDateFormat D = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss.sss");
                    String time = D.format(cal);


                    SendMessageSQLhandler SMS = new SendMessageSQLhandler(getContext());
                    SMS.execute(Uname, msg, time);
                }
            }
        });



        super.onActivityCreated(savedInstanceState);
    }
    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}
