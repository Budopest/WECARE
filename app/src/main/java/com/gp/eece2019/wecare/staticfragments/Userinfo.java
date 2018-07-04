package com.gp.eece2019.wecare.staticfragments;



import android.annotation.SuppressLint;
import android.content.Intent;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.gp.eece2019.wecare.R;
import com.gp.eece2019.wecare.shared.InternetConnectionChecker;


import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class Userinfo extends Fragment {


    //UserSQLiteHandler usql;
    String Fname,Lname,Uname,phone,Bdate;
    InternetConnectionChecker internetConnectionChecker;
    TextView user_name,user_phone,user_birthdate,doctor_name,doctor_phone,cameratext;
    Button take_photo,call_doctor;
    ImageView user_image;
    DoctorDetailsMySqlHandler D;
    DoctorDetailsSQLliteHandler Dsql;
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
        cameratext     =  getView().findViewById(R.id.camerahint);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        Dsql = new DoctorDetailsSQLliteHandler(getActivity());
        internetConnectionChecker = new InternetConnectionChecker(getContext());
        final Cursor Ddata = Dsql.getAllData();
        if(Ddata.getCount()==0) {
            if(!internetConnectionChecker.checkinternetconnection()){
                D = new DoctorDetailsMySqlHandler(getActivity());
                D.execute(Uname);
            }
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

        String userimage = getContext().getSharedPreferences("Image", MODE_PRIVATE).getString("i","");

        if(!userimage.equals("")) ImageReloder(userimage);
        else {
            cameratext.setVisibility(View.VISIBLE);
            user_image.setImageResource(R.drawable.ic_person_black_24dp);
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
            cameratext.setVisibility(View.INVISIBLE);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

            getContext().getSharedPreferences("Image", MODE_PRIVATE).edit()
                    .putString("i",encodedImage).apply();

        }

    }
    private void ImageReloder(String userimage){
        try {
            byte [] encodeByte=Base64.decode(userimage,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            user_image.setImageBitmap(bitmap);
        } catch(Exception e) {
            e.getMessage();
        }
    }
}
