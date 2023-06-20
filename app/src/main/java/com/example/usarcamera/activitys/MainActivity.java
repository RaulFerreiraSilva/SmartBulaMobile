package com.example.usarcamera.activitys;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;


import com.example.usarcamera.R;
import com.example.usarcamera.databinding.ActivityMainBinding;

import com.example.usarcamera.fragments.CameraFragment;
import com.example.usarcamera.fragments.FavoritoFragment;
import com.example.usarcamera.fragments.HistoricoFragment;
import com.example.usarcamera.fragments.HomeFragment;
import com.example.usarcamera.fragments.UserFragment;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //alternar fragments
        replaceFragment(new HomeFragment());
        binding.btnNav.setBackground(null);
        binding.btnNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.fragHome: replaceFragment(new HomeFragment());break;
                case R.id.fragFavorito: replaceFragment(new FavoritoFragment());break;
                case R.id.fragHistorico: replaceFragment(new HistoricoFragment());break;
                case R.id.fragCamera: replaceFragment(new CameraFragment());break;
                case R.id.fragUser: replaceFragment(new UserFragment());break;

            }
            return true;
        });
    }

    //método para realizar pesquisa por voz
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TESTE", ">>>>>>>>>>> PRIMEIRO PASSO");
        if (requestCode == 111){
            Log.d("TESTE", ">>>>>>>>>>>> SEGUNDO PASSO");
            if (resultCode == RESULT_OK){
                Log.d("TESTE", ">>>>>>>> TERCEIRO PASSO");
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                for (String r : result){
                    Log.d("TESTE", ">>>>>>>>>>>>>" + r);
                    SharedPreferences salvar = getSharedPreferences("usuario", Context.MODE_PRIVATE);
                    SharedPreferences.Editor gravar = salvar.edit();
                    gravar.putString("nomeRemedio", r);
                    gravar.commit();
                }
            }
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();

    }

}