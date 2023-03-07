package com.example.usarcamera;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {


    private AppCompatButton foto1, enviarFoto;
    private ImageView fotinha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fotinha = findViewById(R.id.foto);
        foto1 = findViewById(R.id.fotobtn);


        foto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION)
                            == PackageManager.PERMISSION_DENIED || checkSelfPermission
                            (Manifest.permission.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION) ==
                            PackageManager.PERMISSION_DENIED) {

                    }

                }

            }
        });

    }


    private void tirarFoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap img = (Bitmap) extras.get("data");
            fotinha.setImageBitmap(img);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}