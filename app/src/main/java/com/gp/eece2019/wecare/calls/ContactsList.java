package com.gp.eece2019.wecare.calls;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.gp.eece2019.wecare.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsList extends Fragment {
    Contactssqllitehandler myDb;

    public ContactsList() {
        // Required empty public constructor


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View View = inflater.inflate(R.layout.fragment_contact, container, false);
        return View;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        myDb = new Contactssqllitehandler(getActivity());
        Cursor res = myDb.getAllData();
        if(res.getCount() != 0){
            int j = 0;
            while (res.moveToNext()) {
                j++;
            }
            res.moveToFirst();
            final String[] A = new String[j];
            A[0] = res.getString(1);
            final String[] B = new String[j];
            B[0] = res.getString(2);
            int l = 1;
            while (res.moveToNext()) {
                A[l] = res.getString(1);
                B[l] = res.getString(2);
                l++;
            }



            ListView listView = (ListView) getView().findViewById(R.id.mobile_list);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_list_item_1, A);
            listView.setAdapter(adapter);


            registerForContextMenu(listView);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:0" + B[position]));
                    if (ActivityCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    getActivity().startActivity(callIntent);
                }
            });}
        else {
            showMessage("Error", "Nothing found");
        }
        super.onActivityCreated(savedInstanceState);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select Action");
        menu.add(0, v.getId(), 0, "Call");//groupId, itemId, order, title
        menu.add(0, v.getId(), 1, "Edit");
        menu.add(0, v.getId(), 2, "Delete");

    }

    public boolean onContextItemSelected(MenuItem item) {
        Cursor res = myDb.getAllData();
        int j = 0;
        while (res.moveToNext()) {
            j++;
        }
        res.moveToFirst();
        String[] A = new String[j];
        A[0] = res.getString(1);
        String[] B = new String[j];
        B[0] = res.getString(2);
        String[] c = new String[j];
        c[0] = res.getString(0);

        int l = 1;
        while (res.moveToNext()) {
            A[l] = res.getString(1);
            B[l] = res.getString(2);
            c[l] = res.getString(0);
            l++;
        }

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;

        if (item.getTitle() == "Call") {

            try {
                if (Build.VERSION.SDK_INT > 22) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling

                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 101);

                        return false;
                    }

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:0" + B[index]));
                    getActivity().startActivity(callIntent);

                } else {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:0" + B[index]));
                    startActivity(callIntent);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else if (item.getTitle() == "Edit") {
            Edit Edit= new Edit();
            Bundle bundle = new Bundle();
            bundle.putInt("index", index);// Put anything what you want
            Edit.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.Fragment_container, Edit)
                    .addToBackStack(null)
                    .commit();
        } else if (item.getTitle() == "Delete") {
            Integer deletedRows = myDb.deleteData(c[index]);
            if (deletedRows > 0)
                Toast.makeText(getActivity(), "Data Deleted", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getActivity(), "Data not Deleted", Toast.LENGTH_LONG).show();
            update();
        } else {
            return false;
        }
        return true;
    }
    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
    public void update() {

        Cursor res = myDb.getAllData();
        if(res.getCount() == 0) {
            // show message
            showMessage("Error","Nothing found");
            ContactsList BlankFragment= new ContactsList();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.Fragment_container, BlankFragment)
                    .addToBackStack(null)
                    .commit();
            return;
        }

        int j=0;
        while (res.moveToNext()) {

            j++;
        }
        res.moveToFirst();
        String[] A = new String[j];
        String[] B = new String[j];
        String[] c = new String[j];
        int l=0;
        while(res.moveToNext()){
            A[l]= res.getString(1);
            B[l]= res.getString(2);
            c[l]= res.getString(0);
            l++;
        }
        ContactsList BlankFragment= new ContactsList();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.Fragment_container, BlankFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.addcontacts,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.addcontact:
                Addcontacts Add = new Addcontacts();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.Fragment_container, Add)
                        .addToBackStack(null)
                        .commit();


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}




