package com.sehanw.technopulse;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Activity for user login, handling email/password authentication via Firebase.
 * Provides input validation, progress indication, and navigation to home or register screens.
 */
public class LoginActivity extends BaseActivity {

    // Firebase authentication instance
    private FirebaseAuth mAuth;

    // UI elements for username and password input
    private TextInputLayout usernameLayout, passwordLayout;
    private TextInputEditText usernameInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize activity and set content view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        usernameLayout = findViewById(R.id.uEmail);
        passwordLayout = findViewById(R.id.uPassword);
        usernameInput = findViewById(R.id.inputEmail);
        passwordInput = findViewById(R.id.inputPassword);
        Button loginButton = findViewById(R.id.btnLogin);
        Button registerButton = findViewById(R.id.btnRegister);

        // Setup input validation and button click listeners
        setupInputListeners();
        loginButton.setOnClickListener(view -> {
            String email = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            if (validateLoginInput(email, password)) {
                signInUser(email, password);
            }
        });

        registerButton.setOnClickListener(view ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }

    /**
     * Sets up TextWatchers to clear input errors as the user types.
     */
    private void setupInputListeners() {
        usernameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                usernameLayout.setError(null); // Clear error when text changes
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });

        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordLayout.setError(null); // Clear error when text changes
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });
    }

    /**
     * Validates the email and password inputs.
     *
     * @param email    The email entered by the user.
     * @param password The password entered by the user.
     * @return True if inputs are valid, false otherwise.
     */
    private boolean validateLoginInput(String email, String password) {
        if (email.isEmpty()) {
            usernameLayout.setError("Email is required");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            usernameLayout.setError("Enter a valid email");
            return false;
        }
        if (password.isEmpty()) {
            passwordLayout.setError("Password is required");
            return false;
        }
        if (password.length() < 6) {
            passwordLayout.setError("Password must be at least 6 characters");
            return false;
        }
        return true;
    }

    /**
     * Attempts to sign in the user with the provided email and password using Firebase Authentication.
     *
     * @param email    The user's email.
     * @param password The user's password.
     */
    private void signInUser(String email, String password) {
        showProgressDialog("Signing in...");
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    dismissProgressDialog();
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user); // Navigate to home screen on successful login
                    } else {
                        Toast.makeText(this,
                                "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    /**
     * Updates the UI based on the user's authentication state.
     * If the user is authenticated, navigates to the HomeActivity.
     *
     * @param user The current FirebaseUser.
     */
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish(); // Finish LoginActivity so user can't navigate back
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // If user is already signed in, go directly to HomeActivity
            updateUI(currentUser);
        }
    }
}
