package com.example.usarcamera.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.usarcamera.R;
import com.example.usarcamera.classes.Alergia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListaAlergiaUsuario extends AppCompatActivity {

    private ImageButton btnVoltarAlergiaUser;

    private ListView alergiaUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alergia);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        SharedPreferences ler = getSharedPreferences("usuario", Context.MODE_PRIVATE);

        iniciarComponentes();
        mostrarAlergias(queue, ler);
    }

    private void mostrarAlergias(RequestQueue queue, SharedPreferences ler) {
        List<Alergia> lista = new ArrayList<>();

        String endpoint = "http://localhost:5000/api/Alergia/ListarAlergiaUsuario/?usuarioId" +
                ler.getString("id", "");

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, endpoint, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response != null && response.length() > 0){
                            for (int i = 0; i < response.length(); i++){
                                try {
                                    JSONObject resposta = response.getJSONObject(i);

                                    Alergia alergia = new Alergia(resposta.getInt("id_Alergia"),
                                            resposta.getString("tipo_Alergia"));

                                    lista.add(alergia);

                                } catch (JSONException exc){
                                    exc.printStackTrace();
                                }
                            }
                            ArrayAdapter<Alergia> adaptador = new ArrayAdapter<Alergia>(
                                    ListaAlergiaUsuario.this, //Contexto
                                    android.R.layout.simple_list_item_1, //Layout padrão
                                    lista); //Lista com os valores
                            alergiaUser.setAdapter(adaptador);
                        }
                        Log.d("RESPONSE", ">>>>>>>>>" + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }

    private void iniciarComponentes() {
        btnVoltarAlergiaUser = findViewById(R.id.btnVoltarAlergiaUser);
        alergiaUser = findViewById(R.id.alergiasUser);
    }
}