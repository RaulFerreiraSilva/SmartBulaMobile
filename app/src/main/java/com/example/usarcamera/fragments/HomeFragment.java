package com.example.usarcamera.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.usarcamera.R;
import com.example.usarcamera.classes.Pessoa;
import com.example.usarcamera.databinding.FragmentHomeBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private String endpoint = "https://localhost:5001/api/usuario/salvar/";
    private TextView nome, sbNome, data, senha, email, sucess;

    private Button enviar;
    private FragmentHomeBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();


        nome = root.findViewById(R.id.txtNome);
        sbNome = root.findViewById(R.id.txtSbNome);
        data = root.findViewById(R.id.txtData);
        senha = root.findViewById(R.id.txtSenha);
        email = root.findViewById(R.id.txtEmail);
        sucess = root.findViewById(R.id.txtSucess);
        enviar = root.findViewById(R.id.btnEnviar);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(root.getContext());

                nome.setText("Guilherme");
                sbNome.setText("Scarlati");
                senha.setText("123456789a");
                email.setText("123@gmail.com");
                data.setText("2001-01-04");



                Pessoa pessoa = new Pessoa(nome.getText().toString(), sbNome.getText().toString(),
                        senha.getText().toString(), email.getText().toString(), data.getText().toString());
                JSONObject dados = new JSONObject();
                try {
                    dados.put("", pessoa);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, endpoint, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    nome.setText("");
                                    sbNome.setText("");
                                    data.setText("");
                                    senha.setText("");
                                    email.setText("");

                                    sucess.setText("SUCESSO!");
                                } catch (Exception e){
                                    Log.d("API2", ">>>>>>>>>>>>" + e.getMessage());
                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("API", ">>>>>>>>>" + error.getMessage());
                    }
                });

                queue.add(request);
            }
        });



        return root;
    }


}