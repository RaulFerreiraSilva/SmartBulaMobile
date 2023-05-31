package com.example.usarcamera.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.usarcamera.R;
import com.example.usarcamera.classes.Alergia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AlergiaActivity extends AppCompatActivity {

    private TextView tituloAlertDialogAlergia, mensagemAlertDialogAlergia;

    private AppCompatButton btnConfirmarAlergia, btnCancelarAlergia;

    private ListView alergiasListV;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alergia);

        RequestQueue queue = Volley.newRequestQueue(this);


        iniciarComponentes();
        mostrarRemedios(queue);

    }

    private void mostrarRemedios(RequestQueue queue) {
        List<String> lista = new ArrayList<>();

        String endpoint = "http://10.0.2.2:5000/api/Alergia/Listar";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, endpoint, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null && response.length() > 0){
                    for (int i = 0; i < response.length(); i++){
                        try {
                            String valor = response.getString(i);
                            lista.add(valor);
                            JSONObject resposta = response.getJSONObject(i);

                            SharedPreferences salvar = getSharedPreferences("usuario", Context.MODE_PRIVATE);
                            SharedPreferences.Editor gravar = salvar.edit();
                            gravar.putString("id_Alergia", resposta.getString("id_Alergia"));
                            gravar.putString("tipo_Alergia", resposta.getString("tipo_Alergia"));
                            gravar.commit();

                        } catch (JSONException exc){
                            exc.printStackTrace();
                        }
                    }
                    ArrayAdapter<String> adaptador = new ArrayAdapter<String>(
                            AlergiaActivity.this, //Contexto
                            android.R.layout.simple_list_item_1, //Layout padrão
                            lista); //Lista com os valores
                    alergiasListV.setAdapter(adaptador);
                }
                Log.d("RESPONSE", ">>>>>>>>>" + response);
                selecionarAlergia(queue, lista);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(request);
    }

    private void selecionarAlergia(RequestQueue queue, List<String> lista) {

        alergiasListV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String dados = lista.get(position);
                    if (dados != null){
                        confirmarAcao(dados, queue);
                    }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(AlergiaActivity.this, "Nenhum item selecionado!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmarAcao(String dados, RequestQueue queue) {

        SharedPreferences ler = getSharedPreferences("usuario", Context.MODE_PRIVATE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.dialog, null);
        tituloAlertDialogAlergia = layout.findViewById(R.id.tituloDialog);
        mensagemAlertDialogAlergia = layout.findViewById(R.id.mensagemDialog);
        btnConfirmarAlergia = layout.findViewById(R.id.btnConfirmar);
        btnCancelarAlergia = layout.findViewById(R.id.btnCancelar);

        builder.setView(layout);

        tituloAlertDialogAlergia.setText("ATENÇÃO!");
        mensagemAlertDialogAlergia.setText("Você está prestes a marcar que possui alergia a " +
               ler.getString("tipo_Alergia", "") + ", uma vez marcado, não poderá ser desfeito!");
        btnConfirmarAlergia.setText("Confirmar");
        btnCancelarAlergia.setText("Voltar");

        final AlertDialog dialog = builder.create();

        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                ViewGroup parent = (ViewGroup) layout.getParent();
                if (parent != null)parent.removeView(layout);
            }
        });


        btnConfirmarAlergia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                salvarRemedioAlergia(dados, queue);

                dialog.dismiss();

            }
        });

        btnCancelarAlergia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                ViewGroup parent = (ViewGroup) layout.getParent();
                if (parent != null) {
                    parent.removeView(layout);
                }
            }
        });
        if (dialog.getWindow() != null){dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(0));}



    }

    private void salvarRemedioAlergia(String nomeRemedio, RequestQueue queue) {

        SharedPreferences ler = getSharedPreferences("usuario", Context.MODE_PRIVATE);

        String endpoint = "http://10.0.2.2:5000/api/Alergia/AlergiaUsuario/?id_Usuario=" +
                ler.getString("id", "") + "&tipoAlergia=" + ler.getString("tipo_Alergia", "");

        StringRequest request = new StringRequest(Request.Method.POST, endpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", ">>>>>>>>>>>>>" + response);
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
        alergiasListV = findViewById(R.id.alergias);


    }
}