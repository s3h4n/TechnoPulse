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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private TextInputLayout usernameLayout, passwordLayout, confirmLayout, emailLayout;
    private TextInputEditText usernameInput, passwordInput, confirmInput, emailInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Bind layouts (matching activity_register.xml) :contentReference[oaicite:0]{index=0}
        usernameLayout = findViewById(R.id.uUsername);
        passwordLayout = findViewById(R.id.uPassword);
        confirmLayout = findViewById(R.id.uConfirmPassword);
        emailLayout = findViewById(R.id.uEmail);

        // Bind inputs
        usernameInput = findViewById(R.id.inputUsername);
        passwordInput = findViewById(R.id.inputPassword);
        confirmInput = findViewById(R.id.inputConfirmPassword);
        emailInput = findViewById(R.id.inputEmail);

        Button registerButton = findViewById(R.id.btnRegister);
        Button loginButton = findViewById(R.id.btnLogin);

        // Clear errors immediately on edit
        TextWatcher clearErrorWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int st, int b, int c) {
            }

            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c) {
                usernameLayout.setError(null);
                passwordLayout.setError(null);
                confirmLayout.setError(null);
                emailLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable e) {
            }
        };
        usernameInput.addTextChangedListener(clearErrorWatcher);
        passwordInput.addTextChangedListener(clearErrorWatcher);
        confirmInput.addTextChangedListener(clearErrorWatcher);
        emailInput.addTextChangedListener(clearErrorWatcher);

        registerButton.setOnClickListener(view -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String confirmPassword = confirmInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();

            if (validateRegistration(username, email, password, confirmPassword)) {
                registerUser(username, email, password);
            }
        });

        loginButton.setOnClickListener(view -> finish());
    }

    private boolean validateRegistration(String username, String email,
                                         String password, String confirmPassword) {
        if (username.isEmpty()) {
            usernameLayout.setError("Username is required");
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
        if (!password.equals(confirmPassword)) {
            confirmLayout.setError("Passwords don't match");
            return false;
        }
        if (email.isEmpty()) {
            emailLayout.setError("Email is required");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Enter a valid email");
            return false;
        }
        return true;
    }

    private void registerUser(String username, String email, String password) {
        showProgressDialog("Creating account...");
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates =
                                new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
                                        .build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(profileTask -> {
                                    if (profileTask.isSuccessful()) {
                                        saveUserToFirestore(user.getUid(), username, email);
                                    } else {
                                        dismissProgressDialog();
                                        Toast.makeText(this,
                                                "Failed to update profile: " + profileTask.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        dismissProgressDialog();
                        Toast.makeText(this,
                                "Registration failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToFirestore(String userId, String username, String email) {
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("email", email);
        user.put("createdAt", System.currentTimeMillis());

        db.collection("users").document(userId)
                .set(user)
                .addOnCompleteListener(task -> {
                    dismissProgressDialog();
                    if (task.isSuccessful()) {
                        Toast.makeText(this,
                                "Registration successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this,
                                "Failed to save user data: " + task.getException(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
