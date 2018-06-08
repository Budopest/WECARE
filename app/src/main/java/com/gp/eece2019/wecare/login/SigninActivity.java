package com.gp.eece2019.wecare.login;


import com.gp.eece2019.wecare.R;
import com.gp.eece2019.wecare.welcomeslider.WelcomeScreen;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        Button loginbutton  =  findViewById(R.id.loginbutton);
        TextView signuptext =  findViewById(R.id.signuptext);

        //EditText username =   findViewById(R.id.username);
        //EditText password =   findViewById(R.id.password);

        loginbutton.setOnClickListener(this);
        signuptext.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        boolean allfields=true;

        switch (view.getId()) {

            case R.id.loginbutton:


                Middleground m = new Middleground(this);
                if(!m.checkinternetconnection()) break;

                EditText username =   findViewById(R.id.username);
                EditText password =   findViewById(R.id.password);
                String username_s = username.getText().toString();
                String password_s = password.getText().toString();

                if(username_s.equals(""))
                {
                    username.setHintTextColor(Color.RED);
                    username.setHint("Enter user Name");
                    allfields = false;
                }

                if(password_s.equals(""))
                {
                    password.setHintTextColor(Color.RED);
                    password.setHint("Enter Password");
                    allfields = false;
                }
                if(allfields)
                {
                    String type = "login";

                    Mysqlhandler loginhandler = new Mysqlhandler(this);
                    loginhandler.execute(type, username_s, password_s);

                }


                break;

            case R.id.signuptext:

                Intent i = new Intent(this,SignupActivity.class);
                startActivity(i);
                finish();
                break;


            default:
                break;
        }

    }
}
