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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.usarcamera.R;

import org.json.JSONObject;

public class BulaActivity extends AppCompatActivity {

    private TextView bula, opcaoBula, opcaoResumo;
    private ImageButton voltarTela, aumentarFonte, diminuirFonte, leituraBula;
    private ImageView favorito;

    private SwitchCompat alternarTexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bula);

        SharedPreferences ler = getSharedPreferences("usuario", Context.MODE_PRIVATE);

        RequestQueue queue = Volley.newRequestQueue(this);

        iniciarComponentes();
        queroFavoritar(queue);
        aumentarDiminuirFonte();
        mudarEstadoSwitch(ler);



        voltar();

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
            bula.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanhoNovo);
        });

        diminuirFonte.setOnClickListener(v ->{
            float tamanhoNormal = bula.getTextSize();
            float tamanhoNovo = tamanhoNormal--;
            bula.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanhoNovo);
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
        String endpoint = "http://localhost:5000/api/Usuario/Favoritar/?id_Usuario=" +
                ler.getString("id", "") + "&id_Medicamento=" +
                ler.getString("idMed", "");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                endpoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("TAG", ">>>>>>>>" +response);

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