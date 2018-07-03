package com.gp.eece2019.wecare.measurements;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

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


public class ExtraMeasuresSQLhandler extends AsyncTask<String,Void,String> {

    Context ctx;
    int error=0;
    AlertDialog alertDialog;
    IPSTRING Surl = new IPSTRING();
    ExtraMeasuresSQLhandler (Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(String... params) { // check string params



        try {
            String type = params[0]; // the user name in case of testing account the user name will be "Test_user"
            String user_name = params[1];
            String data = params[2];
            String dataurl;

            if(type.equals("p")) dataurl=Surl.getPressure_url();
            else dataurl=Surl.getDiabetes_url();

            URL url = new URL(dataurl);

            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data;
            if(type.equals("p"))
                post_data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"+
                    URLEncoder.encode("pressure","UTF-8")+"="+URLEncoder.encode(data,"UTF-8");
            else
                post_data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"+
                        URLEncoder.encode("diabetes","UTF-8")+"="+URLEncoder.encode(data,"UTF-8");


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
        if(error==0)
        {


        }


    }


    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}