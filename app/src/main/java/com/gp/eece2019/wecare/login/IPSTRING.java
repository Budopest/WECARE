package com.gp.eece2019.wecare.login;

/**
 * Created by budopest on 20/04/18.
 */

public class IPSTRING {


    private final String Sip          = "http://192.168.1.121/";
    private final String login_url    = Sip+"logintest.php";
    private final String signup_url   = Sip+"signuptest.php";
    private final String medicine_url = Sip+"medicines.php";
    private final String measures_url = Sip+"readm.php";

    public String Getlogin()
    {
        return login_url;
    }
    public String Getsignup()
    {
        return signup_url;
    }
    public String Getmedicine()
    {
        return medicine_url;
    }
    public String Getmeasures()
    {
        return measures_url;
    }



}
