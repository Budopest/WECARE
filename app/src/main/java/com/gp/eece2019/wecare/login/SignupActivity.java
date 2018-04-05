package com.gp.eece2019.wecare.login;


import com.gp.eece2019.wecare.MainActivity;
import com.gp.eece2019.wecare.R;
import com.gp.eece2019.wecare.calls.Contactssqllitehandler;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import android.content.Intent;

import java.util.Calendar;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    String user_type="P";
    EditText firstname,lastname,username,password,phone,datefield;
    String firstname_s,lastname_s,username_s,password_s,datefield_s,phone_s;

    private DatePickerDialog.OnDateSetListener birthdate_picker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            month++;
            setdatefield( year, month,  day);

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button signupbutton =  findViewById(R.id.signupbutton);
        Button datepicker   =  findViewById(R.id.datepicker);
        TextView signintext = findViewById(R.id.signintext);
        TextView skiptext   =  findViewById(R.id.skipregisterationtext);

        firstname   =   findViewById(R.id.firstname);
        lastname    =   findViewById(R.id.lastname);
        username    =   findViewById(R.id.username1);
        password    =   findViewById(R.id.password1);
        datefield   =   findViewById(R.id.date);
        phone       =   findViewById(R.id.phone);



        signupbutton.setOnClickListener(this);
        signintext.setOnClickListener(this);
        datepicker.setOnClickListener(this);
        skiptext.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        boolean allfields;
        switch (view.getId()) {

            case R.id.signupbutton:


                Middleground m = new Middleground(this);
                if(!m.checkinternetconnection()) break;

                firstname_s = firstname.getText().toString();
                phone_s     = phone.getText().toString();
                lastname_s  = lastname.getText().toString();
                username_s  = username.getText().toString();
                password_s  = password.getText().toString();
                datefield_s = datefield.getText().toString();

                allfields = checkcompletefields(firstname_s,lastname_s,username_s,password_s,phone_s,datefield_s);


                if(allfields){
                    String type = "signup";
                    Mysqlhandler signuphandler = new Mysqlhandler(this);
                    signuphandler.execute(type, firstname_s, lastname_s,username_s,password_s,datefield_s,phone_s,user_type);


                }

                break;

            case R.id.signintext:

                Intent i = new Intent(this,MainActivity.class);
                startActivity(i);
                break;

            case R.id.datepicker:

                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        SignupActivity.this,birthdate_picker,year,month,day);
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                break;

            case R.id.skipregisterationtext:

                firstname_s = firstname.getText().toString();
                phone_s     = phone.getText().toString();
                lastname_s  = lastname.getText().toString();
                username_s  = username.getText().toString();
                password_s  = password.getText().toString();
                datefield_s = datefield.getText().toString();
                allfields = checkcompletefields(firstname_s,lastname_s,username_s,password_s,phone_s,datefield_s);
                //if(allfields){
                USERsqllitehandler usql = new USERsqllitehandler(this);
                Contactssqllitehandler csql = new Contactssqllitehandler(this);
                //usql.insertData(firstname_s,lastname_s,username_s,phone_s,datefield_s);
                 boolean insert1=   usql.insertData("Abdelrahman","Mohamed","Test_user","01098930028","5/11/1995");
                 boolean insert2=   csql.insertData("Call an Ambulance (emergency only)","123");
                    if(insert1&&insert2){
                    Intent im = new Intent(this,MainActivity.class);
                    startActivity(im);
                    finish();
                    }
                //}

                break;


            default:
                break;
        }

    }
    public void Radioclick(View v)
    {

        boolean checked = ((RadioButton)v).isChecked();
        switch (v.getId()){
            case R.id.radioD:
                if(checked) user_type="D";
                break;
            case R.id.radioP:
                if(checked) user_type="P";
                break;
        }
    }
    //Check if entered date is in the future and set the text field
    public void setdatefield(int year,int month, int day)
    {
        boolean future = false;
        EditText datefield =   findViewById(R.id.date);
        final Calendar c = Calendar.getInstance();
        int yeartoday = c.get(Calendar.YEAR);
        int monthtoday = c.get(Calendar.MONTH) +1;
        int daytoday = c.get(Calendar.DAY_OF_MONTH);
        if(yeartoday<year){future=true;}
        else if (yeartoday==year){
            if(month>monthtoday) {future=true;}
            else if(month==monthtoday) {
                if(day>daytoday){future=true;}
            }
        }

        if(!future)
            datefield.setText(day+"/"+month+"/"+year);
        else {datefield.setHintTextColor(Color.RED);   datefield.setHint("invalid date"); }
    }
    public boolean checkcompletefields(String a,String b,String c,String d,String e,String f)
    {
        int emptyfields=0;
        if(a.equals("")){

            firstname.setHintTextColor(Color.RED);
            firstname.setHint("Enter First Name");
            emptyfields++;


        }
        if(b.equals("")){

            lastname.setHintTextColor(Color.RED);
            lastname.setHint("Enter last Name");
            emptyfields++;

        }
        if(c.equals("")){

            username.setHintTextColor(Color.RED);
            username.setHint("Enter user Name");
            emptyfields++;

        }
        if(d.equals("")){

            password.setHintTextColor(Color.RED);
            password.setHint("Enter password ");
            emptyfields++;

        }
        if(d.length()<4 || d.length()>12){

            password.setText("");
            password.setHintTextColor(Color.RED);
            password.setHint("length must be between 4 and 12 ");
            emptyfields++;

        }

        if(e.equals("")){

            phone.setHintTextColor(Color.RED);
            phone.setHint("Enter phone Number");
            emptyfields++;

        }
        if(f.equals("")){

            datefield.setHintTextColor(Color.RED);
            datefield.setHint("Enter your date");
            emptyfields++;

        }

        if(emptyfields!=0){return false;}
        else {return true;}
    }

}