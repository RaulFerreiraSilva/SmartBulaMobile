package com.example.usarcamera.activitys;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.usarcamera.R;
import com.example.usarcamera.classes.Pessoa;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

public class CadastroActivity extends AppCompatActivity {

    private EditText editNascimento, editNome, editSobrenome, editEmail, editSenha, editConfirmarSenha;
    private AppCompatButton btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        iniciarComponentes();
        RequestQueue queue = Volley.newRequestQueue(this);


        clique(queue);

    }

    private void verificaCamposVazios(RequestQueue queue){
        if (editNascimento.getText().toString().isEmpty() && editNome.getText().toString().isEmpty()
                && editSobrenome.getText().toString().isEmpty() && editEmail.getText().toString().isEmpty()
                && editSenha.getText().toString().isEmpty() && editConfirmarSenha.getText().toString().isEmpty()){
            Toast.makeText(this, "Campos Não Preenchidos, favor Preenche-los", Toast.LENGTH_SHORT).show();
        } else {
            verficaSenhaInverteOrdem(queue);
        }
    }

    private void clique(RequestQueue queue) {
        btnCadastrar.setOnClickListener(v -> verificaCamposVazios(queue));
    }


    private void verficaSenhaInverteOrdem(RequestQueue queue) {

        String dataPadrao = editNascimento.getText().toString();

        SimpleDateFormat formatoEntrada = new SimpleDateFormat("ddmmyyyy");
        SimpleDateFormat formatoSaida = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date data = formatoEntrada.parse(dataPadrao);
            String dataFormatada = formatoSaida.format(data);

            Log.d("DATA", ">>>>>>>>>>>>>>" + dataFormatada);

            if (editSenha.getText().toString().equals(editConfirmarSenha.getText().toString())) {
                cadastrarUsuario(queue, dataFormatada);
            } else {
                editSenha.setText("");
                editConfirmarSenha.setText("");
                Toast.makeText(getApplicationContext(), "As senhas digitadas não são iguais, por favor, tente novamente!", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException exc){
            exc.printStackTrace();
        }
    }

    private void cadastrarUsuario(RequestQueue queue, String dataFormata) {

        Pessoa pessoa = new Pessoa(editNome.getText().toString(), editSobrenome.getText().toString(),
                dataFormata, editEmail.getText().toString(),
                editSenha.getText().toString());

        JSONObject usuario = new JSONObject();
        try {
            usuario.put("Nome", pessoa.getNome());
            usuario.put("SobreNome", pessoa.getSobreNome());
            usuario.put("DataNasc", pessoa.getDataNasc());
            usuario.put("Email", pessoa.getEmail());
            usuario.put("Senha", pessoa.getSenha());
            Log.d("RESULTADO", ">>>>>>>>>>" + usuario);
        } catch (JSONException e) {
            Log.d("TAG", "cadastrarUsuario " + e.getMessage());
        }

        //mudando tempo de espera de requisição do Volley
        int timeout = 20000;
        RetryPolicy policy = new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        String endpoint = "http://localhost:5000/api/Usuario/Salvar/?usuario=" + usuario;
        Log.d("USUARIO", ">>>>>>>>>>>>>>" + usuario);

        StringRequest request = new StringRequest(Request.Method.POST, endpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null) && response.length() > 0){
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    limpaCampos();
                } else {
                    Toast.makeText(CadastroActivity.this, "Usuario não cadastrado, favor tentar novamente!", Toast.LENGTH_SHORT).show();
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

    private void limpaCampos() {
        editNome.setText("");
        editSobrenome.setText("");
        editEmail.setText("");
        editNascimento.setText("");
        editSenha.setText("");
        editConfirmarSenha.setText("");
    }

    private void iniciarComponentes() {
        editNascimento = findViewById(R.id.editDataCadastro);
        editNome = findViewById(R.id.editNomeCadastro);
        editSobrenome = findViewById(R.id.editSobrenomeCadastro);
        editEmail = findViewById(R.id.editEmailCadastro);
        editSenha = findViewById(R.id.editSenhaCadastro);
        editConfirmarSenha = findViewById(R.id.editConfirmarSenhaCadastro);
        btnCadastrar = findViewById(R.id.btnCadastrar);
    }
}