package com.example.tr_pam.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.tr_pam.R;
import com.example.tr_pam.storage.DBHelperNotif;

import java.util.ArrayList;

public class NotifList extends AppCompatActivity {
    private static final String TAG = "ListDataActivity";

    DBHelperNotif dbHelperNotif;

    private ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif_list);
        mListView = (ListView) findViewById(R.id.listView);
        dbHelperNotif = new DBHelperNotif(this);

        populateListView();
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");

        //get the data and append to a list
        Cursor data = dbHelperNotif.getData();
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){
            //get the value from the database in column 1
            //then add it to the ArrayList
            listData.add(data.getString(1));
            listData.add(data.getString(2));
        }
        //create the list adapter and set the adapter
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.two_line_list_item, listData);
        mListView.setAdapter(adapter);

        //set an onItemClickListener to the ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final CharSequence[] items = {"View", "Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(NotifList.this);
                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i==0){

                            Cursor c = dbHelperNotif.getData();
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            Cursor dataview = dbHelperNotif.getItemID(arrID.get(position));

                            ArrayList<String> listData = new ArrayList<>();
                            while(dataview.moveToNext()){

                                listData.add(dataview.getString(1));
                                listData.add(dataview.getString(2));
                            }

                            Log.d(TAG, listData.toString());
                        }

                        if (i==1){
                            //delete
                            Cursor c = dbHelperNotif.getData();
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }

                            dbHelperNotif.deleteName(arrID.get(position));
                            finish();
                            startActivity(getIntent());
                        }

                    }
                });
                dialog.show();

            }
        });
    }
}