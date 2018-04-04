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

import com.gp.eece2019.wecare.calls.Addcontacts;
import com.gp.eece2019.wecare.calls.ContactsList;
import com.gp.eece2019.wecare.calls.DatabaseHelper;
import com.gp.eece2019.wecare.login.SigninActivity;
import com.gp.eece2019.wecare.login.USERsqllitehandler;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    USERsqllitehandler usql;
    //DatabaseHelper bybass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usql =  new USERsqllitehandler(this);
        //bybass = new DatabaseHelper(this);
        boolean Firstusestatus= Checkfirstuse();

        if(Firstusestatus) {
            //bybass.insertData("Dummy","00");
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

                Cursor deleteid = usql.getid();
                StringBuffer buffer = new StringBuffer();
                while (deleteid.moveToNext()) {
                    buffer.append(deleteid.getString(0));
                }
                //int id = Integer.parseInt(buffer.toString());
                usql.deleteData(buffer.toString());
                //Toast.makeText(MainActivity.this,"Logging out",Toast.LENGTH_LONG).show();
                //Snackbar.make(view, "Logging out", Snackbar.LENGTH_LONG).setAction("Action", null).show();
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
            // Handle the camera action
        } else if (id == R.id.nav_measurements) {

        }
        else if (id == R.id.nav_notification) {

        } else if (id == R.id.nav_addcontacts) {

            Addcontacts Add = new Addcontacts();
            android.support.v4.app.FragmentManager Manager = getSupportFragmentManager();
            Manager.beginTransaction().replace(R.id.Fragment_container,Add).commit();

        } else if (id == R.id.nav_contacts) {

            ContactsList C = new ContactsList();
            android.support.v4.app.FragmentManager Manager = getSupportFragmentManager();
            Manager.beginTransaction().replace(R.id.Fragment_container,C).commit();


        } else if (id == R.id.nav_about) {


        } else if (id == R.id.nav_contactus) {

        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public boolean Checkfirstuse()
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

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }




}
