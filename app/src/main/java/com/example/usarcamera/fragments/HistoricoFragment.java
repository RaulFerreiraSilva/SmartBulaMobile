package com.example.usarcamera.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.usarcamera.databinding.FragmentHistoricoBinding;

public class HistoricoFragment extends Fragment {

    private FragmentHistoricoBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHistoricoBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();



        return root;
    }
}