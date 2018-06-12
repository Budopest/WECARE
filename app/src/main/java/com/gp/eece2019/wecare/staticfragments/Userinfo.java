package com.gp.eece2019.wecare.staticfragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gp.eece2019.wecare.MainActivity;
import com.gp.eece2019.wecare.R;
import com.gp.eece2019.wecare.login.USERsqllitehandler;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class Userinfo extends Fragment {


    //USERsqllitehandler usql;
    String Fname,Lname,Uname,phone,Bdate;
    TextView user_name,user_phone,user_birthdate,doctor_name,doctor_phone;
    Button take_photo,call_doctor;
    ImageView user_image;
    Doctordetails D;
    DOCTORsqllite Dsql;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public Userinfo(String f,String l,String u,String p,String d) {
        Fname = f;
        Lname = l;
        Uname = u;
        phone = p;
        Bdate = d;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_userinfo, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        user_name      =  getView().findViewById(R.id.Userfragment_name);
        user_birthdate =  getView().findViewById(R.id.Userfragment_birthdate);
        user_phone     =  getView().findViewById(R.id.Userfragment_phone);
        doctor_name    =  getView().findViewById(R.id.Userfragment_Doctorname);
        doctor_phone   =  getView().findViewById(R.id.Userfragment_Doctorphone);
        take_photo     =  getView().findViewById(R.id.user_take_photo);
        user_image     =  getView().findViewById(R.id.user_image);
        call_doctor    =  getView().findViewById(R.id.call_doctor);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        Dsql = new DOCTORsqllite(getActivity());
        final Cursor Ddata = Dsql.getAllData();
        if(Ddata.getCount()==0) {
            D = new Doctordetails(getActivity());
            D.execute(Uname);
        }

        user_name.setText(String.format(" %s %s", Fname, Lname));
        user_birthdate.setText(String.format(" %s", Bdate));
        user_phone.setText(String.format(" %s", phone));
        if(Ddata.moveToNext())
        {
        doctor_name.setText(Ddata.getString(1)); // 0 is the ID
        doctor_phone.setText(Ddata.getString(2));
        call_doctor.setVisibility(View.VISIBLE);
        }

        take_photo.setOnClickListener(new View.OnClickListener(){
              @Override
              public void onClick(View v) {

                  dispatchTakePictureIntent();
              }});
        call_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Call_Doctor = new Intent(Intent.ACTION_CALL);
                Call_Doctor.setData(Uri.parse("tel:"+Ddata.getString(2)));
                if (Call_Doctor.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(Call_Doctor);
                }
            }
        });


        super.onActivityCreated(savedInstanceState);
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            user_image.setImageBitmap(imageBitmap);
        }

    }
}
