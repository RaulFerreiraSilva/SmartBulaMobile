package com.example.usarcamera.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.usarcamera.R;

import org.json.JSONObject;

public class BulaActivity extends AppCompatActivity {

    private TextView bula;
    private ImageButton voltarTela;
    private ImageView favorito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bula);

        RequestQueue queue = Volley.newRequestQueue(this);

        iniciarComponentes();
        queroFavoritar(queue);
        voltar();

    }

    private void voltar() {
        voltarTela.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void queroFavoritar(RequestQueue queue) {
        favorito.setOnClickListener(view -> {
            try {
                if (favorito.getTag().toString().equals("NoFav")) {
                    favorito.setImageResource(R.drawable.ic_favoritado);
                    favorito.setTag("YesFav");
                    Log.d("FAVORITEI", ">>>>>>>>>>>>>>" + favorito.getTag().toString());
                    bulaFavoritada(queue);
                } else {
                    favorito.setImageResource(R.drawable.ic_senha_login);
                    favorito.setTag("NoFav");
                    Log.d("JAFAV", ">>>>>>>>" + favorito.getTag().toString());
                }
            }catch (Exception e){
                Log.d("ERRO", ">>>>>>>>>>" + e);
            }
        });

    }


    private void bulaFavoritada(RequestQueue queue) {
        SharedPreferences ler = getSharedPreferences("usuario", Context.MODE_PRIVATE);
        String endpoint = "http://10.0.2.2:5000/api/Usuario/Favoritar/?id_Usuario=" +
                ler.getString("id", "") + "&id_Medicamento=" +
                ler.getString("idMed", "");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                endpoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                bula.setText(ler.getString("resumoBula", ""));
                Log.d("RETORNO", ">>>>>>>>" + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERRO", ">>>>>>>>>" + error);
                Log.d("ERRO", ">>>>>>>>>" + error.getMessage());
                Log.d("ERRO", ">>>>>>>>>" + error.getCause());
            }
        });

        queue.add(request);
    }

    private void iniciarComponentes() {
        voltarTela = findViewById(R.id.btnVoltarBula);
        favorito = findViewById(R.id.btnFavoritarBula);
        bula = findViewById(R.id.txtBula);
    }



}