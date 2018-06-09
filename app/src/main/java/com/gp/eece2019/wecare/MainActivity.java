package com.gp.eece2019.wecare;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.database.Cursor;
import android.content.Intent;
import android.widget.TextView;

import com.gp.eece2019.wecare.calls.Addcontacts;
import com.gp.eece2019.wecare.calls.ContactsList;
import com.gp.eece2019.wecare.measurements.Measurements;
import com.gp.eece2019.wecare.staticfragments.About;
import com.gp.eece2019.wecare.staticfragments.Contactus;
import com.gp.eece2019.wecare.staticfragments.DOCTORsqllite;
import com.gp.eece2019.wecare.staticfragments.Userinfo;
import com.gp.eece2019.wecare.login.SigninActivity;
import com.gp.eece2019.wecare.login.USERsqllitehandler;
import com.gp.eece2019.wecare.notification.MedicinesList;
import com.gp.eece2019.wecare.welcomeslider.WelcomeScreen;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    USERsqllitehandler usql;
    DOCTORsqllite Dsql;
    String Fname,Lname,Uname,phone,Bdate;
    TextView firstfield,secondfield;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usql =  new USERsqllitehandler(this);
        Dsql =  new DOCTORsqllite(this);

        boolean Firstusestatus= Checkfirstuse();

        if(Firstusestatus) {

            Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .getBoolean("isFirstRun", true);
            if (isFirstRun) {
                //show start activity

                startActivity(new Intent(MainActivity.this,WelcomeScreen.class));
                finish();
            }

            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                    .putBoolean("isFirstRun", false).commit();

            setContentView(R.layout.activity_main);

        }
        else {
            Intent i = new Intent(this,SigninActivity.class);
            startActivity(i);
            finish();
        }


        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor Doctor   = Dsql.getAllData();
                Cursor deleteid = usql.getid();
                while (deleteid.moveToNext()) {
                    usql.deleteData(deleteid.getString(0));
                }
                while (Doctor.moveToNext()){
                    Dsql.deleteData(Doctor.getString(0));
                }
                Intent i = new Intent(MainActivity.this,SigninActivity.class);
                startActivity(i);
                finish();


            }
        });

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        firstfield =  (TextView)headerView.findViewById(R.id.name_navdrawer);
        secondfield =  (TextView)headerView.findViewById(R.id.email_navdrawer);
        if(Getuserdetails()) {
            firstfield.setText(Fname+" "+Lname);
            secondfield.setText(Uname+ "  " + phone);
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

        return super.onOptionsItemSelected(item);
    }

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

        } else if (id == R.id.nav_addcontacts) {

            Addcontacts Add = new Addcontacts();
            android.support.v4.app.FragmentManager Manager = getSupportFragmentManager();
            Manager.beginTransaction().replace(R.id.Fragment_container,Add).commit();

        } else if (id == R.id.nav_contacts) {

            ContactsList C = new ContactsList();
            android.support.v4.app.FragmentManager Manager = getSupportFragmentManager();
            Manager.beginTransaction().replace(R.id.Fragment_container,C).commit();


        } else if (id == R.id.nav_about) {
            About a = new About();
            android.support.v4.app.FragmentManager Manager = getSupportFragmentManager();
            Manager.beginTransaction().replace(R.id.Fragment_container,a).commit();


        } else if (id == R.id.nav_contactus) {
            Contactus cu = new Contactus();
            android.support.v4.app.FragmentManager Manager = getSupportFragmentManager();
            Manager.beginTransaction().replace(R.id.Fragment_container,cu).commit();

        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public boolean Checkfirstuse()  // Return false at first use only
    {
        Cursor res = usql.getid();
        if(res.getCount() == 0) {
            // show message
            //showMessage("Error","Nothing found");
            return false;
        }
        /*
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("Id :"+ res.getString(0)+"\n");

        }

        //Show all data
        //showMessage("Data",buffer.toString());
        */
        return true;
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

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }




}
