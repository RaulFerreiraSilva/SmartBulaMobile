package com.example.usarcamera.fragments;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
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

    private ConstraintLayout constr;

    private TextView tituloAlertDialog, mensagemAlertDialog;

    private AppCompatButton btnConfirmarSaida, btnCancelarSaida;

    private ImageView capsulaSair;

    private FragmentHomeBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        View layout = inflater.inflate(R.layout.dialog, container, false);

        tituloAlertDialog = layout.findViewById(R.id.tituloDialog);
        nomePessoa = root.findViewById(R.id.bem_vindo);
        capsulaSair = root.findViewById(R.id.img_sair);
        mensagemAlertDialog = layout.findViewById(R.id.mensagemDialog);
        btnConfirmarSaida = layout.findViewById(R.id.btnConfirmar);
        btnCancelarSaida = layout.findViewById(R.id.btnCancelar);
        constr = layout.findViewById(R.id.layoutDialogContainer);

        SharedPreferences ler = getActivity().getApplicationContext().getSharedPreferences("usuario", Context.MODE_PRIVATE);

        nomePessoa.setText(ler.getString("nome", ""));

        Log.d("RETORNO", ">>>>>>>>>>>" + nomePessoa.getText().toString());


        capsulaSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());
                        //R.style.AlertDialogTheme);

                builder.setView(layout);

                tituloAlertDialog.setText(R.string.txtTituloDialog);
                mensagemAlertDialog.setText(R.string.txtMensagemDialog);
                btnConfirmarSaida.setText(R.string.btnConfirmarSaida);
                btnCancelarSaida.setText(R.string.btnCancelarSaida);

                final AlertDialog dialog = builder.create();

                dialog.show();

                btnConfirmarSaida.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Handler atraso = new Handler();
                        atraso.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity().getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                            }
                        }, 2500);
                        dialog.dismiss();

                        Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                        startActivity(intent);

                    }
                });

                btnCancelarSaida.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        ViewGroup parent = (ViewGroup) layout.getParent();
                        if (parent != null) {
                            parent.removeView(layout);
                        }
                    }
                });
                if (dialog.getWindow() != null){dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(0));}




            }
        });
        mudarBemVindo();



        return root;
    }

    private void mudarBemVindo() {

    }


    private void logOut(View root) {
        try {
            root = binding.getRoot();

        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

}