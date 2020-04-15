package com.example.todo_list;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasySplashScreen config = new EasySplashScreen(SplashScreenActivity.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(2000)
                .withBackgroundColor(Color.parseColor("#33ccff"))
                .withHeaderText("ToDo")
                .withFooterText("Lory & Arber")
                .withBeforeLogoText("HeARC")
                .withAfterLogoText("Développement Mobile")
                .withLogo(R.mipmap.ic_launcher_round);

        View easySplashScreen = config.create();
        setContentView(easySplashScreen);

    }
}
