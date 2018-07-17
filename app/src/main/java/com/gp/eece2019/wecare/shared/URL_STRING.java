package com.gp.eece2019.wecare.shared;

/**
 * Created by budopest on 20/04/18.
 */

public class URL_STRING {


    private final String Sip             = "http://abdoqasem95.000webhostapp.com/";
    private final String login_url       = Sip+"logintest.php";
    private final String signup_url      = Sip+"signuptest.php";
    private final String medicine_url    = Sip+"medicines.php";
    private final String measures_url    = Sip+"measurements.php";
    private final String doctor_url      = Sip+"doctors.php";
    private final String sendMessage_url = Sip+"chat.php";
    private final String recMessage_url  = Sip+"respond.php";
    private final String pressure_url    = Sip+"pressure.php";
    private final String diabetes_url    = Sip+"diabetes.php";
    private final String feedback_url    = Sip+"feedback.php";
    private final String UPLOAD_URL      = Sip+"AndroidImageUpload/uploads/upload.php";



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
    public String getPressure_url() {
        return pressure_url;
    }
    public String getDiabetes_url() {
        return diabetes_url;
    }
    public String getFeedback_url() {
        return feedback_url;
    }
    public String getUPLOAD_URL() {
        return UPLOAD_URL;
    }
}
