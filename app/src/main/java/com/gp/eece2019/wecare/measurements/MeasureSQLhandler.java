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
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;
import android.widget.Toast;

import com.gp.eece2019.wecare.R;
import com.gp.eece2019.wecare.shared.IPSTRING;
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


public class MeasureSQLhandler extends AsyncTask<String,Void,String> {

    Context ctx;
    int error=0;
    int idnumber = R.id.T11; int idnumber2 = R.id.T101; int ns =0;

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
        //alertDialog.setTitle("Connection Status");
        //String result = "|12/12/1995,(100,2,3,4),(10,20,30,40)";
        Displayexistingmeasures();

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
            MeasureSQLLITE M = new MeasureSQLLITE(ctx);
            LastRECdata   LD = new LastRECdata(ctx);
            boolean isinserted = false;

            for(int i =0;i<result.length();i++)
            {
                if(result.charAt(i)=='|' && !end)          {indexdatestart=i+1; end=true;}
                if(result.charAt(i)==',' && !firstelement) {
                    date = result.substring(indexdatestart,i);
                    firstelement=true;

                    Cursor res =  LD.getAllData();
                    TextView timeupdate = (TextView)((Activity)ctx).findViewById(R.id.updateddate);
                    timeupdate.setText(String.format("Updated On \" %s \"", date));
                    if(res.getCount() == 0) { //the data base is empty

                        if(LD.insertData(date)){ Toast.makeText(ctx,"First Data Received Correctly",Toast.LENGTH_LONG).show();}
                    }
                    else{
                        String OLDid="",OLDdate="";
                        while (res.moveToNext()) { OLDid   = res.getString(0);  OLDdate = res.getString(1); }
                        if(OLDdate.equals(date)){break;}
                        if (LD.updateData(OLDid,date)) { Toast.makeText(ctx,"New data Received",Toast.LENGTH_LONG).show(); }
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
                            Toast.makeText(ctx, "Data Inserted", Toast.LENGTH_LONG).show();
                            firsttime = false;
                        }
                    }
                    catch (NumberFormatException e){}

                }
            }
            Displayexistingmeasures();
            if(WTF==3 || WPF==3){
                Automaticcall(3);
            }
            else if(WTF==4 || WPF==4){
                Automaticcall(4);
            }
            else if(WTF==5 || WPF==5){
                Automaticcall(5);
            }



            /*
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
            */

        }

    }

    private void Displayexistingmeasures() {

        MeasureSQLLITE EM = new MeasureSQLLITE(ctx);
        LastRECdata   ELD = new LastRECdata(ctx);
        Cursor date =  ELD.getAllData();
        TextView timeupdate = (TextView)((Activity)ctx).findViewById(R.id.updateddate);
        if(date.getCount()!=0){
            while(date.moveToNext()){timeupdate.setText("Updated On \" " + date.getString(1) + " \"");}
        }

        //timeupdate.setText("Updated On \" " + date + " \"");
        Cursor res = EM.getAllData();
        //int c = 0 ;
        if(res.getCount() != 0) {
            while (res.moveToNext()) {
                //res.getString(0);
                //c++;
                Fillthetable(res.getString(1),res.getString(2),
                        res.getString(3),res.getString(4));
            }

        }

    }

    private void Fillthetable(String t, String tf, String p, String pf) {

        if(ns<9){
        TextView f1 = (TextView)((Activity)ctx).findViewById(idnumber++);
        TextView f2 = (TextView)((Activity)ctx).findViewById(idnumber++);
        TextView f3 = (TextView)((Activity)ctx).findViewById(idnumber++);
        TextView f4 = (TextView)((Activity)ctx).findViewById(idnumber++);
        f1.setText(t);
        f2.setText(tf);
        f3.setText(p);
        f4.setText(pf);
        ns++;
        }
        else
            {
                TextView f1 = (TextView)((Activity)ctx).findViewById(idnumber2++);
                TextView f2 = (TextView)((Activity)ctx).findViewById(idnumber2++);
                TextView f3 = (TextView)((Activity)ctx).findViewById(idnumber2++);
                TextView f4 = (TextView)((Activity)ctx).findViewById(idnumber2++);
                f1.setText(t);
                f2.setText(tf);
                f3.setText(p);
                f4.setText(pf);
                ns=0; idnumber = R.id.T11; idnumber2 =R.id.T101;
            }


    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    private void Automaticcall(int f) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        String ph = getDOCTORphone();

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
    private String getDOCTORphone(){
        DoctorDetailsSQLliteHandler Dsql = new DoctorDetailsSQLliteHandler(ctx);
        Cursor res = Dsql.getAllData();
        while (res.moveToNext()){return res.getString(2);}
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}