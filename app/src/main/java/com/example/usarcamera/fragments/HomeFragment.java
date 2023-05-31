package com.example.usarcamera.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.usarcamera.R;
import com.example.usarcamera.activitys.AlergiaActivity;
import com.example.usarcamera.activitys.LoginActivity;
import com.example.usarcamera.classes.Alergia;
import com.example.usarcamera.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView nomePessoa;

    private ListView listaDeAlergias;

    private TextView tituloAlertDialog, mensagemAlertDialog, mostrarAlergia;

    private AppCompatButton btnConfirmarSaida, btnCancelarSaida;

    private ImageView capsulaSair;

    private FragmentHomeBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        View layout = inflater.inflate(R.layout.dialog, container, false);

        iniciarCompontes(root, layout);

        SharedPreferences ler = getActivity().getApplicationContext().getSharedPreferences("usuario", Context.MODE_PRIVATE);

        deslogar(root, layout);
        mudarBemVindo(ler);
        abrirTelaAlergia();
        mudarBemVindo(ler);
        return root;
    }

    /*private void listarAlergias(RequestQueue queue, View alergiaView){


        String endpoint = "http://10.0.2.2:5000/api/Alergia/Listar";


        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, endpoint, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null && response.length() > 0) {
                }
                abrirTela();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }*/

    private void abrirTelaAlergia() {
        mostrarAlergia.setOnClickListener(v ->{
            Intent intent = new Intent(getActivity().getApplicationContext(), AlergiaActivity.class);
            startActivity(intent);
        });
    }

    private void deslogar(View root, View layout) {
        capsulaSair.setOnClickListener(v ->{

                    AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());
                    //R.style.AlertDialogTheme);

                    builder.setView(layout);

                    tituloAlertDialog.setText(R.string.txtTituloDialog);
                    mensagemAlertDialog.setText(R.string.txtMensagemDialog);
                    btnConfirmarSaida.setText(R.string.btnConfirmarSaida);
                    btnCancelarSaida.setText(R.string.btnCancelarSaida);

                    final AlertDialog dialog = builder.create();

                    dialog.show();
                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            ViewGroup parent = (ViewGroup) layout.getParent();
                            if (parent != null)parent.removeView(layout);
                        }
                    });


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






        });
    }

    private void iniciarCompontes(View root, View layout) {
        tituloAlertDialog = layout.findViewById(R.id.tituloDialog);
        nomePessoa = root.findViewById(R.id.bem_vindo);
        capsulaSair = root.findViewById(R.id.img_sair);
        mostrarAlergia = root.findViewById(R.id.txtMostrarAlergias);
        mensagemAlertDialog = layout.findViewById(R.id.mensagemDialog);
        btnConfirmarSaida = layout.findViewById(R.id.btnConfirmar);
        btnCancelarSaida = layout.findViewById(R.id.btnCancelar);
    }

    private void mudarBemVindo(SharedPreferences ler) {

        nomePessoa.setText("Olá " + ler.getString("nome", ""));
    }

}