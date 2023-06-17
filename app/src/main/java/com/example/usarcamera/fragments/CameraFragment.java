package com.example.usarcamera.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.usarcamera.R;
import com.example.usarcamera.activitys.BulaActivity;
import com.example.usarcamera.classes.Remedio;
import com.example.usarcamera.databinding.FragmentCameraBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class CameraFragment extends Fragment {

    private FragmentCameraBinding binding;

    private ImageButton fotoRemedio, btnPesquisarFala;

    private AppCompatButton pesquisar;

    private EditText pesquisarPorTexto;

    private TextView tituloAlertDialogCamera, mensagemAlertDialogCamera;

    private AppCompatButton btnConfirmar, btnCancelar;

    private String[] remediosCadastrados = {"dipirona"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCameraBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        View layout = inflater.inflate(R.layout.dialog, container, false);

        RequestQueue queue= Volley.newRequestQueue(getActivity().getApplicationContext());

        SharedPreferences ler = getActivity().getApplicationContext().getSharedPreferences(
                "usuario", Context.MODE_PRIVATE);

        iniciarComponentes(root, layout);
        pesquisar(queue, ler, root, layout);
        tirarFoto();
        analisarFala(ler);

        return root;
    }

    private void confirmarConsulta(View root, View layout, RequestQueue queue) {

            AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());

            builder.setView(layout);

            String principioAtivo = pesquisarPorTexto.getText().toString();

            tituloAlertDialogCamera.setText("Cuidado!");
            mensagemAlertDialogCamera.setText("Você marcou que possui alergia a " + principioAtivo +
                    " e está prestes a consulta a bula deste remédio, prossiga ciente deste fato!");
            btnConfirmar.setText("Sim");
            btnCancelar.setText("Não");

            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null)parent.removeView(layout);

            final AlertDialog dialog = builder.create();

            dialog.show();
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    ViewGroup parent = (ViewGroup) layout.getParent();
                    if (parent != null)parent.removeView(layout);
                }
            });


            btnConfirmar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    retornarBula(queue);
                }
            });

            btnCancelar.setOnClickListener(new View.OnClickListener() {
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


    private void analisarFala(SharedPreferences ler) {
        btnPesquisarFala.setOnClickListener(v->{

            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale agora!");
            getActivity().startActivityForResult(intent, 111);
            Log.d("ANALISARFALA", ">>>>>>>>>>>" + intent);

            Handler tempo = new Handler();
            tempo.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String nome = ler.getString("nomeRemedio", "");
                    retornoDaFala(nome);
                }
            }, 9000);

        });

    }

    private void tirarFoto() {
        fotoRemedio.setOnClickListener(v -> {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                resultFoto.launch(intent);
        });
    }

    private void pesquisar(RequestQueue queue, SharedPreferences ler, View root, View layout) {
        pesquisar.setOnClickListener(v -> {

            verificaAlergia(queue, ler, root, layout);
        });
    }

    private void verificaAlergia(RequestQueue queue, SharedPreferences ler, View root, View layout) {

        List<String> lista = new ArrayList<>();

        String endpoint = "http://10.0.2.2:5000/api/Alergia/ListarAlergiaUsuario/?usuarioId=" +
                ler.getString("id", "");

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, endpoint, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response != null && response.length() > 0){
                            for (int i = 0; i < response.length(); i++){
                                try {
                                    JSONObject resposta = response.getJSONObject(i);

                                    lista.add(resposta.getString("tipo_Alergia"));

                                    String remedio = pesquisarPorTexto.getText().toString();

                                    for (String alergiaRemedio : lista){
                                        Log.d("ALERGIAS", ">>>>>>>>>>" + alergiaRemedio);
                                        if (alergiaRemedio.equals(remedio)){
                                            Log.d("TEM ALERGIA", ">>>>>>>>>" + "TEM ALERGIA");
                                            confirmarConsulta(root, layout, queue);
                                        }else{
                                            Log.d("else", ">>>>>>>>>>" + alergiaRemedio);
                                            retornarBula(queue);
                                        }
                                        break;
                                    }

                                } catch (JSONException exc){
                                    exc.printStackTrace();
                                }
                            }
                        }
                        Log.d("RESPONSE", ">>>>>>>>>" + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }

    private void retornoDaFala(String nome){
        pesquisarPorTexto.setText(nome);

        Log.d("CAMPOPREENCHIDO", ">>>>>>>>>>" + pesquisarPorTexto.getText().toString());
    }

    private void iniciarComponentes(View root, View layout) {
        fotoRemedio = root.findViewById(R.id.imgRemedio);
        pesquisar = root.findViewById(R.id.btnPesquisar);
        pesquisarPorTexto = root.findViewById(R.id.edit_search);
        btnPesquisarFala = root.findViewById(R.id.btnFalar);

        //componentes inflados de um arquivo xml
        tituloAlertDialogCamera = layout.findViewById(R.id.tituloDialog);
        mensagemAlertDialogCamera = layout.findViewById(R.id.mensagemDialog);
        btnConfirmar = layout.findViewById(R.id.btnConfirmar);
        btnCancelar = layout.findViewById(R.id.btnCancelar);
    }

    private void retornarBula(RequestQueue queue) {

        String endpoint = "http://10.0.2.2:5000/api/Remedio?response="+
                pesquisarPorTexto.getText().toString();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, endpoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null && response.length() > 0){
                    try {
                        Remedio remedio = new Remedio(
                                response.getInt("idMedicamento"),
                                response.getString("bula"),
                                response.getString("resumoBula"),
                                response.getString("principioAtivo"));

                        SharedPreferences salvar = getActivity().getApplicationContext()
                                .getSharedPreferences("usuario", Context.MODE_PRIVATE);

                        SharedPreferences.Editor gravar = salvar.edit();
                        gravar.putString("idMed", response.getString("idMedicamento"));
                        gravar.putString("bula", response.getString("bula"));
                        gravar.putString("resumoBula", response.getString("resumoBula"));
                        gravar.putString("principioAtivo", response.getString("principioAtivo"));
                        gravar.putString("contraIndicacao", response.getString("contraIndicacao"));
                        gravar.putString("recomendadoPara", response.getString("recomendadoPara"));

                        gravar.commit();

                        pesquisarPorTexto.setText("");

                        Handler espera = new Handler();
                        espera.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getActivity().getApplicationContext(), BulaActivity.class);
                                startActivity(intent);
                            }
                        }, 2000);
                    }catch (JSONException ex){

                        ex.printStackTrace();
                    }
                } else{
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Remedio não encontrado", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getActivity().getApplicationContext(), "Remedio não encontrado, verifique o nome e tente novamente!", Toast.LENGTH_SHORT).show();
                pesquisarPorTexto.setText("");
            }
        });
        queue.add(request);
    }

    ActivityResultLauncher<Intent> resultFoto = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                try {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Bundle extras = result.getData().getExtras();
                        Bitmap img = (Bitmap) extras.get("data");

                        fotoRemedio.setRotation(270);

                        fotoRemedio.setImageBitmap(img);


                        analisarImagem(img, "https://scanremedio.cognitiveservices.azure.com/computervision/imageanalysis:analyze?api-version=2023-02-01-preview&features=read"
                                , "6c193a3b7d2747ae8fc02707a665fb7f");

                    }

                }
                catch (NullPointerException e){
                    Log.w("TAG", "Error: " + e.getMessage());
                }


            }
    );

    private void analisarImagem(Bitmap img, final String endpoint, final String key){
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        JSONObject dados = new JSONObject();
        try {
            dados.put("", byteArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int timeout = 20000;
        RetryPolicy policy = new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, endpoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray retorno = response.getJSONObject("readResult").getJSONArray(
                            "pages");
                    JSONArray words = retorno.getJSONObject(0).getJSONArray("words");


                    for (int i=0;i< words.length(); i++){
                        JSONObject content = words.getJSONObject(i);
                        String resultadoJSON = content.getString("content");

                        for (String palavra : remediosCadastrados){
                           if (resultadoJSON.equals(palavra)){
                               Log.d("nomeRemedio", ">>>>>>>>>" + palavra);
                                pesquisarPorTexto.setText(palavra);
                           }
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("APP_BULA", "DEU ZIKA " + error);
                Log.d("APP_BULA", ">>>>>>>>>>>> " + error.getMessage());
                Log.d("APP_BULA", ">>>>>>>>>>>> " + error.getCause());
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Ocp-Apim-Subscription-Key", key);
                headers.put("Content-Type", "application/octet-stream");

                return headers;
            }

            @Override
            public byte[] getBody() {
                return byteArray;
            }
        };

        //Executa a conexão
        queue.add(request);
    }

}