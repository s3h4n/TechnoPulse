package com.sehanw.technopulse;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * The main entry point of the application.
 * This activity checks the current authentication status and navigates
 * the user to either the Home screen (if logged in) or the Login screen (if not logged in).
 * It extends {@link BaseActivity} for common functionalities like theme setting.
 */
public class MainActivity extends BaseActivity {

    /**
     * Called when the activity is first created.
     * This method initializes Firebase Authentication and checks the current user's status.
     * Based on the authentication status, it redirects the user to the appropriate activity.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        // Determine the next activity based on whether a user is currently logged in.
        // If currentUser is not null, the user is logged in, so navigate to HomeActivity.
        // Otherwise, navigate to LoginActivity.
        Class<?> next = currentUser != null
                ? HomeActivity.class
                : LoginActivity.class;

        startActivity(new Intent(this, next)); // Start the determined activity.
        finish(); // Finish MainActivity so the user cannot navigate back to it using the back button.
    }
}