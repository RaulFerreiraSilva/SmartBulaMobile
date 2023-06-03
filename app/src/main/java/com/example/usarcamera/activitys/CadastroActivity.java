package com.example.usarcamera.activitys;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

public class CadastroActivity extends AppCompatActivity {

    private EditText editNascimento, editNome, editSobrenome, editEmail, editSenha, editConfirmarSenha;
    private AppCompatButton btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        iniciarComponentes();
        RequestQueue queue = Volley.newRequestQueue(this);
        //editNascimento.addTextChangedListener(new Mascara());

        clique(queue);

    }

    private void clique(RequestQueue queue) {
        btnCadastrar.setOnClickListener(View -> confirmarSenha(queue));
    }

    private void confirmarSenha(RequestQueue queue) {

        if (editSenha.getText().toString().equals(editConfirmarSenha.getText().toString())) {
            cadastrarUsuario(queue);
        } else {
            editSenha.setText("");
            editConfirmarSenha.setText("");
            Toast.makeText(this, "As senhas digitadas não são iguais, por favor, tente novamente!", Toast.LENGTH_SHORT).show();
        }
    }

    private void cadastrarUsuario(RequestQueue queue) {

        Pessoa pessoa = new Pessoa(editNome.getText().toString(), editSobrenome.getText().toString(),
                editNascimento.getText().toString(), editEmail.getText().toString(),
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

        int timeout = 20000;
        RetryPolicy policy = new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        String endpoint = "http://10.0.2.2:5000/api/Usuario/Salvar/?usuario=" + usuario;
        Log.d("USUARIO", ">>>>>>>>>>>>>>" + usuario);

        StringRequest request = new StringRequest(Request.Method.POST, endpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null) && response.length() > 0){
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
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
        /*JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, endpoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERRO", ">>>>>>>>>>>>>> " + error);
            }
        });*/

        queue.add(request);
    }

    /*private class Mascara implements TextWatcher{

        private boolean formatando;


        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (formatando){
                formatando = false;
                return;
            }

            String dtNascimento = s.toString().replaceAll("[^\\d]", "");

            if (dtNascimento.length() == 8) {
                dtNascimento = dtNascimento.substring(0, 2) + "-" + dtNascimento.substring(2, 4) + "-" +
                        dtNascimento.substring(4, 8) + "-";
            }

            formatando = true;
            editNascimento.setText(dtNascimento);
            editNascimento.setSelection(editNascimento.getText().length());
        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }*/

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