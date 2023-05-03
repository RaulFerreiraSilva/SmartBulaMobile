package com.example.usarcamera.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.usarcamera.R;
import com.example.usarcamera.classes.Pessoa;

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

        String endpoint = "http://10.0.2.2:5000/api/Usuario/Logar/?userName="+emailLogin.getText().toString() + "&password=" + senhaLogin.getText().toString();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, endpoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject email = response.getJSONObject("email");
                    JSONObject senha = response.getJSONObject("senha");
                } catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
}