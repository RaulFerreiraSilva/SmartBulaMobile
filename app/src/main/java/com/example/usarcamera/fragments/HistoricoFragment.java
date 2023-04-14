package com.example.usarcamera.fragments;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.usarcamera.R;
import com.example.usarcamera.classes.Historico;
import com.example.usarcamera.databinding.FragmentHistoricoBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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