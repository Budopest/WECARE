package com.gp.eece2019.wecare.messanger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gp.eece2019.wecare.MainActivity;
import com.gp.eece2019.wecare.R;
import com.gp.eece2019.wecare.calls.Contactssqllitehandler;
import com.gp.eece2019.wecare.login.USERsqllitehandler;
import com.gp.eece2019.wecare.shared.IPSTRING;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class SendMessageSQLhandler extends AsyncTask<String,Void,String> {

    Context context;
    AlertDialog alertDialog;
    MessagesSqlLitehandler MSQLLITE;
    String message_final;
    IPSTRING Surl = new IPSTRING();

    int error=0;
    //AlertDialog alertDialog;
    SendMessageSQLhandler (Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) { // check string params

        String message_url = Surl.getSendMessage_url();

            try {
                String user_name = params[0];
                String message   = params[1];
                message_final = params[1];
                String time      = params[2];
                URL url = new URL(message_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                        +URLEncoder.encode("content","UTF-8")+"="+URLEncoder.encode(message,"UTF-8");
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
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Connection Status");

    }

    @Override
    protected void onPostExecute(String result) {

        if(error==0){

            //alertDialog.setMessage(result);
            //alertDialog.show();

            int length = result.length();
            String conf ="",time = "",id="";
            boolean ft=true;
            int c=0;
            int start=0;
            for(int i=0;i<length;i++)
            {
                if(result.charAt(i)=='|'&& ft){
                    start=i;
                    ft = false;
                    continue;
                }
                if(result.charAt(i)=='|'&& (!ft)) {

                    if(c==0) { conf = result.substring(start+1,i); c++; start=i+1;}
                    else if(c==1) {time=result.substring(start,i); c++; start=i+1;}
                    else if(c==2) {id=result.substring(start,i);}

                }
            }
            //alertDialog.setMessage(conf + time +id);
            //alertDialog.show();


            MSQLLITE = new MessagesSqlLitehandler(context);
            MSQLLITE.insertData(message_final,time,id,"send");

            TextView ms_area = ((Activity)context).findViewById(R.id.messanger_area);
            ListView mesaage_list = ((Activity)context).findViewById(R.id.messages_list);
            ms_area.setText("");

            Cursor res =MSQLLITE.getAllData();

            final ListViewItem[] items = new ListViewItem[res.getCount()];

            if(res.getCount() == 0) {
                // show message
                showMessage("Error","Nothing found");
                return;
            }


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
                i++;
            }


            CustomAdapter customAdapter = new CustomAdapter(context, R.id.message_container, items);
            mesaage_list.setAdapter(customAdapter);

            //else Toast.makeText(context, "Check your user name and password", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}
