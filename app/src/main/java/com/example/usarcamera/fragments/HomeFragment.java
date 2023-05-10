package com.example.usarcamera.fragments;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.usarcamera.R;
import com.example.usarcamera.activitys.LoginActivity;
import com.example.usarcamera.classes.Pessoa;
import com.example.usarcamera.databinding.FragmentHomeBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class HomeFragment extends Fragment {

    private TextView nomePessoa;

    private ImageView capsulaSair;

    private FragmentHomeBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();


        nomePessoa = root.findViewById(R.id.bem_vindo);
        capsulaSair = root.findViewById(R.id.img_sair);

        SharedPreferences ler = getActivity().getApplicationContext().getSharedPreferences("usuario", Context.MODE_PRIVATE);

        nomePessoa.setText(ler.getString("nome", ""));

        Log.d("RETORNO", ">>>>>>>>>>>" + nomePessoa);

        /*mudarBemVindo();
        iniciarComponentes(root);
        logOut();*/

        return root;
    }

   /*private void logOut() {
        capsulaSair.setOnClickListener(View -> sair());
    }

    private void sair() {
        Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void mudarBemVindo() {


    }

    private void iniciarComponentes(View root) {

    }*/
}