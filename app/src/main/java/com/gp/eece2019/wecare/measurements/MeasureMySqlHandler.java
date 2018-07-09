package com.gp.eece2019.wecare.measurements;

/**
 * Created by budopest on 08/04/18.
 */

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gp.eece2019.wecare.R;
import com.gp.eece2019.wecare.shared.URL_STRING;
import com.gp.eece2019.wecare.staticfragments.DoctorDetailsSQLliteHandler;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class MeasureMySqlHandler extends AsyncTask<String,Void,String> {

    Context ctx;
    int error=0;

    //String type="";
    String user_name;
    String idL;
    AlertDialog alertDialog;
    boolean casethree=false;
    boolean casefour=false;
    boolean casefive=false;

    MeasureSQLiteHandler measureSQLiteHandler;

    URL_STRING Surl = new URL_STRING();
    public MeasureMySqlHandler(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(String... params) { // check string params

        String measures_url = Surl.Getmeasures();  //Add the url here

            try {
                //type = params[1];
                user_name = params[0]; // the user name in case of testing account the user name will be "Test_user"
                idL = params[1];
                URL url = new URL(measures_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                      + URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(idL,"UTF-8") ;
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            }

            catch(Exception e){e.printStackTrace(); error++;}

        return null;
    }

    @Override
    protected void onPreExecute() {
        //alertDialog = new AlertDialog.Builder(ctx).create(); //Alert dialog for testing to show
        //alertDialog.setTitle("Connection Status");
    }

    @Override
    protected void onPostExecute(String result) {

        //alertDialog.setMessage(result);
        //alertDialog.show();

        if(error==0){


            // Handling the response
            String conf=""; String messagesCount =""; String messagesOnly="";
            int start =0;   int c=0;
            boolean s = true;

            for(int i=0;i<result.length();i++) {

                if(result.charAt(i)=='|' && s ) {   start=i+1; s=false; continue;  }
                if(result.charAt(i)=='|' && !s) {
                    if(c==0) { conf=result.substring(start, i); if(!conf.equals("success")) break; start=i+1; c++; continue;  }
                    if(c==1) { messagesCount= result.substring(start, i); start=i+1; c++; continue;}
                    if(c==2) { messagesOnly= result.substring(start, i); break;}
                }

            }
            if(conf.equals("success")) {

                int messageStart = 0;
                String temperature = "";
                String temperature_condition = "";
                String heartrate = "";
                String heartrate_condition = "";
                String date="";
                String id="";
                boolean inside = false;
                int segment = 0;
                {

                    for (int i = 0; i < messagesOnly.length(); i++) {

                        if (messagesOnly.charAt(i) == '(' && !inside) {
                            messageStart = i + 1;
                            inside = true;
                            continue;
                        }
                        if (messagesOnly.charAt(i) == ',' && inside) {
                            if (segment == 0) {
                                temperature = messagesOnly.substring(messageStart, i);
                                messageStart = i + 1;
                                segment++;
                                continue;
                            }
                            if (segment == 1) {
                                temperature_condition = messagesOnly.substring(messageStart, i);
                                messageStart = i + 1;
                                segment++;
                                continue;
                            }
                            if(segment==2)
                            {
                                heartrate = messagesOnly.substring(messageStart, i);
                                messageStart = i + 1;
                                segment++;
                                continue;
                            }
                            if(segment==3){
                                heartrate_condition = messagesOnly.substring(messageStart, i);
                                messageStart = i + 1;
                                segment++;
                                continue;
                            }
                            if(segment==4){
                                date = messagesOnly.substring(messageStart, i);
                                messageStart = i + 1;
                                segment++;
                                continue;
                            }
                            /*
                            if(segment==5){
                                id = messagesOnly.substring(messageStart, i);
                                messageStart = i + 1;
                                segment++;
                                continue;
                            }
                            */
                        }
                        if (messagesOnly.charAt(i) == ')' && inside) {
                            id = messagesOnly.substring(messageStart, i);
                            inside = false;
                            segment = 0;
                            Insert(temperature, temperature_condition, heartrate,heartrate_condition,date,id);
                            continue;
                        }

                    }
                }

                if(casefive) Automaticcall(5);
                else if (casefour) Automaticcall(4);
                else if (casethree) Automaticcall(3);
            }

            MeasureMySqlHandler measureMySqlHandler = new MeasureMySqlHandler(ctx);
            measureMySqlHandler.execute(user_name,idL);



        }

    }

    private void Insert(String t, String t_c, String hr,String hr_c,String date, String id) {

        measureSQLiteHandler = new MeasureSQLiteHandler(ctx);

        try{

            int I_t = Integer.parseInt(t);
            int I_tc = Integer.parseInt(t_c);
            int I_hr = Integer.parseInt(hr);
            int I_hrc = Integer.parseInt(hr_c);

            if(I_tc==3 || I_hrc==3){
                casethree = true;
            }
            else if(I_tc==4 || I_hrc==4){
                casefour =true;
            }
            else if(I_tc==5 || I_hrc==5){
                casefive = true;
            }

            measureSQLiteHandler.insertData(I_t,I_tc,I_hr,I_hrc,date,id);
            //
            ArrayList<Measurementitem> alldata = new ArrayList<Measurementitem>();

            Cursor res =measureSQLiteHandler.getAllData();

            if(res.getCount()!=0)
            {
                res.moveToLast(); boolean last=true;
                while(res.moveToPrevious())
                {
                    if(last) {res.moveToLast(); last=false;  idL = res.getString(6);}
                    String T_c  = "Normal";
                    String HR_c = "Normal";
                    if(res.getString(2).equals("3"))  T_c="UP NORMAL";
                    else if(res.getString(2).equals("4") || res.getString(2).equals("5")) T_c="DANGEROUS";

                    if(res.getString(4).equals("3"))  HR_c="UP NORMAL";
                    else if(res.getString(4).equals("4") || res.getString(4).equals("5")) HR_c="DANGEROUS";

                    alldata.add(new Measurementitem(res.getString(1),T_c,res.getString(3),HR_c,res.getString(5),res.getString(6)));
                }
            }
            else{
                Toast toast = Toast.makeText(ctx,"No measurement are received yet",Toast.LENGTH_LONG);
                toast.show();
            }

            MeasurementsAdapter a = new MeasurementsAdapter(ctx, alldata);

            // Get a reference to the ListView, and attach the adapter to the listView.
            ListView listView = (ListView)((Activity)ctx).findViewById(R.id.measure_list);
            listView.setAdapter(a);

        }
        catch (Exception e){}

        /*

        ListView mesaage_list = ((Activity) context).findViewById(R.id.messages_list);

        Cursor res = MSQLLITE.getAllData();

        final ListViewItem[] items = new ListViewItem[res.getCount()];

        if (res.getCount() == 0) {
            // show message
            showMessage("Error", "Nothing found");

        }
        else {
            int i = 0;
            while (res.moveToNext()) {

                if (res.getString(4).equals("send")) {
                    items[i] = new ListViewItem(res.getString(1), CustomAdapter.TYPE_send);
                } else {
                    items[i] = new ListViewItem(res.getString(1) , CustomAdapter.TYPE_rec);
                }
                ID=res.getString(3);
                i++;
            }


            CustomAdapter customAdapter = new CustomAdapter(context, R.id.message_container, items);
            mesaage_list.setAdapter(customAdapter);
        }

        */
    }





    private void Automaticcall(int f) {
        Intent intent = new Intent(Intent.ACTION_CALL);

        if(f==3) intent.setData(Uri.parse("tel:"+ctx.getSharedPreferences("STATENUMBERS", MODE_PRIVATE).getString("state1",null)));
        else if (f==4) intent.setData(Uri.parse("tel:"+ctx.getSharedPreferences("STATENUMBERS", MODE_PRIVATE).getString("state2",null)));
        else if(f==5) intent.setData(Uri.parse("tel:"+ctx.getSharedPreferences("STATENUMBERS", MODE_PRIVATE).getString("state3",null)));
        else return;
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        /*
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }*/
        ctx.startActivity(intent);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}