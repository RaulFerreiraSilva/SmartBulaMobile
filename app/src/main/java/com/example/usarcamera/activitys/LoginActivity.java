package com.example.usarcamera.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.usarcamera.R;
import com.example.usarcamera.classes.Pessoa;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private TextView txtCadastrar;

    private EditText emailLogin, senhaLogin;
    private AppCompatButton btnLogar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        iniciarComponentes();
        RequestQueue queue = Volley.newRequestQueue(this);
        mudarTela();

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logarUsuario(queue);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void iniciarComponentes(){
        txtCadastrar = findViewById(R.id.txtCadastro);
        btnLogar = findViewById(R.id.btnLogar);
        senhaLogin = findViewById(R.id.editSenhaLogin);
        emailLogin = findViewById(R.id.editEmailLogin);
    }

    private void mudarTela(){
        txtCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CadastroActivity.class);
                startActivity(intent);
            }
        });
    }

   private void logarUsuario(RequestQueue queue){

        int timeout = 20000;
        RetryPolicy policy = new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        String endpoint = "http://10.0.2.2:5000/api/Usuario/Logar/?email="+emailLogin.getText().toString()+"&password="+senhaLogin.getText().toString();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, endpoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                      SharedPreferences salvar = getSharedPreferences("usuario", Context.MODE_PRIVATE);

                      SharedPreferences.Editor gravar = salvar.edit();
                      gravar.putString("nome", response.getString("nome"));
                      gravar.putString("SobreNome", response.getString("sobreNome"));
                      gravar.putString("dataNasc", response.getString("dataNasc"));
                      gravar.putString("email", response.getString("email"));
                      gravar.putString("senha", response.getString("senha"));
                      gravar.commit();

                    Log.d("NOME", ">>>>>>>>" + response.getString("nome"));

                } catch (JSONException e){
                    e.printStackTrace();
                }

                Handler tempo = new Handler();
                tempo.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }, 3000);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(LoginActivity.this, "Email ou senha incorretos, por favor tente novamente!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }
}