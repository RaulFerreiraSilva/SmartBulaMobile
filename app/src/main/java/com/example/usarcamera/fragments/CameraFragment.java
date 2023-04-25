package com.example.usarcamera.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.example.usarcamera.databinding.FragmentCameraBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;


public class CameraFragment extends Fragment {

    private FragmentCameraBinding binding;
    private ImageButton fotoRemedio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCameraBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        fotoRemedio = root.findViewById(R.id.imgRemedio);

        fotoRemedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                resultFoto.launch(intent);
            }
        });

        return root;
    }

    ActivityResultLauncher<Intent> resultFoto = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                try {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Bundle extras = result.getData().getExtras();
                        Bitmap img = (Bitmap) extras.get("data");

                        fotoRemedio.setRotation(90);

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

        //o Volley tem um TimeOut muito curto, então tive que estender o tempo para 20s pois assim
        //consigo receber o retorno da API
        int timeout = 20000;
        RetryPolicy policy = new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, endpoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("APP_CUSTOM_VISION", ">>>>>>>>>>>> " + response.toString());

                //o retorno por enquanto está sendo trazido ao front por um setText, porém depois
                //o retorno irá vir por meio da bula do remédio
                try {
                    JSONObject obj = response.getJSONObject("readResult");
                    String text = obj.getString("content");
                    if (!text.equals(null)){
                        Log.d("SUCESSO", ">>>>>>>>>>>" + text);

                    } else {
                        Toast.makeText(getActivity(), "Erro", Toast.LENGTH_SHORT).show();;
                    }
                } catch (JSONException e) {
                    Log.d("ERRO", "onResponse: " + e.getMessage());
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