package com.example.usarcamera.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.usarcamera.R;
import com.example.usarcamera.databinding.FragmentLoadingBinding;


public class LoadingFragment extends Fragment {

    private FragmentLoadingBinding binding;

    private ImageView loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoadingBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        loading = root.findViewById(R.id.img_loading);
        loading.setVisibility(View.INVISIBLE);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loading.setVisibility(View.VISIBLE);

            }
        }, 3000);

        return root;
    }
}