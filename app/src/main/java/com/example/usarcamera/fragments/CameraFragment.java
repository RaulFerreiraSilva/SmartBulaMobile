package com.example.usarcamera.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.usarcamera.R;
import com.example.usarcamera.activitys.BulaActivity;
import com.example.usarcamera.classes.Remedio;
import com.example.usarcamera.databinding.FragmentCameraBinding;
import com.google.gson.JsonArray;

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

    private TextView bula;

    private int referencia = R.drawable.foto_dipirona;

    private String[] remediosCadastrados = {"dipirona"};




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCameraBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        View layout = inflater.inflate(R.layout.activity_bula, container, false);

        RequestQueue queue= Volley.newRequestQueue(getActivity().getApplicationContext());


        SharedPreferences ler = getActivity().getApplicationContext().getSharedPreferences(
                "usuario", Context.MODE_PRIVATE);

        Bitmap fotinha = BitmapFactory.decodeResource(getResources(),
                referencia);

        iniciarComponentes(root, layout);
        pesquisarBula(layout, queue, ler);
        tirarFoto(fotinha);
        abrirMicrofone();
        fotoRemedio.setImageBitmap(fotinha);



        return root;
    }

    private void abrirMicrofone(){
        btnPesquisarFala.setOnClickListener(v ->{
            analisarFala();
        });
    }

    private void analisarFala() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 5);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale agora!");
        startActivityForResult(intent, 111);
        Log.d("ANALISARFALA", ">>>>>>>>>>>" + intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ANTESDOIF", ">>>>>>>>>>>>>>" + data);
        if (requestCode == 111 && resultCode == getActivity().RESULT_OK){
            pesquisarPorTexto.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).
                    get(0));
            Log.d("DEPOISDOIF", ">>>>>>>>>>>>" + data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
        }
    }

    private void tirarFoto(Bitmap fotinha) {
        fotoRemedio.setOnClickListener(v -> {

                /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                resultFoto.launch(intent);*/

                analisarImagem(fotinha,
                        "https://scanremedio.cognitiveservices.azure.com/computervision/imageanalysis:analyze?api-version=2023-02-01-preview&features=read",
                        "6c193a3b7d2747ae8fc02707a665fb7f");


        });
    }

    private void pesquisarBula(View layout, RequestQueue queue, SharedPreferences ler) {
        pesquisar.setOnClickListener(v -> {

                    retornarBula(queue, layout, ler);

        });
    }

    private void iniciarComponentes(View root, View layout) {

        bula = layout.findViewById(R.id.txtBula);
        fotoRemedio = root.findViewById(R.id.imgRemedio);
        pesquisar = root.findViewById(R.id.btnPesquisar);
        pesquisarPorTexto = root.findViewById(R.id.edit_search);
       btnPesquisarFala = root.findViewById(R.id.btnFalar);
    }

    private void retornarBula(RequestQueue queue, View layout, SharedPreferences ler) {


        Log.d("NOMEREMEDIO", ">>>>>>>>>>>>>>>" + ler.getString("remedioEncontrado", ""));

        String teste = "dipirona";

        String endpoint = "http://10.0.2.2:5000/api/Remedio?response="+teste;
                //ler.getString("remedioEncontrado", "");

        List<Remedio> lista = new ArrayList<>();
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

                        lista.add(remedio);

                        SharedPreferences salvar = getActivity().getApplicationContext()
                                .getSharedPreferences("usuario", Context.MODE_PRIVATE);

                        SharedPreferences.Editor gravar = salvar.edit();
                        gravar.putString("idMed", response.getString("idMedicamento"));
                        gravar.putString("bula", response.getString("bula"));
                        gravar.putString("resumoBula", response.getString("resumoBula"));
                        gravar.putString("contraIndicacao", response.getString("contraIndicacao"));
                        gravar.putString("recomendadoPara", response.getString("recomendadoPara"));

                        gravar.commit();

                        Handler espera = new Handler();
                        espera.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                bula.setText(ler.getString("bula", ""));
                                Intent intent = new Intent(getActivity().getApplicationContext(), BulaActivity.class);
                                startActivity(intent);
                            }
                        }, 3000);
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
                Log.d("ERRO", ">>>>>>>>>>>>>>" + error);
                Log.d("ERRO", ">>>>>>>>>>>>>>" + error.getMessage());
                Log.d("ERRO", ">>>>>>>>>>>>>>" + error.getCause());
            }
        }){
            /*@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");

                return headers;
            }*/
        };
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
                                SharedPreferences salvar = getActivity().getApplicationContext()
                                        .getSharedPreferences("usuario", Context.MODE_PRIVATE);
                                SharedPreferences.Editor gravar = salvar.edit();
                                gravar.putString("remedioEncontrado", palavra);
                                gravar.commit();
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