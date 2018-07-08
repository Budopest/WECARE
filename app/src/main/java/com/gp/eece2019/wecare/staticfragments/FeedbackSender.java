package com.gp.eece2019.wecare.staticfragments;

/**
 * Created by budopest on 08/04/18.
 */

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gp.eece2019.wecare.R;
import com.gp.eece2019.wecare.calls.Contactssqllitehandler;
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

import static android.content.Context.MODE_PRIVATE;


public class FeedbackSender extends AsyncTask<String,Void,String> {

    Context ctx;
    int error=0;
    AlertDialog alertDialog;
    URL_STRING Surl = new URL_STRING();
    FeedbackSender(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(String... params) { // check string params

        String doctor_url = Surl.getFeedback_url();  //Add the url here

        try {
            String user_name = params[0]; // the user name in case of testing account the user name will be "Test_user"
            String feedback  = params[1];
            URL url = new URL(doctor_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"+
                    URLEncoder.encode("feedback","UTF-8")+"="+URLEncoder.encode(feedback,"UTF-8");
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
        alertDialog = new AlertDialog.Builder(ctx).create(); //Alert dialog for testing to show
        alertDialog.setTitle("Thanks");
    }

    @Override
    protected void onPostExecute(String result) {
        //alertDialog.setMessage(result);
        //alertDialog.show();
        if(error==0)
        {

            alertDialog.setMessage("Thank you for sending your feedback.");
            alertDialog.show();
        }


    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}