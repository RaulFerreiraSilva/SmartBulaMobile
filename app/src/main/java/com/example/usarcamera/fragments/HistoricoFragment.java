package com.example.usarcamera.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.usarcamera.R;
import com.example.usarcamera.classes.Remedio;
import com.example.usarcamera.databinding.FragmentHistoricoBinding;

import java.util.ArrayList;
import java.util.List;

public class HistoricoFragment extends Fragment {

    private FragmentHistoricoBinding binding;

    private ListView historico;
    private ImageButton voltar;

    private List<Remedio> lista = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHistoricoBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        SharedPreferences ler = getActivity().getApplicationContext().getSharedPreferences("usuario", Context.MODE_PRIVATE);

        iniciarComponentes(root);
        voltarTela();
        gerarLista(ler, root);
        return root;
    }

    private void gerarLista(SharedPreferences ler, View root) {
        Remedio remedio = new Remedio(0, ler.getString("bula", ""), ler.getString("resumoBula", ""), "dipirona");

        lista.add(remedio);

        ArrayAdapter<Remedio> adaptador = new ArrayAdapter<Remedio>(root.getContext(),
                android.R.layout.simple_list_item_1, lista);
        historico.setAdapter(adaptador);
    }

    private void voltarTela() {
        voltar.setOnClickListener(v ->{
            onDestroy();
        });
    }

    private void iniciarComponentes(View root) {
        voltar = root.findViewById(R.id.ic_before_historico);
        historico = root.findViewById(R.id.listViewHistorico);
    }
}