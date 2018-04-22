package com.gp.eece2019.wecare.notification;


import android.content.Context;
import android.database.Cursor;
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


public class MedicineSQLhandler extends AsyncTask<String,Void,String> {

    Context ctx;
    int error=0;
    IPSTRING Surl = new IPSTRING();
    AlertDialog alertDialog;
    MedicineSQLhandler (Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(String... params) { // check string params

        String medicine_url = Surl.Getmedicine();  //Add the url here

        try {
            String user_name = params[0]; // the user name in case of testing account the user name will be "Test_user"
            URL url = new URL(medicine_url);
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

        alertDialog = new AlertDialog.Builder(ctx).create(); //Alert dialog for testing to show
        alertDialog.setTitle("Connection Status");               //the recieved response from the data base

    }

    @Override
    protected void onPostExecute(String result) {

        if(error==0) {
            alertDialog.setMessage(result);
            alertDialog.show();
            Medicinesqllitehandler MSQL = new Medicinesqllitehandler(ctx);

            int num=0; boolean first=false; int indexstart=0;
            for(int i =0;i<result.length();i++)
            {
                if(result.charAt(i)==',') {num++;}
            }
            num = (num+1)/2;
            String name[] = new String[num];
            int    dose[] = new int[num]; int halfc=0;
            int c1=0; int c2=0;
            for(int i =0;i<result.length();i++)
            {
                if(result.charAt(i)=='|' && !first) {indexstart=i+1; first=true; continue; }
                if(result.charAt(i)==',') {
                    if(halfc<num) {name[c1]=result.substring(indexstart,i); c1++; indexstart=i+1; halfc++; }
                    else {dose[c2]=Integer.parseInt(result.substring(indexstart,i)); c2++; indexstart=i+1; halfc++; }
                }
                if(i==result.length()-1) { dose[c2]=Integer.parseInt(result.substring(indexstart,i)); }

            }
            Cursor res = MSQL.getAllData();
            if(res.getCount() == 0) {
                for(int i=0;i<num;i++)
                {
                    MSQL.insertData(name[i],dose[i]);
                }
            }
            else{
            StringBuffer buffer = new StringBuffer();
               boolean found = false;
            for(int i=0;i<num;i++)
            {
                while (res.moveToNext()) {
                    String s1 = res.getString(0); //ID
                    String s2= res.getString(1); //name
                    String s3= res.getString(2); //dose
                if((s2==name[i])&&(s3.equals(Integer.toString(dose[i])))) {found=true; break;}
                else if((s2==name[i])&&!(s3.equals(Integer.toString(dose[i])))){MSQL.updateData(s1,name[i],dose[i]); found=true; break;}
                }
                if(!found) MSQL.insertData(name[i],dose[i]);

                }
            }

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