package com.gp.eece2019.wecare.calls;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gp.eece2019.wecare.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Edit extends Fragment  {

    Contactssqllitehandler myDb;
    public Edit() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        myDb = new Contactssqllitehandler(getActivity());
        Cursor res = myDb.getAllData();
        int j = 0;
        while (res.moveToNext()) {
            j++;
        }
        res.moveToFirst();
        final   String[] A = new String[j];
        A[0] = res.getString(1);
        final   String[] B = new String[j];
        B[0] = res.getString(2);
        final String[] c = new String[j];
        c[0] = res.getString(0);

        int l = 1;
        while (res.moveToNext()) {
            A[l] = res.getString(1);
            B[l] = res.getString(2);
            c[l] = res.getString(0);
            l++;
        }
        final EditText editname = (EditText) getView().findViewById(R.id.edit_name);
        final EditText editnumber = (EditText) getView().findViewById(R.id.edit_number);
        Button Edit = (Button) getView().findViewById(R.id.edit_button);
        Bundle bundle = this.getArguments();
        //final String [] N  = intent.getStringArrayExtra("Numbers");
        final int index = bundle.getInt("index");

        editname.setText(A[index]);
        editnumber.setText("0"+B[index]);

        Edit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //final String[] m = l.getStringArrayExtra("ID_array");

                        // int index = l.getIntExtra("index", 0);
                        Integer deletedRows = myDb.deleteData(c[index]);

                        boolean isInserted = myDb.insertData(editname.getText().toString(),
                                editnumber.getText().toString());

                        if (isInserted == true)
                            Toast.makeText(getActivity(), "Data Editted", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getActivity(), "Data not Editted", Toast.LENGTH_LONG).show();

                    }

                }
        );

        super.onActivityCreated(savedInstanceState);
    }


}
