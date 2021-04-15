package com.example.tr_pam.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.tr_pam.R;
import com.example.tr_pam.firebase.Config;
import com.example.tr_pam.firebase.NotificationUtils;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tr_pam.storage.DBHelperNotif;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtRegID, txtMessage;
    DBHelperNotif dbHelperNotif;
    Button btNotif, btDaftar, btPeserta, btMusik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtMessage = (TextView) findViewById(R.id.textNotif);
        txtRegID = (TextView) findViewById(R.id.textRegID);

        btNotif = (Button) findViewById(R.id.buttonHistoryNotif);
        btDaftar = (Button) findViewById(R.id.buttonGoDaftar);
        btPeserta = (Button) findViewById(R.id.buttonPeserta);
        btMusik = (Button) findViewById(R.id.buttonMusik);

        dbHelperNotif = new DBHelperNotif(this);
        String message, message1;

        if(getIntent().getExtras()!=null){
            txtMessage.setText("");
            message = getIntent().getExtras().getString("title");
            message1 = getIntent().getExtras().getString("body");
            if(message==null && message1 == null)
            {
                message = "No new messages";
            }
            AddData(message1, message);
            txtMessage.setText(message);
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");
                    String message1 = intent.getStringExtra("title");
                    AddData(message1,message);

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    txtMessage.setText(message);
                    Log.e(TAG, "Firebase reg id: " + message);
                }
            }
        };

        btNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotifList.class);
                startActivity(intent);
            }
        });

        btDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DaftarActivity.class);
                startActivity(intent);
            }
        });

        btPeserta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PesertaList.class);
                startActivity(intent);
            }
        });

        btMusik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Musik.class);
                startActivity(intent);
            }
        });

        displayFirebaseRegId();
    }

    public void AddData(String newEntry, String newEntry1){
        boolean insertData = dbHelperNotif.addData(newEntry, newEntry1);

        if(insertData){

        }else{

        }

    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
            txtRegID.setText("Firebase Reg Id: " + regId);
        else
            txtRegID.setText("Firebase Reg Id is not received yet!");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
}