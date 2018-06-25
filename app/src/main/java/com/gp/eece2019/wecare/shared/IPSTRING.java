package com.gp.eece2019.wecare.shared;

/**
 * Created by budopest on 20/04/18.
 */

public class IPSTRING {


    private final String Sip             = "http://192.168.1.21/";
    private final String login_url       = Sip+"logintest.php";
    private final String signup_url      = Sip+"signuptest.php";
    private final String medicine_url    = Sip+"medicines.php";
    private final String measures_url    = Sip+"readm.php";
    private final String doctor_url      = Sip+"doctors.php";
    private final String sendMessage_url = Sip+"chat.php";
    private final String recMessage_url  = Sip+"respond.php";



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
    public String getDoctor_url() {
        return doctor_url;
    }
    public String getSendMessage_url() {
        return sendMessage_url;
    }
    public String getRecMessage_url() {
        return recMessage_url;
    }
}
