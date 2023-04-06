package com.example.usarcamera.activitys;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.usarcamera.R;
import com.example.usarcamera.databinding.ActivityMainBinding;
import com.example.usarcamera.fragments.CameraFragment;
import com.example.usarcamera.fragments.FavoritoFragment;
import com.example.usarcamera.fragments.HistoricoFragment;
import com.example.usarcamera.fragments.HomeFragment;
import com.example.usarcamera.fragments.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

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

    private ActivityMainBinding binding;
    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment = new HomeFragment();
    private CameraFragment cameraFragment = new CameraFragment();
    private FavoritoFragment favoritoFragment = new FavoritoFragment();
    private HistoricoFragment historicoFragment = new HistoricoFragment();
    private UserFragment userFragment = new UserFragment();

    //Uri img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        iniciarComponentes();
        replaceFragment(new HomeFragment());
        binding.btnNav.setBackground(null);
        binding.btnNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.fragHome: replaceFragment(new HomeFragment());break;
                case R.id.fragFavorito: replaceFragment(new FavoritoFragment());break;
                case R.id.fragHistorico: replaceFragment(new HistoricoFragment());break;
                case R.id.fragCamera: replaceFragment(new CameraFragment());break;
                case R.id.fragUser: replaceFragment(new UserFragment());break;
            }
            return true;
        });


        //referencias aos componentes da tela usados


        /*getSupportFragmentManager().beginTransaction().replace(androidx.core.R.id.action_container,
                homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.fragHome: getSupportFragmentManager().beginTransaction().replace(
                            androidx.fragment.R.id.action_container, homeFragment).commit();
                    return true;
                    case R.id.fragCamera: getSupportFragmentManager().beginTransaction().replace(
                            androidx.fragment.R.id.action_container, cameraFragment).commit();
                        return true;
                    case R.id.fragFavorito: getSupportFragmentManager().beginTransaction().replace(
                            androidx.fragment.R.id.action_container, favoritoFragment).commit();
                        return true;
                    case R.id.fragHistorico: getSupportFragmentManager().beginTransaction().replace(
                            androidx.fragment.R.id.action_container, historicoFragment).commit();
                        return true;
                    case R.id.fragUser: getSupportFragmentManager().beginTransaction().replace(
                            androidx.fragment.R.id.action_container, userFragment).commit();
                        return true;
                }

                return false;
            }
        });*/

        //evento de clique do botão que chama a câmera
        /*btFoto.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            resultFoto.launch(intent);
        });*/

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }

    //recupera o resultado obtido pela câmera e coloca no ImageView
    /*ActivityResultLauncher<Intent> resultFoto = registerForActivityResult(
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
    }*/

    private void iniciarComponentes() {
       /* fotinha = findViewById(R.id.fotoCamera);
        btFoto = findViewById(R.id.fotobtn);
        txtResultado = findViewById(R.id.txtResultado);*/
        bottomNavigationView = findViewById(R.id.btnNav);
    }

}