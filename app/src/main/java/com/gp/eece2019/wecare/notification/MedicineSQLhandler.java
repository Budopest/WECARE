package com.gp.eece2019.wecare.notification;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;
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


public class MedicineSQLhandler extends AsyncTask<String,Void,String> {

    Context ctx;
    int error=0;
    URL_STRING Surl = new URL_STRING();
    AlertDialog alertDialog;
    String updatemessage="";
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
        alertDialog.setTitle("New updates on your medicines list");//the recieved response from the data base
        //Medicinesqllitehandler MSQL = new Medicinesqllitehandler(ctx);
        //MSQL.insertData("med1",3);
    }

    @Override
    protected void onPostExecute(String result) {

        //alertDialog.setMessage(result);
        //alertDialog.show();

        if(error==0&&!result.equals("nothing")) {

            int note=0;

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
                    else {
                        try{
                            dose[c2]=Integer.parseInt(result.substring(indexstart,i)); }
                        catch (Exception e){return;}
                        c2++; indexstart=i+1; halfc++;
                    }
                }
                if(i==result.length()-1) {
                    try{
                        dose[c2]=Integer.parseInt(result.substring(indexstart,i));}
                    catch (Exception e){return;}
                }

            }
            Cursor res1 = MSQL.getAllData();
            if(res1.getCount() == 0) {
                for(int i=0;i<num;i++)
                {
                    MSQL.insertData(name[i],dose[i]);

                    updatemessage=updatemessage+"medicine :"+name[i]+"\n"+"Number of doses :"+dose[i]+"\n";
                    note++;

                    update();
                }
                if(note>0){
                    alertDialog.setMessage(updatemessage);
                    alertDialog.show();
                    updatemessage="";}
            }
            else{
                int c = 0; int cc=0;
                for(int i=0;i<num;i++)
                {
                    //found = false;
                    Cursor res = MSQL.getAllData();
                    while (res.moveToNext()) {

                        String s1 = res.getString(0); //ID
                        String s2= res.getString(1); //name
                        String s3= res.getString(2); //dose

                        if((s2.equals(name[i]))&&(s3.equals(Integer.toString(dose[i])))) {c++; break;}
                        else if((s2.equals(name[i]))&&!(s3.equals(Integer.toString(dose[i])))){
                            updatemessage=updatemessage+"medicine :"+name[i]+"\n"+"Number of doses :"+dose[i]+"\n";
                            note++;
                            MSQL.updateData(s1,name[i],dose[i]); c++; break;
                        }
                    }
                    if(c==0) {
                        MSQL.insertData(name[i],dose[i]);
                        updatemessage=updatemessage+"medicine :"+name[i]+"\n"+"Number of doses :"+dose[i]+"\n";
                        note++;
                        update();
                    }
                    else c=0;
                }
                if(note>0){
                    alertDialog.setMessage(updatemessage);
                    alertDialog.show();
                    updatemessage="";}

                Cursor res = MSQL.getAllData();
                String s1;
                while (res.moveToNext()) {
                    cc=0;
                    s1 = res.getString(0); //ID
                    String s2 = res.getString(1); //name
                    String s3 = res.getString(2); //dose
                    for(int i=0;i<num;i++){
                        if(s2.equals(name[i]))  cc++;
                    }
                    if(cc==0) {MSQL.deleteData(s1);  update();}
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
    public void update(){

        Medicinesqllitehandler Db = new Medicinesqllitehandler(ctx);
        Cursor res = Db.getAllData();
        if(res.getCount() != 0){
            int j = 0;
            while (res.moveToNext()) {
                j++;
            }
            res.moveToFirst();
            final String[] A = new String[j];
            A[0] = res.getString(1);
            int l = 1;
            while (res.moveToNext()) {
                A[l] = res.getString(1);
                l++;
            }

            ListView listView = (ListView) ((Activity)ctx).findViewById(R.id.medicine_list);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    ctx, android.R.layout.simple_list_item_1, A);
            listView.setAdapter(adapter);

        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}