package com.example.usarcamera.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.usarcamera.R;
import com.example.usarcamera.databinding.FragmentUserBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    private EditText nome, sobreNome, email, dataNascimento;

    private EditText senhaAtual, senhaNova, confirmarSenha;

    private TextView alterarSenha;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(getLayoutInflater());

        View root = binding.getRoot();

        nome = root.findViewById(R.id.editNomeUser);
        sobreNome = root.findViewById(R.id.editSobreNomeUser);
        email = root.findViewById(R.id.editEmailUser);
        dataNascimento = root.findViewById(R.id.editDataNascimentoUser);
        senhaAtual = root.findViewById(R.id.editSenhaAtual);
        senhaNova = root.findViewById(R.id.editSenhaNova);
        confirmarSenha = root.findViewById(R.id.editConfirmarSenha);
        alterarSenha = root.findViewById(R.id.txtAcaoAlterarSenha);

        //iniciarComponentes(root);



        SharedPreferences ler = getActivity().getApplicationContext().getSharedPreferences(
                "usuario", Context.MODE_PRIVATE);

        String Nome = ler.getString("nome", "");
        String sbNome = ler.getString("SobreNome", "");
        String emailShared = ler.getString("email", "");
        String data = ler.getString("dataNasc", "");




        nome.setText(Nome);
        sobreNome.setText(sbNome);
        email.setText(emailShared);
        dataNascimento.setText(data);
        senhaAtual.setText(ler.getString("senha", ""));

        nome.setFocusable(false);
        sobreNome.setFocusable(false);
        email.setFocusable(false);
        dataNascimento.setFocusable(false);

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        alterarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verficarSenha(queue, ler);
            }
        });

        return root;
    }

    private void verficarSenha(RequestQueue queue, SharedPreferences ler) {

        if (senhaAtual.getText().toString().equals(ler.getString("senha", ""))){
            if (senhaNova.getText().toString().equals(confirmarSenha.getText().toString())){
                editarSenha(queue);
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Os campos de nova" +
                        " senha não coincidem, favor tente novamente!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Senha atual incorreta" +
                    ", favor tente novamente!", Toast.LENGTH_SHORT).show();
        }
    }

    private void iniciarComponentes(View root) {

    }

    private void editarSenha(RequestQueue queue) {

        int timeout = 20000;
        RetryPolicy policy = new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        SharedPreferences ler = getActivity().getApplicationContext().getSharedPreferences(
                "usuario", Context.MODE_PRIVATE);

        JSONObject dados = new JSONObject();
        try {
            dados.put("nome", ler.getString("nome", ""));
            dados.put("sobreNome", ler.getString("SobreNome", ""));
            dados.put("dataNasc", "2001-01-04");
            dados.put("email", ler.getString("email", ""));
            dados.put("senha", senhaNova.getText().toString());
        } catch (JSONException e){
            e.printStackTrace();
        }

        String endpoint = "http://10.0.2.2:5000/api/Usuario/Editar/?usuario="+dados;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, endpoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    SharedPreferences salvar = getActivity().getApplicationContext()
                            .getSharedPreferences("usuario", Context.MODE_PRIVATE);

                    SharedPreferences.Editor gravar = salvar.edit();
                    gravar.putString("senha", response.getString("senha"));

                    Log.d("SUCESSO", ">>>>>>>>>" + response);
                } catch (JSONException x){
                    Log.d("catch", ">>>>>>>>>>" + x);
                    x.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                Log.d("volleyError", ">>>>>>>>>>>>>" + error);
            }
        });
        queue.add(request);


    }
}