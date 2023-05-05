package com.example.usarcamera.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
        iniciarComponentes();
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

        String endpoint = "http://10.0.2.2:5000/api/Usuario/Logar/?userName="+emailLogin.getText().toString()+"&password="+senhaLogin.getText().toString();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, endpoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                      Pessoa pessoa = new Pessoa(response.getString("nome"),
                              response.getString("sobreNome"), response.getString("dataNasc"),
                              response.getString("email"), response.getString("senha"));

                      SharedPreferences.Editor gravar = getSharedPreferences("usuario", MODE_PRIVATE).edit();
                      gravar.putString("nome", pessoa.getNome());
                      gravar.putString("SobreNome", pessoa.getSobreNome());
                      gravar.putString("dataNasc", pessoa.getDataNasc());
                      gravar.putString("email", pessoa.getEmail());
                      gravar.putString("senha", pessoa.getSenha());

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } catch (JSONException e){
                    e.printStackTrace();
                }

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