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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;


public class MainActivity extends AppCompatActivity {

    //private static final int PERMISSION_CODE = 1;
    //private static final int CAPTURE_CODE = 1001;
    private AppCompatButton btFoto, enviarFoto;
    private ImageView fotinha;
    private TextView tvTeste;

    //Uri img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String url = "https://jsonplaceholder.typicode.com/todos/1";

        iniciarComponentes();

        JsonObjectRequest obgJs = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int userId = response.getInt("userId");
                    int id = response.getInt("id");
                    String title = response.getString("title");
                    boolean completed = response.getBoolean("completed");

                    tvTeste.setText(userId + "/n" + id + "/n" + title + "/n" + completed);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    tvTeste.setText("ERROR");
            }
        });

        if (!fotinha.equals(null)) {
        fotinha.setVisibility(View.INVISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(obgJs);
        }


        btFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                resultFoto.launch(intent);
            }
        });

    }

    //por enquanto vou tentar fazer um botão para enviar a foto para a API
    //depois de enviar vou receber um resultado que no caso vai ser a bula abrindo
    //posso fazer automaticamente colocando um handler talvez? Mas por enquanto fica assim
    //vou jogar a bula em uma nova Activity ou Fragment, depende do que for melhor

    //Aqui estou convertendo a foto em um array de bytes, porém como não vejo como aplicar isto agora
    //vou deixar comentado para mais tarde puxar o código
    /*public void enviarImg(View v, Bitmap img, String url){
        Intent intent = new Intent();

        ByteArrayOutputStream arrayBytes = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 100,arrayBytes);
        byte[] byteArray = arrayBytes.toByteArray();

        //nesse starActivity, ele ja vai trazer a bula pra mim
        startActivity(intent);
    }*/

    private void iniciarComponentes() {
        fotinha = findViewById(R.id.fotoCamera);
        btFoto = findViewById(R.id.fotobtn);
        enviarFoto = findViewById(R.id.solicitar_resposta_btn);
        tvTeste = findViewById(R.id.testAPI);
    }

    ActivityResultLauncher<Intent> resultFoto = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK){
                    Bundle extras = result.getData().getExtras();
                    Bitmap img = (Bitmap) extras.get("data");

                    fotinha.setImageBitmap(img);
                }
            }
    );




    //código que deu errado e por enquanto acho melhor focar em algo mais confiável
    /*private void tirarFoto(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "nova imagem");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Imagem da camera");
        img = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, img);
        startActivityForResult(intent, CAPTURE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSION_CODE:
                if (grantResults.length>0 && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED) {
                    tirarFoto();
                } else {
                    Toast.makeText(this, "Permissão Negada!", Toast.LENGTH_SHORT).show();
                }
        }

    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            fotinha.setImageURI(img);
        }
    }*/
}