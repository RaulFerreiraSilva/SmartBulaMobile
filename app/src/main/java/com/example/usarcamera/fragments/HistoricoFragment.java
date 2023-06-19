package com.example.usarcamera.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.usarcamera.R;
import com.example.usarcamera.activitys.BulaActivity;
import com.example.usarcamera.classes.Remedio;
import com.example.usarcamera.databinding.FragmentHistoricoBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoricoFragment extends Fragment {

    private FragmentHistoricoBinding binding;

    private ListView historico;

    private List<Remedio> lista = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHistoricoBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        SharedPreferences ler = getActivity().getApplicationContext().getSharedPreferences("usuario", Context.MODE_PRIVATE);

        iniciarComponentes(root);
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
    private void verBulaHistorico(RequestQueue queue, List<Remedio> lista, SharedPreferences ler){
        historico.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Remedio remedin = lista.get(position);
                int idAlergia = remedin.getId();
                String principioAtivo = remedin.getPrincipioAtivo();

                if (remedin != null){
                    mostrarBulaHistorico(queue, ler, principioAtivo);
                }
            }
        });
    }

    private void mostrarBulaHistorico(RequestQueue queue, SharedPreferences ler, String principioAtivo) {
        String endpoint = "http://10.0.2.2:5000/api/Remedio?response=" + principioAtivo;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, endpoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null && response.length() > 0){
                    try {
                        Remedio remedio = new Remedio(
                                response.getInt("idMedicamento"),
                                response.getString("bula"),
                                response.getString("resumoBula"),
                                response.getString("principioAtivo"));

                        Intent intent = new Intent(getActivity().getApplicationContext(), BulaActivity.class);
                        startActivity(intent);
                    }catch (JSONException ex){

                        ex.printStackTrace();
                    }
                } else{
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Remedio não encontrado", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("ERRO", ">>>>>>>>>>>>>>" + error);
                Log.d("ERRO", ">>>>>>>>>>>>>>" + error.getMessage());
                Log.d("ERRO", ">>>>>>>>>>>>>>" + error.getCause());
            }
        });
        queue.add(request);
    }

    private void iniciarComponentes(View root) {
        historico = root.findViewById(R.id.listViewHistorico);
    }
}