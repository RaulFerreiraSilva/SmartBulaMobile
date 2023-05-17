package com.example.usarcamera.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.usarcamera.R;

public class LoadingActivity extends AppCompatActivity {

    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        iniciarComponentes();

        Handler handler = new Handler();


        Glide.with(this).load(R.drawable.loading).into(img);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                direcionarLogin();

            }
        }, 3000);
    }

    private void iniciarComponentes(){
        img = findViewById(R.id.img_loading);
    }

    private void direcionarLogin(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}