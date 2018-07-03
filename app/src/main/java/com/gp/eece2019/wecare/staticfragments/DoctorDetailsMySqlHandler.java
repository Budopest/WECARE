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

import static android.content.Context.MODE_PRIVATE;


public class DoctorDetailsMySqlHandler extends AsyncTask<String,Void,String> {

    Context ctx;
    int error=0;
    AlertDialog alertDialog;
    IPSTRING Surl = new IPSTRING();
    DoctorDetailsMySqlHandler(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(String... params) { // check string params

        String doctor_url = Surl.getDoctor_url();  //Add the url here

        try {
            String user_name = params[0]; // the user name in case of testing account the user name will be "Test_user"
            URL url = new URL(doctor_url);
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
        //alertDialog.setTitle("Connection Status");
    }

    @Override
    protected void onPostExecute(String result) {
        //alertDialog.setMessage(result);
        //alertDialog.show();
        if(error==0)
        {
            boolean isInserted1 = false;
            boolean isInserted2 = true;
            int indexSTART = 0;
            int indexSTRING = 0;
            int count=0;
            int l = result.length();
            String[] userDATA = new String[6];
            for (int i = 0; i < l; i++) {
                if (result.charAt(i) == '|') {
                    if(count==0){count++; indexSTART=i+1; continue;}
                    userDATA[indexSTRING] = result.substring(indexSTART, i);
                    indexSTART = i + 1;
                    indexSTRING++;
                   }
            }
            if(userDATA[0].equals("success")){
            Contactssqllitehandler CD = new Contactssqllitehandler(ctx);
            Cursor C = CD.getAllData();
            if(C.getCount()==1){
                CD.insertData("Doctor " + userDATA[1],"0"+userDATA[2]);
                //insert doctor details to its own database
                DoctorDetailsSQLliteHandler Dsql = new DoctorDetailsSQLliteHandler(ctx);
                Dsql.insertData("Doctor " +userDATA[1],"0"+userDATA[2]);

                ctx.getSharedPreferences("STATENUMBERS", MODE_PRIVATE).edit()
                        .putString("state2","123").apply();
                ctx.getSharedPreferences("STATENUMBERS", MODE_PRIVATE).edit()
                        .putString("state1",userDATA[2]).apply();
                ctx.getSharedPreferences("STATENUMBERS", MODE_PRIVATE).edit()
                        .putString("state3","123").apply();

                TextView doc_name  = ((Activity)ctx).findViewById(R.id.Userfragment_Doctorname);
                TextView doc_phone = ((Activity)ctx).findViewById(R.id.Userfragment_Doctorphone);
                Button call_doctor =  ((Activity)ctx).findViewById(R.id.call_doctor);
                doc_name.setText(userDATA[1]);
                doc_phone.setText("0"+userDATA[2]);
                call_doctor.setVisibility(View.VISIBLE);
            }
            }
            else
            {
                TextView doc_name = ((Activity)ctx).findViewById(R.id.Userfragment_Doctorname);
                TextView doc_phone = ((Activity)ctx).findViewById(R.id.Userfragment_Doctorphone);
                doc_name.setText("Confirm with  your doctor");
                doc_phone.setText("Confirm with your doctor");
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