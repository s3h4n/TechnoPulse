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

public class LoginActivity extends BaseActivity {

    private FirebaseAuth mAuth;

    private TextInputLayout usernameLayout, passwordLayout;
    private TextInputEditText usernameInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        usernameLayout = findViewById(R.id.uEmail);
        passwordLayout = findViewById(R.id.uPassword);
        usernameInput = findViewById(R.id.inputEmail);
        passwordInput = findViewById(R.id.inputPassword);
        Button loginButton = findViewById(R.id.btnLogin);
        Button registerButton = findViewById(R.id.btnRegister);

        // Clear errors as user types
        usernameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int st, int b, int c) {
            }

            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c) {
                usernameLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable e) {
            }
        });
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int st, int b, int c) {
            }

            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c) {
                passwordLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable e) {
            }
        });

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

    private void signInUser(String email, String password) {
        showProgressDialog("Signing in...");
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    dismissProgressDialog();
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Toast.makeText(this,
                                "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        }
    }
}
