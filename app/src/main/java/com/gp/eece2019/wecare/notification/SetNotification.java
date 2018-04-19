package com.gp.eece2019.wecare.notification;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import com.gp.eece2019.wecare.R;

import static android.content.Context.ALARM_SERVICE;

public class SetNotification extends Fragment {


    public SetNotification() {
        // Required empty public constructor
    }
    AlarmManager alarmManager;
    private PendingIntent pending_intent;
    private TimePicker alarmTimePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_notification, container, false);
    }
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {

        // set the alarm to the time that you picked
        final Calendar calendar = Calendar.getInstance();
        alarmTimePicker = (TimePicker) getView().findViewById(R.id.timePicker2);
        // Get the alarm manager service
        alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        final ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();

        Button start_alarm= (Button) getView().findViewById(R.id.setalalrm);
        final Intent myIntent = new Intent(getActivity(), AlarmReceiver.class);
        start_alarm.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)

            @Override
            public void onClick(View v) {

                //calendar.add(Calendar.SECOND, 3);
                final int hour = alarmTimePicker.getCurrentHour();
                final int minute = alarmTimePicker.getCurrentMinute();
                calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
                final int id = (int) System.currentTimeMillis();
                myIntent.putExtra("extra", "alarm on");
                pending_intent = PendingIntent.getBroadcast(getActivity(), id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , pending_intent);
                intentArray.add(pending_intent);

                Toast.makeText(getActivity(), "alarm set to" + " " + hour + " : " + minute, Toast.LENGTH_SHORT).show();

            }});
        Button Repeating_alarm= (Button) getView().findViewById(R.id.Set_repeating_alarm);
        Repeating_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.SECOND, 3);
                //setAlarmText("You clicked a button");

                final int hour = alarmTimePicker.getCurrentHour();
                final int minute = alarmTimePicker.getCurrentMinute();;

                // Log.e("MyActivity", "In the receiver with " + hour + " and " + minute);
                calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());

                myIntent.putExtra("extra", "alarm on");
                pending_intent = PendingIntent.getBroadcast(getActivity(), 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60*60*24, pending_intent);
                Toast.makeText(getActivity(), "alarm set to"+ " " +hour + " : " + minute + "daily", Toast.LENGTH_SHORT).show();
            }
        });

        Button stop_alarm= (Button) getView().findViewById(R.id.Turnoff_alarm);
        stop_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myIntent.putExtra("extra", "alarm off");
                getActivity().sendBroadcast(myIntent);
                alarmManager.cancel(pending_intent);
                Toast.makeText(getActivity(), "alarm canceled", Toast.LENGTH_SHORT).show();
            }
        });

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}