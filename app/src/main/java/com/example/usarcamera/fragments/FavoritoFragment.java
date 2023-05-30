package com.example.usarcamera.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.usarcamera.R;
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

    private TextView nome, principioAtivo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoritoBinding.inflate(getLayoutInflater());

        View root = binding.getRoot();

        View layoutLista = inflater.inflate(R.layout.list_item_favoritos, container, false);

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        SharedPreferences ler = getActivity().getApplicationContext().getSharedPreferences(
                "usuario", Context.MODE_PRIVATE);

        iniciarComponentes(root, layoutLista);

        listarFavoritos(queue, ler);

        return root;
    }

    private void listarFavoritos(RequestQueue queue, SharedPreferences ler) {
        String endpoint = "http://10.0.2.2:5000/api/Usuario/ListaFavoritar/" +
                "?id_Usuario="+ler.getString("id", "");

        List<Remedio> lista = new ArrayList<>();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST,
                endpoint, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0;i< response.length();i++){
                    try {
                        JSONObject retorno = response.getJSONObject(i);
                        Remedio remedin = new Remedio(retorno.getInt("idMedicamento"),
                                retorno.getString("bula"), retorno.getString(
                                "resumoBula"), retorno.getString(
                                "principioAtivo"));
                        lista.add(remedin);


                    } catch (JSONException ex){
                        ex.printStackTrace();
                    }

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(request);
    }


    private void iniciarComponentes(View root, View lista){
        favoritos = root.findViewById(R.id.listViewFavorito);
        nome = lista.findViewById(R.id.txtNomeRemedio);
        principioAtivo = lista.findViewById(R.id.txtPrincipioAtivo);
    }
}