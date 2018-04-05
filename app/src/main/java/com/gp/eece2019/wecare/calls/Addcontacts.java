package com.gp.eece2019.wecare.calls;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.database.Cursor;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gp.eece2019.wecare.R;

public class Addcontacts extends Fragment implements View.OnClickListener {


    public Addcontacts() {
        // Required empty public constructor
    }


    Contactssqllitehandler myDb;
    EditText editName, editTel;
    Button Add;
    Button contacts;


    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        editName = (EditText) getView().findViewById(R.id.editText_name);
        editTel = (EditText) getView().findViewById(R.id.editText_Tel);
        Add = (Button) getView().findViewById(R.id.button_add);
        contacts = (Button) getView().findViewById(R.id.button_viewAll);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.
                fragment_addcontact, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        myDb = new Contactssqllitehandler(getActivity());
        Add.setOnClickListener(this);
        contacts.setOnClickListener(this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.button_add:

                boolean isInserted = myDb.insertData(editName.getText().toString(),
                        editTel.getText().toString());
                if (isInserted == true){
                    Toast.makeText(getActivity(), "Data Inserted", Toast.LENGTH_LONG).show();

                }

                else
                    Toast.makeText(getActivity(), "Data not Inserted", Toast.LENGTH_LONG).show();
                editName.setText("");
                editTel.setText("");

                break;

            case R.id.button_viewAll:

                Cursor res = myDb.getAllData();
                if (res.getCount() == 0) {
                    // show message
                    showMessage("Error", "Nothing found");
                    return;
                }
                ContactsList blankFragment= new ContactsList();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.Fragment_container, blankFragment)
                        .addToBackStack(null)
                        .commit();

                break;
        }

    }



    public void showMessage(String title,String Message){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

}
