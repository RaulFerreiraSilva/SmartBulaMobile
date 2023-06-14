package com.example.usarcamera.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BulaActivity extends AppCompatActivity {

    private TextView bula, opcaoBula, opcaoResumo;
    private ImageButton voltarTela, aumentarFonte, diminuirFonte;
    private ImageView favorito;

    private SwitchCompat alternarTexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bula);

        SharedPreferences ler = getSharedPreferences("usuario", Context.MODE_PRIVATE);

        RequestQueue queue = Volley.newRequestQueue(this);

        iniciarComponentes();
        colocarTexto(ler);
        verificarFavorito(queue, ler);
        queroFavoritar(queue);
        aumentarDiminuirFonte();
        mudarEstadoSwitch(ler);
        voltar();

    }

    private void verificarFavorito(RequestQueue queue, SharedPreferences ler) {
        String endpoint = "http://10.0.2.2:5000/api/Usuario/ListaFavoritar/" +
                "?id_Usuario="+ler.getString("id", "");

        String nomeRemedioAtual = ler.getString("principioAtivo", "");

        List<String> principiosAtivos = new ArrayList<>();
        Log.d("PRIMEIRO_PASSSO", ">>>>>>>>>>>" + "cheguei aqui");
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, endpoint, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null && response.length() > 0){
                    for (int i = 0; i < response.length(); i++){
                        Log.d("FOR_SEGUNDO_PASSO", ">>>>>>>>>>>" + "cheguei aqui");
                        try {
                            JSONObject retorno = response.getJSONObject(i);
                            principiosAtivos.add(retorno.getString("principioAtivo"));

                            for (String principios : principiosAtivos){
                                Log.d("FOREACH_TERCEIRO_PASSO", ">>>>>>>>>>>" + "cheguei aqui");

                                if (principios.equals(nomeRemedioAtual)){

                                    Log.d("IF_QUARTO_PASSO", ">>>>>>>>>>>" + "cheguei aqui");
                                    favorito.setImageResource(R.drawable.ic_favoritado);
                                    favorito.setTag("YesFav");
                                }
                            }
                        }catch (JSONException exc){

                            exc.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);
    }

    private void colocarTexto(SharedPreferences ler) {
        bula.setText(ler.getString("bula", ""));

        String txtPadraoFormatado = Html.fromHtml(bula.getText().toString()).toString();

        bula.setText(txtPadraoFormatado);
    }

    private void mudarEstadoSwitch(SharedPreferences ler) {
        alternarTexto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                float elevation = 0f;
                float realElevatiom = 100f;
                float densidade = getResources().getDisplayMetrics().density;
                float elevationUpper = realElevatiom * densidade;
                if (isChecked){
                    bula.setText(ler.getString("resumoBula", ""));

                    String txtFormatado = Html.fromHtml(bula.getText().toString()).toString();

                    bula.setText(txtFormatado);

                    opcaoBula.setElevation(elevation);
                    opcaoBula.setVisibility(View.INVISIBLE);
                    opcaoResumo.setElevation(elevationUpper);
                    opcaoResumo.setVisibility(View.VISIBLE);
                } else {
                    bula.setText(ler.getString("bula", ""));

                    String txtFormatado = Html.fromHtml(bula.getText().toString()).toString();

                    bula.setText(txtFormatado);

                    opcaoResumo.setElevation(elevation);
                    opcaoResumo.setVisibility(View.INVISIBLE);
                    opcaoBula.setElevation(elevationUpper);
                    opcaoBula.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void aumentarDiminuirFonte() {
        aumentarFonte.setOnClickListener(v ->{
            float tamanhoAtual = bula.getTextSize();
            float tamanhoNovo = tamanhoAtual + 2;
            bula.setTextSize(TypedValue.COMPLEX_UNIT_PX, tamanhoNovo);
        });

        diminuirFonte.setOnClickListener(v ->{
            float tamanhoAtual = bula.getTextSize();
            float tamanhoNovo = tamanhoAtual - 2;
            bula.setTextSize(TypedValue.COMPLEX_UNIT_PX, tamanhoNovo);
        });

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
                    favorito.setImageResource(R.drawable.ic_nofav);
                    favorito.setTag("NoFav");
                    Log.d("JAFAV", ">>>>>>>>" + favorito.getTag().toString());
                    bulaFavoritada(queue);
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

                Log.d("TAG", ">>>>>>>>" +response.toString());

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
        aumentarFonte = findViewById(R.id.btnAumentarFonte);
        diminuirFonte = findViewById(R.id.btnDiminuirFonte);
        alternarTexto = findViewById(R.id.swtichBula);
        opcaoBula = findViewById(R.id.txtOpcaoBula);
        opcaoResumo = findViewById(R.id.txtOpcaoResumo);
    }



}