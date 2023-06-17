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

    ImageSwitcher switcher;
    AppCompatButton anterior, proximo, sairTutorial;
    int posicao = 0;
    int[] img = {R.drawable.TelaHome, R.drawable.TelaAlergia, R.drawable.TelaHistorico, R.drawable.TelaUser, R.drawable.TelaUser2,
            R.drawable.TelaListar, R.drawable.TelaCamera, R.drawable.TelaBula, R.drawable.TelaBulaResumo, R.drawable.TelaFavorito};

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