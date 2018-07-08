package com.gp.eece2019.wecare;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.database.Cursor;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.gp.eece2019.wecare.calls.ContactsList;
import com.gp.eece2019.wecare.calls.Contactssqllitehandler;
import com.gp.eece2019.wecare.login.UserSQLiteHandler;
import com.gp.eece2019.wecare.measurements.Calc_BloodPressure;
import com.gp.eece2019.wecare.measurements.MeasureSQLiteHandler;
import com.gp.eece2019.wecare.measurements.Measurements;
import com.gp.eece2019.wecare.messanger.MessagesSqlLitehandler;
import com.gp.eece2019.wecare.staticfragments.About;
import com.gp.eece2019.wecare.staticfragments.Contactus;
import com.gp.eece2019.wecare.staticfragments.DoctorDetailsSQLliteHandler;
import com.gp.eece2019.wecare.messanger.Messanger;
import com.gp.eece2019.wecare.staticfragments.Settings;
import com.gp.eece2019.wecare.staticfragments.Userinfo;
import com.gp.eece2019.wecare.login.SigninActivity;
import com.gp.eece2019.wecare.notification.MedicinesList;
import com.gp.eece2019.wecare.welcomeslider.WelcomeScreen;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    UserSQLiteHandler usql;
    DoctorDetailsSQLliteHandler Dsql;
    MessagesSqlLitehandler smssql;
    MeasureSQLiteHandler msql;
    Contactssqllitehandler csql;

    String Fname, Lname, Uname, phone, Bdate;
    TextView firstfield, secondfield;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usql   = new UserSQLiteHandler(this);
        Dsql   = new DoctorDetailsSQLliteHandler(this);
        smssql = new MessagesSqlLitehandler(this);
        msql   = new MeasureSQLiteHandler(this);
        csql   = new Contactssqllitehandler(this);

        boolean Firstusestatus = Checkfirstuse();

        if (Firstusestatus) {

            Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .getBoolean("isFirstRun", true);
            if (isFirstRun) {
                //show start activity

                startActivity(new Intent(MainActivity.this, WelcomeScreen.class));
                finish();
            }

            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                    .putBoolean("isFirstRun", false).apply();

            setContentView(R.layout.activity_main);

        } else {
            Intent i = new Intent(this, SigninActivity.class);
            startActivity(i);
            finish();
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // The floating button action

                Intent Call_ambulance = new Intent(Intent.ACTION_CALL);
                Call_ambulance.setData(Uri.parse("tel:"+getSharedPreferences("STATENUMBERS", MODE_PRIVATE).getString("state3",null)));
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(Call_ambulance);

            }
        });

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        firstfield  =  headerView.findViewById(R.id.name_navdrawer);
        secondfield =  headerView.findViewById(R.id.email_navdrawer);
        imageView   =  headerView.findViewById(R.id.imageView);
        if(Getuserdetails()) {
            firstfield.setText(String.format("%s %s", Fname, Lname));
            secondfield.setText(String.format("%s  %s", Uname, phone));

            String userimage = getSharedPreferences("Image", MODE_PRIVATE).getString("i","");
            ImageReloder(userimage);

            Userinfo UI = new Userinfo(Fname,Lname,Uname,phone,Bdate);
            android.support.v4.app.FragmentManager Manager = getSupportFragmentManager();
            Manager.beginTransaction().replace(R.id.Fragment_container,UI).commit();
        }
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.Logout)
        {
            Logout();
        }

        return super.onOptionsItemSelected(item);
    }
    */

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Patientinfo) {

            Userinfo UI = new Userinfo(Fname,Lname,Uname,phone,Bdate);
            android.support.v4.app.FragmentManager Manager = getSupportFragmentManager();
            Manager.beginTransaction().replace(R.id.Fragment_container,UI).commit();
            // Handle the camera action
        } else if (id == R.id.nav_measurements) {

            Measurements M = new Measurements(Uname);
            android.support.v4.app.FragmentManager Manager = getSupportFragmentManager();
            Manager.beginTransaction().replace(R.id.Fragment_container,M).commit();

        }
        else if (id == R.id.nav_notification) {
            MedicinesList C = new MedicinesList(Uname);
            android.support.v4.app.FragmentManager Manager = getSupportFragmentManager();
            Manager.beginTransaction().replace(R.id.Fragment_container,C).commit();

        }
        else if (id == R.id.nav_contacts) {

            ContactsList C = new ContactsList();
            android.support.v4.app.FragmentManager Manager = getSupportFragmentManager();
            Manager.beginTransaction().replace(R.id.Fragment_container,C).commit();


        }else if (id == R.id.nav_messanger) {
            Messanger M = new Messanger(Uname);
            android.support.v4.app.FragmentManager Manager = getSupportFragmentManager();
            Manager.beginTransaction().replace(R.id.Fragment_container,M).commit();


        } else if (id == R.id.nav_about) {
            About a = new About();
            android.support.v4.app.FragmentManager Manager = getSupportFragmentManager();
            Manager.beginTransaction().replace(R.id.Fragment_container,a).commit();


        } else if (id == R.id.nav_contactus) {
            Contactus cu = new Contactus();
            android.support.v4.app.FragmentManager Manager = getSupportFragmentManager();
            Manager.beginTransaction().replace(R.id.Fragment_container,cu).commit();

        }
        else if (id == R.id.nav_measurements_calcualator) {
            Calc_BloodPressure cl = new Calc_BloodPressure(Uname);
            android.support.v4.app.FragmentManager Manager = getSupportFragmentManager();
            Manager.beginTransaction().replace(R.id.Fragment_container,cl).commit();

        }
        else if (id == R.id.nav_hospitals_location){

        }
        else if (id == R.id.nav_settings){
            Settings settings = new Settings();
            android.support.v4.app.FragmentManager Manager = getSupportFragmentManager();
            Manager.beginTransaction().replace(R.id.Fragment_container,settings).commit();
        }
        else if (id == R.id.nav_logout){ Logout(); }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public boolean Checkfirstuse()  // Return false at first use only
    {
        Cursor res = usql.getid();
        return res.getCount() != 0;
    }
    public boolean Getuserdetails()
    {
        Cursor res = usql.getAllData();
        if(res.getCount() == 0) {
            return false;
        }

        while (res.moveToNext()) {

            Fname = res.getString(1);
            Lname  = res.getString(2);
            Uname = res.getString(3);
            phone = res.getString(4);
            Bdate =res.getString(5);
        }

        return true;
    }
    public void Logout(){  // Clear all SQL LITE databases and direct the user to the login screen

        Cursor Doctor   = Dsql.getAllData();
        Cursor user     = usql.getid();
        Cursor contacts = csql.getAllData();
        Cursor sms      = smssql.getAllData();
        Cursor measure  = msql.getAllData();
        while (user.moveToNext()) {
            usql.deleteData(user.getString(0));
        }
        while (Doctor.moveToNext()){
            Dsql.deleteData(Doctor.getString(0));
        }

        while (contacts.moveToNext()){
            csql.deleteData(contacts.getString(0));
        }

        while (sms.moveToNext()){
            smssql.deleteData(sms.getString(0));
        }
        while (measure.moveToNext()){
            msql.deleteData(measure.getString(0));
        }

        Intent i = new Intent(MainActivity.this,SigninActivity.class);
        startActivity(i);
        finish();
    }
    private void ImageReloder(String userimage){
        try {
            byte [] encodeByte= Base64.decode(userimage,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            imageView.setImageBitmap(bitmap);
        } catch(Exception e) {
            e.getMessage();
        }
    }

}
