package com.example.usarcamera;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

    private AppCompatButton btFoto, enviarFoto;
    private ImageView fotinha;

    //Uri img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciarComponentes();

        btFoto.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            resultFoto.launch(intent);
        });

    }

    ActivityResultLauncher<Intent> resultFoto = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK){
                    Bundle extras = result.getData().getExtras();
                    Bitmap img = (Bitmap) extras.get("data");

                    fotinha.setRotation(90);

                    fotinha.setImageBitmap(img);

                    analisarImagem(img, "https://scanremedio.cognitiveservices.azure.com/computervision/imageanalysis:analyze?api-version=2023-02-01-preview&features=read"
                            , "6c193a3b7d2747ae8fc02707a665fb7f");

                }
            }
    );

    private void analisarImagem(Bitmap img, String url, String chave) {
        //Inicia o objeto para conexão com o webservice do CustomVision
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.start();

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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("APP_BULA", ">>>>>>>>>>>> " + response.toString());
                //Se conseguir conectar...
                try {
                    //Recupera a informação da previsão
                    JSONArray retorno = response.getJSONArray("readResult");
                    if (retorno.length() != 0) { //Se houver contedúdo
                        JSONObject obj1 = retorno.getJSONObject(0);
                        obj1.getJSONArray("content");
                        Toast.makeText(MainActivity.this, "E: " + obj1, Toast.LENGTH_SHORT).show();
                        //Recupera o nome da Tag
                        String nomeTag = obj1.getString("Bula");
                        if (!nomeTag.equals(null)) { //Se a probabilidade for acima de 85%
                            Toast.makeText(MainActivity.this, "bula: " + nomeTag, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Não foi encontrado o item", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, "ERRO!" + e, Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("APP_BULA", ">>>>>>>>>>>> " + error);
                Log.w("APP_BULA", ">>>>>>>>>>>> " + error.getMessage());
                Log.w("APP_BULA", ">>>>>>>>>>>> " + error.getCause());
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Ocp-Apim-Subscription-Key", chave);
                headers.put("Content-Type", "application/octet-stream");
                return headers;
            }

            @Override
            public byte[] getBody() {
                return byteArray;
            }
        };
        //Executa a conexão
        requestQueue.add(jsonObjectRequest);
    }

    private void iniciarComponentes() {
        fotinha = findViewById(R.id.fotoCamera);
        btFoto = findViewById(R.id.fotobtn);
        enviarFoto = findViewById(R.id.solicitar_resposta_btn);
    }

}