package com.gp.eece2019.wecare.measurements;

public class Measurementitem {

    private  String temperature;

    private String heartrate;

    private String t_condition;

    private  String hr_condition;

    private String ID;

    public Measurementitem(String t, String t_c, String hr, String hr_c, String id)
    {
        temperature = t;
        heartrate   =hr;
        t_condition = t_c;
        hr_condition = hr_c;
        ID=id;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getHeartrate() {
        return heartrate;
    }

    public String getT_condition() {
        return t_condition;
    }

    public String getHr_condition() {
        return hr_condition;
    }

    public String getID() {
        return ID;
    }
}
