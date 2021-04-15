package com.example.tr_pam.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.tr_pam.Peserta;
import com.example.tr_pam.R;
import com.example.tr_pam.storage.DBHelperDaftar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PesertaList extends AppCompatActivity {
    private static final String TAG = "PesertaList";

    DBHelperDaftar dbHelperDaftar;
    ListView lvPeserta;

    private List<Peserta> words = new ArrayList<>();
    private ArrayList<HashMap<String, String>>
            list = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peserta_list);

        lvPeserta = (ListView) findViewById(R.id.listViewPeserta);

        dbHelperDaftar = new DBHelperDaftar(this);

        populateListView();
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");

        //get the data and append to a list
        Cursor data = dbHelperDaftar.getData();
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){
            //get the value from the database in column 1
            //then add it to the ArrayList
            listData.add(data.getString(1));
            listData.add(data.getString(2));
            listData.add(data.getString(3));
        }
        //create the list adapter and set the adapter
        //ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        //lvPeserta.setAdapter(adapter);

        loadKata();

        //set an onItemClickListener to the ListView
        lvPeserta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final CharSequence[] items = {"View", "Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(PesertaList.this);
                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i==0){

                            Cursor c = dbHelperDaftar.getData();
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            Cursor dataview = dbHelperDaftar.getItemID(arrID.get(position));

                            ArrayList<String> listData = new ArrayList<>();
                            while(dataview.moveToNext()){

                                listData.add(dataview.getString(1));
                                listData.add(dataview.getString(2));
                                listData.add(dataview.getString(3));
                            }

                            Log.d(TAG, listData.toString());
                        }

                        if (i==1){
                            //delete
                            Cursor c = dbHelperDaftar.getData();
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }

                            dbHelperDaftar.deleteName(arrID.get(position));
                            finish();
                            startActivity(getIntent());
                        }

                    }
                });
                dialog.show();

            }
        });
    }

    private void loadKata(){
        words.clear();
        words = dbHelperDaftar.getAllData();

        String[] ArrayNama = new String[words.size()];
        String[] ArrayJenKel = new String[words.size()];
        String[] ArrayUmur = new String[words.size()];

        for (int x = 0; x < words.size(); x++) {
            ArrayNama[x] = words.get(x).getNama();
            ArrayJenKel[x] = words.get(x).getJenisKelamin();
            ArrayUmur[x] = words.get(x).getUmur();

        }

        for (int i=words.size()-1;i>=0;i--){
            HashMap<String, String> contact = new HashMap<>();
            contact.put("nama",ArrayNama[i]);
            contact.put("jenkel",ArrayJenKel[i]);
            contact.put("umur",ArrayUmur[i]);

            list.add(contact);
        }

        ListAdapter listadapter = new SimpleAdapter(
                PesertaList.this, list,R.layout.list_peserta,
                new String[]{"nama","jenkel","umur"},
                new int[]{R.id.lsNama,R.id.lsJenisKelamin,R.id.lsUmur});
        lvPeserta.setAdapter(listadapter);
    }
}