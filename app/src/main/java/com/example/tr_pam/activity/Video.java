package com.example.tr_pam.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.tr_pam.R;

public class Video extends AppCompatActivity {

    private VideoView vv;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        vv = (VideoView) findViewById(R.id.videoView);

        if (mediaController == null){
            new MediaController(this);
            mediaController.setAnchorView(vv);
        }
        vv.setMediaController(mediaController);
        Uri uri = Uri.parse("");
        vv.setVideoURI(uri);
        vv.start();
    }
}