package com.gp.eece2019.wecare.login;
/**
 * Created by budopest on 15/03/18.
 */
    import android.app.Activity;
    import android.content.Context;
    import android.content.Intent;
    import android.database.Cursor;
    import android.os.AsyncTask;
    import android.widget.Toast;

    import com.gp.eece2019.wecare.MainActivity;
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


public class Mysqlhandler extends AsyncTask<String,Void,String> {

    Context context;
    URL_STRING Surl = new URL_STRING();
    int error=0;
    //AlertDialog alertDialog;
    Mysqlhandler (Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) { // check string params
        String type = params[0];
        String login_url = Surl.Getlogin();
        String signup_url = Surl.Getsignup();
        if(type.equals("login")) {
            try {
                String user_name = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
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
        }
        else if(type.equals("signup"))
        {
            try {
                String first_name = params[1];
                String last_name  = params[2];
                String user_name  = params[3];
                String password   = params[4];
                String date       = params[5];
                String phone      = params[6];
                String user_type  = params[7];
                URL url = new URL(signup_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data =
                        URLEncoder.encode("fname","UTF-8")+"="+URLEncoder.encode(first_name,"UTF-8")+"&"+
                                URLEncoder.encode("lname","UTF-8")+"="+URLEncoder.encode(last_name,"UTF-8")+"&"+
                                URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"+
                                URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"+
                                URLEncoder.encode("phone","UTF-8")+"="+URLEncoder.encode(phone,"UTF-8")+"&"+
                                URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(date,"UTF-8")+"&"+
                                URLEncoder.encode("type","UTF-8")+"="+URLEncoder.encode(user_type,"UTF-8");

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
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        //alertDialog = new AlertDialog.Builder(context).create();
        //alertDialog.setTitle("Connection Status");
    }

    @Override
    protected void onPostExecute(String result) {

        if(error==0){
        //alertDialog.setMessage(result);
        //alertDialog.show();
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
        UserSQLiteHandler usql = new UserSQLiteHandler(context);
        Contactssqllitehandler csql = new Contactssqllitehandler(context);
        if(userDATA[0].equalsIgnoreCase("success")) {
            {
                isInserted1 = usql.insertData(userDATA[1], userDATA[2], userDATA[3], '0'+userDATA[4], userDATA[5]);

                Cursor res = csql.getAllData();
                if(res.getCount()==0){
                isInserted2 = csql.insertData("Call an Ambulance (emergency only)","123");
                }

            }
            if (isInserted1&&isInserted2) {
                //Toast.makeText(context, "Data Inserted", Toast.LENGTH_LONG).show();
                Activity act = (Activity) context;
                Intent i = new Intent(context, MainActivity.class);
                act.startActivity(i);
                act.finish();
            } else
                Toast.makeText(context, "Data not Inserted(SQL LITE ERROR)", Toast.LENGTH_LONG).show();

        }
        else Toast.makeText(context, "Check your user name and password", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}