package com.example.tr_pam.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tr_pam.R;
import com.example.tr_pam.storage.DBHelperDaftar;

public class DaftarActivity extends AppCompatActivity {
    EditText edNama, edJenKel, edUmur;
    Button btDaftar;
    DBHelperDaftar dbHelperDaftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        edNama = (EditText) findViewById(R.id.editNama);
        edJenKel = (EditText) findViewById(R.id.editJenisKelamin);
        edUmur = (EditText) findViewById(R.id.editUmur);

        btDaftar = (Button) findViewById(R.id.buttonDaftar);

        dbHelperDaftar = new DBHelperDaftar(this);

        btDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = edNama.getText().toString();
                String jenkel = edJenKel.getText().toString();
                String umur = edUmur.getText().toString();
                dbHelperDaftar.addData(nama, jenkel, umur);
            }
        });
    }
}