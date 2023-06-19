package com.example.usarcamera.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.example.usarcamera.R;

public class TutorialActivity extends AppCompatActivity {

    private ImageSwitcher switcher;
    private AppCompatButton anterior, proximo, sairTutorial;
    private int posicao = 0;
    private int[] img = {R.drawable.tela_home, R.drawable.tela_alergia, R.drawable.tela_historico,
            R.drawable.tela_user, R.drawable.tela_user2, R.drawable.tela_listar,
            R.drawable.tela_camera, R.drawable.tela_bula, R.drawable.tela_bula_resumo,
            R.drawable.tela_favorito};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        iniciarComponentes();
        colocarImagens();
        navegarNoApp();
    }

    private void navegarNoApp() {
        sairTutorial.setOnClickListener(v ->{
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
    }

    private void colocarImagens() {
        switcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imgView = new ImageView(getApplicationContext());
                return imgView;
            }
        });
        switcher.setImageResource(img[posicao]);

        proximo.setOnClickListener(v -> {
            if (posicao<img.length-1){
                posicao++;
                switcher.setImageResource(img[posicao]);
            }
        });

        anterior.setOnClickListener(v ->{
            if (posicao>0){
                posicao--;
                switcher.setImageResource(img[posicao]);
            }
        });
    }

    private void iniciarComponentes() {
        switcher = findViewById(R.id.imgSwitcher);
        anterior = findViewById(R.id.btnAnterior);
        proximo = findViewById(R.id.btnProximo);
        sairTutorial = findViewById(R.id.SairTutorial);
    }
}