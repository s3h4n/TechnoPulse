package com.sehanw.technopulse;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.google.android.material.imageview.ShapeableImageView;

/**
 * SplashActivity displays a splash screen with a logo animation
 * and then transitions to the MainActivity.
 */
public class SplashActivity extends BaseActivity {

    // Duration of the splash screen in milliseconds
    private static final int SPLASH_DURATION = 2000;

    /**
     * Called when the activity is first created.
     * Sets up the layout, initiates the logo animation,
     * and schedules the transition to MainActivity.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Find the logo ImageView
        ShapeableImageView logo = findViewById(R.id.splashLogo);
        // Set initial scale for animation
        logo.setScaleX(0.8f);
        logo.setScaleY(0.8f);

        // Animate the logo: scale up and rotate
        logo.animate()
                .scaleX(1f)
                .scaleY(1f)
                .rotation(0) // Assuming initial rotation is not 0 and we want to bring it to 0
                .setDuration(300) // Animation duration
                .setInterpolator(new AccelerateDecelerateInterpolator()) // Smooth animation
                .start();

        // Handler to delay the transition to MainActivity
        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }, SPLASH_DURATION);
    }

}