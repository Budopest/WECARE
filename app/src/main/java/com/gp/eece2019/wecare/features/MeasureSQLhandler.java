package com.gp.eece2019.wecare.features;

/**
 * Created by budopest on 08/04/18.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;
import android.widget.Toast;

import com.gp.eece2019.wecare.MainActivity;
import com.gp.eece2019.wecare.R;
import com.gp.eece2019.wecare.calls.Contactssqllitehandler;
import com.gp.eece2019.wecare.login.USERsqllitehandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class MeasureSQLhandler extends AsyncTask<String,Void,String> {

    Context ctx;
    int error=0;

    AlertDialog alertDialog;
    MeasureSQLhandler (Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(String... params) { // check string params

        String login_url = "http://192.168.1.28/test/";  //Add the url here

            try {
                String user_name = params[0]; // the user name in case of testing account the user name will be "Test_user"
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8");
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
        //alertDialog = new AlertDialog.Builder(context).create(); //Alert dialog for testing to show
        //alertDialog.setTitle("Connection Status");               //the recieved response from the data base

    }

    @Override
    protected void onPostExecute(String result) {

        if(error==0){    //insert the code that handles the result here
            //alertDialog.setMessage(result);
            //alertDialog.show();

            // The below code display the recieved measures in the form:
            //temp(1,2,3,4)+hrate(5,6,9,8)  the only thing that matters is the () and what is inside of them
            // "temp" "+" "hrate" all doesn't matter

            TextView temp = (TextView)((Activity)ctx).findViewById(R.id.temptext);
            TextView hrate = (TextView)((Activity)ctx).findViewById(R.id.hratetext);
            int indexstart=0; int indexm=0; boolean S=false;
            String Stemp="";
            String Shrate="";
            for(int i =0;i<result.length();i++)
            {
                if(result.charAt(i) == '(')
                {indexstart=i; S=true;}
                if(S && result.charAt(i)==')')
                {S=false;
                if(indexm==0) {Stemp = result.substring(indexstart+1,i); indexm=1;}
                else Shrate=result.substring(indexstart,i);
                }
            }
            temp.setText(Stemp);
            hrate.setText(Shrate);



        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}