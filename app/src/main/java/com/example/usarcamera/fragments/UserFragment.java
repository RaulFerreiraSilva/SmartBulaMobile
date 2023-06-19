package com.example.usarcamera.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
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
import com.example.usarcamera.activitys.ListaAlergiaUsuario;
import com.example.usarcamera.databinding.FragmentUserBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    private EditText nome, sobreNome, email, dataNascimento;

    private EditText senhaAtual, senhaNova, confirmarSenha;

    private TextView alterarSenha;

    private AppCompatButton mostrarAlergia;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(getLayoutInflater());

        View root = binding.getRoot();

        iniciarComponentes(root);

        SharedPreferences ler = getActivity().getApplicationContext().getSharedPreferences(
                "usuario", Context.MODE_PRIVATE);

        passarDados(ler);



        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        verficarSenha(queue, ler);

        alergiaUsuario();

        return root;
    }

    private void formatarData(SharedPreferences ler) {
        String NascSemFormat = ler.getString("dataNasc", "");
        SimpleDateFormat entrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat saida = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date data = entrada.parse(NascSemFormat);
            String resultado = saida.format(data);
            dataNascimento.setText(resultado);
        }catch (ParseException exc){
            Log.d("CATCH", ">>>>>>>>>>>>" + exc);
        }
        
    }

    private void alergiaUsuario() {
        mostrarAlergia.setOnClickListener(v->{
            Intent intent = new Intent(getActivity().getApplicationContext(), ListaAlergiaUsuario.class);
            startActivity(intent);
        });
    }

    private void passarDados(SharedPreferences ler) {
        nome.setText(ler.getString("nome", ""));
        sobreNome.setText(ler.getString("SobreNome", ""));
        email.setText(ler.getString("email", ""));

        formatarData(ler);
        desativarCliqueNosCampos();
    }

    private void desativarCliqueNosCampos() {
         nome.setFocusable(false);
         nome.setClickable(false);
         nome.setLongClickable(false);
         sobreNome.setFocusable(false);
         sobreNome.setClickable(false);
         sobreNome.setLongClickable(false);
         email.setFocusable(false);
         email.setClickable(false);
         email.setLongClickable(false);
         dataNascimento.setFocusable(false);
         dataNascimento.setClickable(false);
         dataNascimento.setLongClickable(false);
    }

    private void verficarSenha(RequestQueue queue, SharedPreferences ler) {
        alterarSenha.setOnClickListener(v -> {
            if (senhaAtual.getText().toString().equals(ler.getString("senha", ""))){
                if (senhaNova.getText().toString().equals(confirmarSenha.getText().toString())){
                    editarSenha(queue, ler);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Os campos de nova" +
                            " senha não coincidem, favor tente novamente!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Senha atual incorreta" +
                        ", favor tente novamente!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void iniciarComponentes(View root) {

        nome = root.findViewById(R.id.editNomeUser);
        sobreNome = root.findViewById(R.id.editSobreNomeUser);
        email = root.findViewById(R.id.editEmailUser);
        dataNascimento = root.findViewById(R.id.editDataNascimentoUser);
        senhaAtual = root.findViewById(R.id.editSenhaAtual);
        senhaNova = root.findViewById(R.id.editSenhaNova);
        confirmarSenha = root.findViewById(R.id.editConfirmarSenha);
        alterarSenha = root.findViewById(R.id.txtAcaoAlterarSenha);
        mostrarAlergia = root.findViewById(R.id.btnMostrarAlergiaUsuario);
    }

    private void editarSenha(RequestQueue queue, SharedPreferences ler) {

        int timeout = 20000;
        RetryPolicy policy = new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        String endpoint = "http://10.0.2.2:5000/api/Usuario/Editar/?email="+ler.getString("email", "")
                +"&senha="+ler.getString("senha", "")+"&senhaNova="+senhaNova.getText().toString();

        StringRequest request = new StringRequest(Request.Method.PUT, endpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                SharedPreferences salvar = getActivity().getApplicationContext().
                        getSharedPreferences("usuario", Context.MODE_PRIVATE);

                SharedPreferences.Editor gravar = salvar.edit();
                gravar.putString("senha", senhaNova.getText().toString());
                gravar.commit();

                senhaAtual.setText("");
                senhaNova.setText("");
                confirmarSenha.setText("");

                Toast.makeText(getActivity().getApplicationContext(), "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(request);


    }
}