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

public class CadastroActivity extends AppCompatActivity {

    private EditText editNascimento, editNome, editSobrenome, editEmail, editSenha, editConfirmarSenha;
    private AppCompatButton btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        iniciarComponentes();
        //editNascimento.addTextChangedListener(new Mascara());

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarUsuario("https://10.0.2.2/api/usuario/salvar/");
            }
        });

    }

    private void cadastrarUsuario(final String endpoint){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        Pessoa pessoa = new Pessoa(editNome.getText().toString(), editSobrenome.getText().toString(),
                editSenha.getText().toString(), editEmail.getText().toString(),
                editNascimento.getText().toString());

        JSONObject dados = new JSONObject();
        try {
            dados.put("", pessoa);
        } catch (JSONException e){
            Log.d("TAG", "cadastrarUsuario " + e.getMessage());
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, endpoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("SUCESSO", ">>>>>>>>> " + response);

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERRO", ">>>>>>>>>>>>>> " + error.getMessage());
            }
        });

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