package com.gp.eece2019.wecare.messanger;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.widget.ListView;

import com.gp.eece2019.wecare.R;
import com.gp.eece2019.wecare.shared.URL_STRING;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class RecMessageSQLhandler extends AsyncTask<String,Void,String> {

    Context context;
    AlertDialog alertDialog;
    MessagesSqlLitehandler MSQLLITE;
    URL_STRING Surl = new URL_STRING();
    String UN,ID;
    String id="";

    int error=0;
    //AlertDialog alertDialog;
    RecMessageSQLhandler (Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) { // check string params

        String message_url = Surl.getRecMessage_url();

        try {
            String user_name = params[0];
            String LatestID  = params[1];
            UN= params[0];
            ID=params[1];
            id=params[1];

            URL url = new URL(message_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                              +URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(LatestID,"UTF-8");
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
        //alertDialog = new AlertDialog.Builder(context).create();
        //alertDialog.setTitle("Connection Status");

    }

    @Override
    protected void onPostExecute(String result) {

        if(error==0) {

            //alertDialog.setMessage(result);
            //alertDialog.show();

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
                String message = "";
                String date = "";
                String ID = "";
                String TYPE = "";
                boolean inside = false;
                int segment = 0;
                if(!id.equals("0"))
                {
                for (int i = 0; i < messagesOnly.length(); i++) {

                    if (messagesOnly.charAt(i) == '(' && !inside) {
                        messageStart = i + 1;
                        inside = true;
                        continue;
                    }
                    if (messagesOnly.charAt(i) == ',' && inside) {
                        if (segment == 0) {
                            message = messagesOnly.substring(messageStart, i);
                            messageStart = i + 1;
                            segment++;
                            continue;
                        }
                        if (segment == 1) {
                            date = messagesOnly.substring(messageStart, i);
                            messageStart = i + 1;
                            segment++;
                            continue;
                        }
                    }
                    if (messagesOnly.charAt(i) == ')' && inside) {
                        ID = messagesOnly.substring(messageStart, i);
                        inside = false;
                        segment = 0;
                        Insert(message, date, ID,"rec");
                        continue;
                    }

                }
            }
            else
                {


                    for (int i = 0; i < messagesOnly.length(); i++) {

                        if (messagesOnly.charAt(i) == '(' && !inside) {
                            messageStart = i + 1;
                            inside = true;
                            continue;
                        }
                        if (messagesOnly.charAt(i) == ',' && inside) {
                            if (segment == 0) {
                                message = messagesOnly.substring(messageStart, i);
                                messageStart = i + 1;
                                segment++;
                                continue;
                            }
                            if (segment == 1) {
                                date = messagesOnly.substring(messageStart, i);
                                messageStart = i + 1;
                                segment++;
                                continue;
                            }
                            if(segment==2)
                            {
                                ID = messagesOnly.substring(messageStart, i);
                                messageStart = i + 1;
                                segment++;
                                continue;
                            }
                        }
                        if (messagesOnly.charAt(i) == ')' && inside) {
                            TYPE = messagesOnly.substring(messageStart, i);
                            inside = false;
                            segment = 0;
                            Insert(message, date, ID,TYPE);
                            continue;
                        }

                    }
                }
            }

        }

        RecMessageSQLhandler RecCheck = new RecMessageSQLhandler(context);
        RecCheck.execute(UN,ID);


    }

    private void Insert(String message, String date, String id,String type) {

        MSQLLITE = new MessagesSqlLitehandler(context);
        MSQLLITE.insertData(message, date, id, type);
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

