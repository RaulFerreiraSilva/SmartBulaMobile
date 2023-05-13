package com.example.usarcamera.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.usarcamera.R;

public class BulaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bula);

        SharedPreferences ler = getSharedPreferences("usuario", Context.MODE_PRIVATE);
        Log.d("BULABANCO", ">>>>>>>>>>>>" + ler.getString("bula", ""));
    }
}