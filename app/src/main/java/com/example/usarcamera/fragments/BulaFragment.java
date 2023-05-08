package com.example.usarcamera.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.usarcamera.R;
import com.example.usarcamera.databinding.FragmentBulaBinding;

public class BulaFragment extends Fragment {

    private FragmentBulaBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBulaBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();


        return root;
    }

}