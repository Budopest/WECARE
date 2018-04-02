package com.gp.eece2019.wecare.LOGIN;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

/**
 * Created by budopest on 02/04/18.
 */

public class Middleground {

    Context ctx;
    Middleground(Context ctx)
    {
        this.ctx=ctx;
    }


    public boolean checkinternetconnection()
    {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni==null) {
            AlertDialog alertDialog =  new AlertDialog.Builder(ctx).create();
            alertDialog.setTitle("Connection Status");
            alertDialog.setMessage("Make sure you are connected to the internet to continue");
            alertDialog.show();
            return false;
        }
        else return true;
    }
}
