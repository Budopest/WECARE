package com.gp.eece2019.wecare.notification;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gp.eece2019.wecare.R;


public class MedicinesList extends Fragment {


    public MedicinesList() {
        // Required empty public constructor
    }
    Medicinesqllitehandler Db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View View = inflater.inflate(R.layout.fragment_medicines, container, false);
        return View;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Db = new Medicinesqllitehandler(getActivity());
        Cursor res = Db.getAllData();
        if(res.getCount() != 0){
            int j = 0;
            while (res.moveToNext()) {
                j++;
            }
            res.moveToFirst();
            final String[] A = new String[j];
            A[0] = res.getString(0);
            int l = 1;
            while (res.moveToNext()) {
                A[l] = res.getString(0);
                l++;
            }

            ListView listView = (ListView) getView().findViewById(R.id.medicine_list);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_list_item_1, A);
            listView.setAdapter(adapter);
            registerForContextMenu(listView);
        }
        else {
            showMessage("Error", "Nothing found");
        }
        super.onActivityCreated(savedInstanceState);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select Action");
        menu.add(0, v.getId(), 0, "Set Notification");//groupId, itemId, order, title
    }

    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle() == "Set Notification") {

            try {
                SetNotification SetNotification = new SetNotification();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.Fragment_container,SetNotification)
                        .addToBackStack(null)
                        .commit();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        else {

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

}




