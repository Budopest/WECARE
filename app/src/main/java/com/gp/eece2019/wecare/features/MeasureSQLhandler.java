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
import com.gp.eece2019.wecare.login.IPSTRING;
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
    IPSTRING Surl = new IPSTRING();
    MeasureSQLhandler (Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(String... params) { // check string params

        String measures_url = Surl.Getmeasures();  //Add the url here

            try {
                String user_name = params[0]; // the user name in case of testing account the user name will be "Test_user"
                URL url = new URL(measures_url);
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
        //alertDialog = new AlertDialog.Builder(ctx).create(); //Alert dialog for testing to show
        //alertDialog.setTitle("Connection Status");               //the recieved response from the data base

    }

    @Override
    protected void onPostExecute(String result) {

        if(error==0){
            //alertDialog.setMessage(result);
            //alertDialog.show();
            int indexdatestart=0;   int indexelementstart=0; boolean ins=true;
            boolean end = false;    boolean firstelement =false; boolean inside = false;
            String date="";
            String element="";
            MeasureSQLLITE M = new MeasureSQLLITE(ctx);
            boolean isinserted = false;

            for(int i =0;i<result.length();i++)
            {
                if(result.charAt(i)=='|' && !end)          {indexdatestart=i+1; end=true;}
                if(result.charAt(i)==',' && !firstelement) {date = result.substring(indexdatestart,i); firstelement=true;}

                if(result.charAt(i)=='(' && !inside) {indexelementstart=i+1; inside=true;}
                if(result.charAt(i)==')' && inside)  {
                    element = result.substring(indexelementstart,i); inside=false;

                    int T; int P; int TF; int PF;
                    int indexS =0;
                    String all[] = new String[4];
                    int indexall=0;

                    for(int j =0;j<element.length();j++)
                    {
                        if(element.charAt(j)==',')
                        {
                            all[indexall] = element.substring(indexS, j);
                            indexall++; indexS=j+1;
                        }
                        if(j==element.length()-1)
                        {
                            all[indexall] = element.substring(indexS, j+1);
                        }
                    }
                    T = Integer.parseInt(all[0]);
                    TF = Integer.parseInt(all[1]);
                    P = Integer.parseInt(all[2]);
                    PF = Integer.parseInt(all[3]);

                    isinserted = M.insertData(T,TF,P,PF);
                    if (isinserted&&ins){
                        Toast.makeText(ctx, "Data Inserted", Toast.LENGTH_LONG).show();
                        ins = false;
                    }
                }
            }
            Cursor res = M.getAllData();
            if(res.getCount() == 0) {
                // show message
                showMessage("Error","Nothing found");
                return;
            }

            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                buffer.append("Id :"+ res.getString(0)+"\n");
                buffer.append("T :"+ res.getString(1)+"\n");
                buffer.append("TF :"+ res.getString(2)+"\n");
                buffer.append("P :"+ res.getString(3)+"\n");
                buffer.append("PF :"+ res.getString(4)+"\n\n");
            }

            // Show all data
            showMessage("Data",buffer.toString());

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