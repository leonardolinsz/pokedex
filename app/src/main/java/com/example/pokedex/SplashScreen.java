package com.example.pokedex;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        ImageView gifImageView = findViewById(R.id.pikachugif);

        Glide.with(this)
                .asGif()
                .load(R.drawable.pikachugif)
                .into(gifImageView);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreen.this, Home.class);
            startActivity(intent);
            finish();
        }, 4000); // 4000ms = 4 segundos
    }
}