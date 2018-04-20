package com.gp.eece2019.wecare.login;

/**
 * Created by budopest on 20/04/18.
 */

public class IPSTRING {


    private final String login_url = "http://192.168.1.73/logintest.php";
    private final String signup_url = "http://192.168.1.73/signuptest.php";
    private final String medicine_url = "http://192.168.1.121/medicines.php";
    private final String measures_url = "http://192.168.1.121/readm.php";

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
