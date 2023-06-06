package com.example.usarcamera.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.usarcamera.R;
import com.example.usarcamera.activitys.AlergiaActivity;
import com.example.usarcamera.activitys.BulaActivity;
import com.example.usarcamera.classes.Alergia;
import com.example.usarcamera.classes.Remedio;
import com.example.usarcamera.databinding.FragmentFavoritoBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FavoritoFragment extends Fragment {

    private FragmentFavoritoBinding binding;

    private ListView favoritos;

    private TextView bula;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoritoBinding.inflate(getLayoutInflater());

        View root = binding.getRoot();

        View layout = inflater.inflate(R.layout.activity_bula, container, false);

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        SharedPreferences ler = getActivity().getApplicationContext().getSharedPreferences(
                "usuario", Context.MODE_PRIVATE);

        iniciarComponentes(root, layout);

        listarFavoritos(queue, root, ler);

        return root;
    }

    private void listarFavoritos(RequestQueue queue, View root, SharedPreferences ler) {
        String endpoint = "http://localhost:5000/api/Usuario/ListaFavoritar/" +
                "?id_Usuario="+ler.getString("id", "");

        List<Remedio> lista = new ArrayList<>();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                endpoint, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null && response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject retorno = response.getJSONObject(i);
                            Remedio remedin = new Remedio(retorno.getInt("idMedicamento"),
                                    retorno.getString("bula"), retorno.getString(
                                    "resumoBula"), retorno.getString(
                                    "principioAtivo"));
                            lista.add(remedin);


                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }

                    }

                }
                ArrayAdapter<Remedio> adaptador = new ArrayAdapter<Remedio>(
                        root.getContext(), //Contexto
                        android.R.layout.simple_list_item_1, //Layout padrão
                        lista); //Lista com os valores
                favoritos.setAdapter(adaptador);
                //verificarFavorito(adaptador, favoritos);
                verFavorito(queue, lista, ler);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(request);
    }

    /*private void verificarFavorito(ArrayAdapter<Remedio> adaptador, ListView favoritos) {

        if (icFavorito.getTag().toString().equals("NoFav")){
            adaptador.clear();
            adaptador.notifyDataSetChanged();
        }
    }*/

    private void verFavorito(RequestQueue queue, List<Remedio> lista, SharedPreferences ler) {
        favoritos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Remedio remedin = lista.get(position);
                int idAlergia = remedin.getId();
                String principioAtivo = remedin.getPrincipioAtivo();

                if (remedin != null){
                    mostrarBulaFavorita(queue, ler, principioAtivo);
                    Log.d("ALERGIA", ">>>>>>>>>>" + "if");

                }
            }
        });
    }

    private void mostrarBulaFavorita(RequestQueue queue, SharedPreferences ler, String principioAtivo) {
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

                                bula.setText(ler.getString("bula", ""));
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


    private void iniciarComponentes(View root, View layout){
        favoritos = root.findViewById(R.id.listViewFavorito);
        bula = layout.findViewById(R.id.txtBula);
    }
}