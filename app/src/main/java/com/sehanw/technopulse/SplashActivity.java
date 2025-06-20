package com.sehanw.technopulse;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.google.android.material.imageview.ShapeableImageView;


public class SplashActivity extends BaseActivity {

    private static final int SPLASH_DURATION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ShapeableImageView logo = findViewById(R.id.splashLogo);
        logo.setScaleX(0.8f);
        logo.setScaleY(0.8f);

        logo.animate()
                .scaleX(1f)
                .scaleY(1f)
                .rotation(0)
                .setDuration(300)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();

        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }, SPLASH_DURATION);
    }

}
