package com.example.usarcamera;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final String TAG= "MainActivity";
    private AppCompatButton btFoto;
    private ImageView fotinha;
    private TextView txtResultado;

    //Uri img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //referencias aos componentes da tela usados
        iniciarComponentes();

        //evento de clique do botão que chama a câmera
        btFoto.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            resultFoto.launch(intent);
        });

    }

    //recupera o resultado obtido pela câmera e coloca no ImageView
    ActivityResultLauncher<Intent> resultFoto = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                try {
                    if (result.getResultCode() == RESULT_OK){
                        Bundle extras = result.getData().getExtras();
                        Bitmap img = (Bitmap) extras.get("data");

                        fotinha.setRotation(90);

                        fotinha.setImageBitmap(img);

                        analisarImagem(img, "https://scanremedio.cognitiveservices.azure.com/computervision/imageanalysis:analyze?api-version=2023-02-01-preview&features=read"
                                , "6c193a3b7d2747ae8fc02707a665fb7f");
                    }

                }
                catch (NullPointerException e){
                    Log.w(TAG, "Error: " + e.getMessage());
                }


            }
    );

    //método responsável por analisar a imagem e fazer toda a requisição a API para trazer o retorno
    private void analisarImagem(Bitmap img, final String endpoint, final String key) {
        //Inicia o objeto para conexão com o webservice do CustomVision
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        //requestQueue.start();

        //Converte a imagem para ser enviada via URL
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        //imagem.recycle();

        //Configura o envio ao webservice do CustomVision
        JSONObject dados = new JSONObject();
        try {
            dados.put("", byteArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Configura a requisição ao webservice
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, endpoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("APP_CUSTOM_VISION", ">>>>>>>>>>>> " + response.toString());

                //o retorno por enquanto está sendo trazido ao front por um setText, porém depois
                //o retorno irá vir por meio da bula do remédio
                try {
                    JSONObject obj = response.getJSONObject("readResult");
                    String text = obj.getString("content");
                    if (!text.equals(null)){
                            txtResultado.setText(text);

                    } else {
                        txtResultado.setText("ERRO!");
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "onResponse: " + e.getMessage());
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
        })
        {
            //cabeçalho da api
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
        //o Volley tem um TimeOut muito curto, então tive que estender o tempo para 20s pois assim
        //consigo receber o retorno da API
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Executa a conexão
        requestQueue.add(jsonObjectRequest);
    }

    private void iniciarComponentes() {
        fotinha = findViewById(R.id.fotoCamera);
        btFoto = findViewById(R.id.fotobtn);
        txtResultado = findViewById(R.id.txtResultado);
    }

}