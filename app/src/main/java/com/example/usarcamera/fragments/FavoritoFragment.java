package com.example.usarcamera.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.usarcamera.R;
import com.example.usarcamera.databinding.FragmentFavoritoBinding;


public class FavoritoFragment extends Fragment {

    private FragmentFavoritoBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoritoBinding.inflate(getLayoutInflater());

        View root = binding.getRoot();

        return root;
    }
}