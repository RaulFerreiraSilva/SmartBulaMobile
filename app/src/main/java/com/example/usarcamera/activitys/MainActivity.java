package com.example.usarcamera.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.usarcamera.R;
import com.example.usarcamera.databinding.ActivityMainBinding;
import com.example.usarcamera.fragments.CameraFragment;
import com.example.usarcamera.fragments.FavoritoFragment;
import com.example.usarcamera.fragments.HistoricoFragment;
import com.example.usarcamera.fragments.HomeFragment;
import com.example.usarcamera.fragments.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

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

    private void replaceFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();

    }

}