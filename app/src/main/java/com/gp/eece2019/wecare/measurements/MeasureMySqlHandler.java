package com.gp.eece2019.wecare.measurements;

/**
 * Created by budopest on 08/04/18.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
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

import static android.content.Context.MODE_PRIVATE;


public class MeasureMySqlHandler extends AsyncTask<String,Void,String> {

    Context ctx;
    int error=0;

    //String type="";
    String user_name;

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
        //alertDialog.setTitle("Connection Status");
        //String result = "|12/12/1995,(100,2,3,4),(10,20,30,40)";
        //if(type.equalsIgnoreCase("withDisplay"))
        //Displayexistingmeasures();

    }



    @Override
    protected void onPostExecute(String result) {

        if(error==0){
            //alertDialog.setMessage(result);
            //alertDialog.show();
            int indexdatestart=0;   int indexelementstart=0; boolean firsttime=true;
            boolean end = false;    boolean firstelement =false; boolean inside = false;
            int WT=0,WTF=0,WP=0,WPF=0;
            String date="";
            String element="";
            MeasureSQLiteHandler M = new MeasureSQLiteHandler(ctx);
            LastRECdata   LD = new LastRECdata(ctx);
            boolean isinserted = false;

            for(int i =0;i<result.length();i++)
            {
                if(result.charAt(i)=='|' && !end)          {indexdatestart=i+1; end=true;}
                if(result.charAt(i)==',' && !firstelement) {
                    date = result.substring(indexdatestart,i);
                    firstelement=true;

                    Cursor res =  LD.getAllData();

                    if(res.getCount() == 0) { //the data base is empty

                        if(LD.insertData(date)){ /*Toast.makeText(ctx,"First Data Received Correctly",Toast.LENGTH_LONG).show();*/}
                    }
                    else{
                        String OLDid="",OLDdate="";
                        while (res.moveToNext()) { OLDid   = res.getString(0);  OLDdate = res.getString(1); }
                        if(OLDdate.equals(date)){break;}
                        if (LD.updateData(OLDid,date)) { /*Toast.makeText(ctx,"New data Received",Toast.LENGTH_LONG).show();*/ }
                    }
                }

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

                    try {
                        T = Integer.parseInt(all[0]);
                        TF = Integer.parseInt(all[1]);
                        P = Integer.parseInt(all[2]);
                        PF = Integer.parseInt(all[3]);
                        if(TF>WTF){WTF=TF; WT=T;}
                        if(PF>WPF){WPF=PF; WP=P;}
                        //TakeNeededActions(T,TF,P,PF); //Take actions in emergency cases

                        isinserted = M.insertData(T,TF,P,PF);
                        if (isinserted && firsttime){
                            //Toast.makeText(ctx, "Data Inserted", Toast.LENGTH_LONG).show();
                            firsttime = false;
                        }
                    }
                    catch (NumberFormatException e){}

                }
            }



            if(WTF==3 || WPF==3){
                Automaticcall(3);
            }
            else if(WTF==4 || WPF==4){
                Automaticcall(4);
            }
            else if(WTF==5 || WPF==5){
                Automaticcall(5);
            }

        }
        //if(type.equalsIgnoreCase("withDisplay")){

        MeasureMySqlHandler measureMySqlHandler = new MeasureMySqlHandler(ctx);
        try {
            measureMySqlHandler.execute(user_name);
        }
        catch (Exception e){}


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