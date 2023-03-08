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
import android.widget.Toast;

import java.io.ByteArrayOutputStream;


public class MainActivity extends AppCompatActivity {

    //private static final int PERMISSION_CODE = 1;
    //private static final int CAPTURE_CODE = 1001;
    private AppCompatButton btFoto, enviarFoto;
    private ImageView fotinha;

    //Uri img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciarComponentes();
        enviarFoto = enviarImg(view);

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
    public void enviarImg(View v, Bitmap img, String url){
        Intent intent = new Intent();

        ByteArrayOutputStream arrayBytes = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 100,arrayBytes);
        byte[] byteArray = arrayBytes.toByteArray();

        //nesse starActivity, ele ja vai trazer a bula pra mim
        startActivity(intent);
    }

    private void iniciarComponentes() {
        fotinha = findViewById(R.id.fotoCamera);
        btFoto = findViewById(R.id.fotobtn);
        enviarFoto = findViewById(R.id.solicitar_resposta_btn);
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