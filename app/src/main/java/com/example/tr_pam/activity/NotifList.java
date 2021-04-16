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
import android.widget.SimpleAdapter;

import com.example.tr_pam.Notifikasi;
import com.example.tr_pam.Peserta;
import com.example.tr_pam.R;
import com.example.tr_pam.storage.DBHelperNotif;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotifList extends AppCompatActivity {
    private static final String TAG = "NotifList";

    DBHelperNotif dbHelperNotif;

    private ListView mListView;

    private List<Notifikasi> notifs = new ArrayList<>();
    private ArrayList<HashMap<String, String>>
            list = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        //ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        //mListView.setAdapter(adapter);

        loadNotif();

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

    private void loadNotif(){
        notifs.clear();
        notifs = dbHelperNotif.getAllData();

        String[] ArrayTitle = new String[notifs.size()];
        String[] ArrayBody = new String[notifs.size()];

        for (int x = 0; x < notifs.size(); x++) {
            ArrayTitle[x] = notifs.get(x).getTitle();
            ArrayBody[x] = notifs.get(x).getBody();

        }

        for (int i=notifs.size()-1;i>=0;i--){
            HashMap<String, String> contact = new HashMap<>();
            contact.put("title",ArrayTitle[i]);
            contact.put("body",ArrayBody[i]);

            list.add(contact);
        }

        ListAdapter listadapter = new SimpleAdapter(
                NotifList.this, list,R.layout.list_notif,
                new String[]{"title","body"},
                new int[]{R.id.lsTitle,R.id.lsBody});
        mListView.setAdapter(listadapter);
    }
}